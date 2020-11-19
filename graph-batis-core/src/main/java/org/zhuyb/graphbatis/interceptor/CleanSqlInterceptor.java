package org.zhuyb.graphbatis.interceptor;

import com.google.common.base.CaseFormat;
import graphql.language.Field;
import graphql.language.Selection;
import graphql.language.SelectionSet;
import graphql.schema.DataFetchingEnvironment;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhuyb.graphbatis.DataFetchingEnvHolder;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author zhuyb
 * @date 2020/4/25
 * 如果没有
 * @see FourArgsInterceptor
 * 该interceptor无法生效
 */
@Intercepts(
        {
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        }
)
public class CleanSqlInterceptor implements Interceptor {

    public static final Logger logger = LoggerFactory.getLogger(CleanSqlInterceptor.class);
    public static final int BOUND_SQL_INDEX = 5;
    public static final int MAPPED_STATEMENT_INDEX = 0;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result;
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[MAPPED_STATEMENT_INDEX];
        if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
            result = invocation.proceed();
        } else {
            long startTime = System.currentTimeMillis();
            DataFetchingEnvironment dataFetchingEnvironment = DataFetchingEnvHolder.get();
            if (dataFetchingEnvironment != null) {
                Invocation changedInvocation = new Invocation(invocation.getTarget(), invocation.getMethod(), args);
                BoundSql originBoundSql = (BoundSql) args[BOUND_SQL_INDEX];
                String originSql = originBoundSql.getSql();
                if (logger.isInfoEnabled()) {
                    logger.info("origin sql {}", originSql.replaceAll("\n",""));
                }
                String cleanSql = getCleanSql(dataFetchingEnvironment, originSql);
                logger.debug("clean sql {}", cleanSql);
                BoundSql cleanBoundSql = new BoundSql(mappedStatement.getConfiguration(), cleanSql, originBoundSql.getParameterMappings(), originBoundSql.getParameterObject());
                args[BOUND_SQL_INDEX] = cleanBoundSql;
                logger.debug("clean sql cost {}ms", System.currentTimeMillis() - startTime);
                result = changedInvocation.proceed();
                DataFetchingEnvHolder.remove();
            } else {
                result = invocation.proceed();
            }
        }
        return result;
    }

    /**
     * 获取过滤后的SQL
     *
     * @param dataFetchingEnvironment
     * @param originSql
     * @return
     * @throws JSQLParserException
     */
    private String getCleanSql(DataFetchingEnvironment dataFetchingEnvironment, String originSql) throws JSQLParserException {
        Select cleanSelectSql = (Select) CCJSqlParserUtil.parse(originSql);
        PlainSelect selectBody = (PlainSelect) cleanSelectSql.getSelectBody();
        //1.获取所有查询字段
        Set<String> allGraphQLFieldNames = getAllGraphQLFieldNames(dataFetchingEnvironment);
        Set<String> cleanTableAlias = new HashSet<>();
        //2.获取查询字段用到的表和字段
        List<SelectItem> cleanSelectItems = getCleanSelectItems(selectBody, allGraphQLFieldNames);
        Set<String> selectTablesAlias = getCleanSelectTablesAlias(allGraphQLFieldNames, selectBody);
        //3.获取条件字段用到的表
        Set<String> whereTableAlias = addWhereTables(selectBody);
        selectBody.setSelectItems(cleanSelectItems);
        cleanTableAlias.addAll(selectTablesAlias);
        cleanTableAlias.addAll(whereTableAlias);
        //4.清理关联表
        cleanJoins(selectBody, cleanTableAlias);
        return cleanSelectSql.toString();
    }

    /**
     * 获取清理后的select的表
     *
     * @param allGraphQLFieldNames
     * @param plainSelect
     * @return
     */
    private Set<String> getCleanSelectTablesAlias(Set<String> allGraphQLFieldNames, PlainSelect plainSelect) {
        Set<String> selectTableAlias = new HashSet<>();
        selectItemsLoop(plainSelect.getSelectItems(), allGraphQLFieldNames, selectItem -> {
            SelectExpressionItem selectExpressionItem = (SelectExpressionItem) selectItem;
            Column column = (Column) selectExpressionItem.getExpression();
            selectTableAlias.add(getTableAliasName(column.getTable()));
        });
        return selectTableAlias;
    }

    /**
     * 获取清理后的select字段
     *
     * @param plainSelect
     * @param allGraphQLFieldNames
     * @return
     */
    private List<SelectItem> getCleanSelectItems(PlainSelect plainSelect, Set<String> allGraphQLFieldNames) {
        List<SelectItem> cleanSelectItems = new ArrayList<>();
        selectItemsLoop(plainSelect.getSelectItems(), allGraphQLFieldNames, selectItem -> cleanSelectItems.add(selectItem));
        return cleanSelectItems;
    }

    /**
     * 过滤关联表
     *
     * @param selectBody
     * @param cleanTableAlias
     */
    private void cleanJoins(PlainSelect selectBody, Set<String> cleanTableAlias) {
        List<Join> originJoins = selectBody.getJoins();
        Set<Join> cleanNotSortJoins = getExplicitJoins(cleanTableAlias, originJoins);
        Table fromItem = (Table) selectBody.getFromItem();
        addImplicitJoin(cleanTableAlias, originJoins, cleanNotSortJoins, fromItem);
        List<Join> cleanSortedJoins = getCleanSortedJoins(originJoins, cleanNotSortJoins);
        selectBody.setJoins(cleanSortedJoins);
    }

    /**
     * 按原有排序,重新排序join
     * 隐式关联将破坏原有顺序
     *
     * @param originJoins
     * @param cleanJoinsNotSort
     * @returnc
     */
    @NotNull
    private List<Join> getCleanSortedJoins(List<Join> originJoins, Set<Join> cleanJoinsNotSort) {
        List<Join> cleanJoinsSorted = new ArrayList<>(cleanJoinsNotSort.size());
        for (Join originJoin : originJoins) {
            if (cleanJoinsNotSort.contains(originJoin)) {
                cleanJoinsSorted.add(originJoin);
            }
        }
        logger.debug("sorted joins {}", cleanJoinsNotSort);
        return cleanJoinsSorted;
    }

    /**
     * 添加隐式关联,比如中间表
     *
     * @param cleanTableAlias
     * @param originJoins
     * @param cleanJoinsNotSort
     * @param fromItem
     */
    private void addImplicitJoin(Set<String> cleanTableAlias, List<Join> originJoins, Set<Join> cleanJoinsNotSort, Table fromItem) {
        for (Join join : cleanJoinsNotSort) {
            EqualsTo onExpression = (EqualsTo) join.getOnExpression();
            Column leftExpression = (Column) onExpression.getLeftExpression();
            Column rightExpression = (Column) onExpression.getRightExpression();
            String leftTableName = leftExpression.getTable().getName();
            String rightTableName = rightExpression.getTable().getName();
            Table rightItem = (Table) join.getRightItem();
            if (getTableAliasName(rightItem).equals(leftTableName)) {
                addLostJoin(cleanTableAlias, originJoins, cleanJoinsNotSort, fromItem, rightTableName);
            } else {
                addLostJoin(cleanTableAlias, originJoins, cleanJoinsNotSort, fromItem, leftTableName);
            }
        }
    }

    /**
     * 获取显式关联,即根据select 字段或者where条件获得的
     *
     * @param cleanTableAlias
     * @param originJoins
     * @return
     */
    private Set<Join> getExplicitJoins(Set<String> cleanTableAlias, List<Join> originJoins) {
        Set<Join> joins = new HashSet<>();
        for (Join join : originJoins) {
            Table table = (Table) join.getRightItem();
            if (cleanTableAlias.contains(getTableAliasName(table))) {
                logger.debug("add explicit join table {}", join);
                joins.add(join);
            } else {
                logger.debug("table {} removed", table.getName());
            }
        }
        return joins;
    }

    /**
     * @param table
     * @return
     * @see Table#getName()
     * 有时getName有值有时没值,这里统一取别名
     */
    private String getTableAliasName(Table table) {
        if (table.getAlias() != null) {
            return table.getAlias().getName();
        } else {
            return table.getName();
        }
    }

    /**
     * 添加丢失不足的关联表 比如中间表,没查询字段,也没where条件
     *
     * @param cleanTableAlias
     * @param originJoins
     * @param cleanJoinsNotSort
     * @param fromItem
     * @param needTableName
     */
    private void addLostJoin(Set<String> cleanTableAlias, List<Join> originJoins, Set<Join> cleanJoinsNotSort, Table fromItem, String needTableName) {
        if (!cleanTableAlias.contains(needTableName) && !getTableAliasName(fromItem).equals(needTableName)) {
            for (Join originJoin : originJoins) {
                Table rightItem = (Table) originJoin.getRightItem();
                if (getTableAliasName(rightItem).equals(needTableName)) {
                    logger.debug("add lost join table {}", needTableName);
                    cleanJoinsNotSort.add(originJoin);
                }
            }
        }
    }

    /**
     * 添加where中用到的表
     *
     * @param selectBody
     * @return
     */
    private Set<String> addWhereTables(PlainSelect selectBody) {
        Set<String> cleanTableAlias = new HashSet<>();
        //从前台取后台取都行
//            Set<String> allGraphQLArgumentsNames = getAllGraphQLArgumentsNames(dataFetchingEnvironment);
        Expression where = selectBody.getWhere();
        BinaryExpression binaryExpression = (BinaryExpression) where;
        List<Column> expressions = new ArrayList<>();
        nextExpression(binaryExpression, expressions);
        for (Column expression : expressions) {
            logger.debug("where table {}", expression.getTable().toString());
            cleanTableAlias.add(expression.getTable().toString());
        }
        return cleanTableAlias;
    }

    /**
     * 添加select中用到的表,清理不需要的select字段
     * 表意不清废弃
     *
     * @param selectBody
     * @param allGraphQLFieldNames
     * @param cleanTableAlias
     */
    @Deprecated
    private void addSelectTablesAndCleanSelectItems(PlainSelect selectBody, Set<String> allGraphQLFieldNames, Set<String> cleanTableAlias) {
        List<SelectItem> originSelectItems = selectBody.getSelectItems();
        List<SelectItem> cleanSelectItems = new ArrayList<>();
        //注意这里只能取到别名partItems
        for (SelectItem selectItem : originSelectItems) {
            SelectExpressionItem selectExpressionItem = (SelectExpressionItem) selectItem;
            Column column = (Column) selectExpressionItem.getExpression();
            String columnName = column.getColumnName();
            if (allGraphQLFieldNames.contains(columnName)) {
                cleanSelectItems.add(selectItem);
                cleanTableAlias.add(getTableAliasName(column.getTable()));
            } else {
                logger.debug("column {} removed", column.getColumnName());
            }
        }
        logger.debug("origin select items -> {}", cleanSelectItems);
        selectBody.setSelectItems(cleanSelectItems);
    }

    private void selectItemsLoop(List<SelectItem> originSelectItems, Set<String> allGraphQLFieldNames, Consumer<SelectItem> selectItemConsumer) {
        //注意这里只能取到别名partItems
        for (SelectItem selectItem : originSelectItems) {
            SelectExpressionItem selectExpressionItem = (SelectExpressionItem) selectItem;
            Column column = (Column) selectExpressionItem.getExpression();
            String columnName = column.getColumnName();
            if (allGraphQLFieldNames.contains(columnName)) {
                selectItemConsumer.accept(selectItem);
            } else {
                logger.debug("column {} removed", column.getColumnName());
            }
        }
    }

    /**
     * 递归获取所有where条件
     *
     * @param expression
     * @param columns
     */
    private void nextExpression(BinaryExpression expression, List<Column> columns) {
        if (expression == null) {
            return;
        }
        Expression leftExpression = (expression).getLeftExpression();
        if (leftExpression != null && leftExpression instanceof BinaryExpression) {
            nextExpression((BinaryExpression) leftExpression, columns);
        }
        if (leftExpression != null && leftExpression instanceof Column) {
            columns.add((Column) leftExpression);
        }
        Expression rightExpression = expression.getRightExpression();
        if (rightExpression != null && rightExpression instanceof BinaryExpression) {
            nextExpression((BinaryExpression) rightExpression, columns);
        }
        if (rightExpression != null && rightExpression instanceof Column) {
            columns.add((Column) rightExpression);
        }
    }

    /**
     * 获取GraphQL传入的参数名
     *
     * @param dataFetchingEnvironment
     * @return
     */
    private Set<String> getAllGraphQLArgumentsNames(DataFetchingEnvironment dataFetchingEnvironment) {
        Set<String> allGraphQLParamNames = null;
        Map<String, Object> arguments = dataFetchingEnvironment.getArguments();
        if (arguments != null) {
            allGraphQLParamNames = arguments.keySet()
                    .stream()
                    .map(s -> CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE).convert(s))
                    .collect(Collectors.toSet());
        }
        return allGraphQLParamNames;
    }

    /**
     * 获取GraphQL查询的字段名
     *
     * @param dataFetchingEnvironment
     * @return
     */
    private Set<String> getAllGraphQLFieldNames(DataFetchingEnvironment dataFetchingEnvironment) {
        Set<String> fieldNames = null;
        List<Field> fields = dataFetchingEnvironment.getFields();
        if (fields != null) {
            fieldNames = new HashSet<>();
            for (Field field : fields) {
                getAllGraphQLFieldNames(fieldNames, field);
            }
            if (fieldNames != null) {
                fieldNames = fieldNames
                        .stream()
                        .map(s -> CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE).convert(s))
                        .collect(Collectors.toSet());
                logger.debug("all graphQL field names {}", fieldNames);
            }
        }
        if (fieldNames == null || fieldNames.isEmpty()) {
            logger.debug("all graphQL field names is empty");
        }
        return fieldNames;
    }

    /**
     * 递归获取GraphQL所有查询的字段名
     *
     * @param fieldNames
     * @param field
     */
    private void getAllGraphQLFieldNames(Set<String> fieldNames, Field field) {
        if (field != null) {
            SelectionSet selectionSet = field.getSelectionSet();
            if (selectionSet != null) {
                List<Selection> selections = selectionSet.getSelections();
                if (selections != null) {
                    for (Selection selection : selections) {
                        Field subField = (Field) selection;
                        fieldNames.add(subField.getName());
                        getAllGraphQLFieldNames(fieldNames, subField);
                    }
                }
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        logger.debug("properties {}", properties);
    }

}
