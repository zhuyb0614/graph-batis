package org.zhuyb.graphbatis.interceptor;

import com.google.common.base.CaseFormat;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
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
import org.apache.commons.lang3.StringUtils;
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
import org.zhuyb.graphbatis.entity.Tables;

import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author zhuyb
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
    public static final int DEFAULT_MAX_LOOP_DEEP = -1;
    public static final int DEFAULT_MAX_CACHE_SIZE = 1024;
    public static final String CACHE_KEY_DELIMITER = "&";
    public static final String D_S = "%d:%s";
    private static Cache<String, String> cache;

    /**
     * 循环剔出层数,默认无限
     */
    private Integer maxLoopDeep = DEFAULT_MAX_LOOP_DEEP;
    private Integer maxCacheSize = DEFAULT_MAX_CACHE_SIZE;

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
                String cleanSql = getCleanSql(dataFetchingEnvironment, originSql);
                BoundSql cleanBoundSql = new BoundSql(mappedStatement.getConfiguration(), cleanSql, originBoundSql.getParameterMappings(), originBoundSql.getParameterObject());
                args[BOUND_SQL_INDEX] = cleanBoundSql;
                logger.debug("clean sql cost {}ms", System.currentTimeMillis() - startTime);
                result = changedInvocation.proceed();
            } else {
                result = invocation.proceed();
            }
        }
        return result;
    }

    private String getCleanSql(DataFetchingEnvironment dataFetchingEnvironment, String originSql) throws JSQLParserException {
        return getCleanSql(dataFetchingEnvironment, originSql, 0);
    }

    private String getCleanSql(DataFetchingEnvironment dataFetchingEnvironment, String originSQL, int loopTimes) throws JSQLParserException {
        return getCleanSql(dataFetchingEnvironment, originSQL, null, 0);
    }

    /**
     * 获取过滤后的SQL
     *
     * @param dataFetchingEnvironment
     * @param originSQL
     * @return
     * @throws JSQLParserException
     */
    private String getCleanSql(DataFetchingEnvironment dataFetchingEnvironment, String originSQL, String cacheKey, int loopTimes) throws JSQLParserException {
        if (maxLoopDeep != DEFAULT_MAX_LOOP_DEEP && loopTimes > maxLoopDeep) {
            return originSQL;
        }
        //获取所有查询字段
        Set<String> allGraphQLFieldNames = getAllGraphQLFieldNames(dataFetchingEnvironment);
        if (cacheKey == null) {
            cacheKey = String.format(D_S, originSQL.hashCode(), allGraphQLFieldNames.stream().sorted().collect(Collectors.joining(CACHE_KEY_DELIMITER)));
        }
        String cacheSQL = cache.getIfPresent(cacheKey);
        if (cacheSQL != null) {
            return cacheSQL;
        }
        Select cleanSelectSql = (Select) CCJSqlParserUtil.parse(originSQL);
        PlainSelect selectBody = (PlainSelect) cleanSelectSql.getSelectBody();
        //获取查询字段用到的字段
        selectBody.setSelectItems(getCleanSelectItems(selectBody, allGraphQLFieldNames));

        Set<String> cleanTableAlias = new HashSet<>();
        //获取查询字段用到的表
        Set<String> cleanSelectTablesAlias = getCleanSelectTablesAlias(selectBody, allGraphQLFieldNames);
        cleanTableAlias.addAll(cleanSelectTablesAlias);
        //获取条件字段用到的表
        cleanTableAlias.addAll(getCleanWhereTables(selectBody));
        //清理关联表
        Tables tables = getCleanTables(selectBody, cleanTableAlias, cleanSelectTablesAlias);
        selectBody.setFromItem(tables.getFromItem());
        List<Join> joins = tables.getJoins();
        selectBody.setJoins(joins);
        String cleanSql = selectBody.toString();
        if (joins == null || joins.isEmpty() || originSQL.equals(cleanSql)) {
            cache.put(cacheKey, cleanSql);
            return cleanSql;
        } else {
            logger.debug("loop times {} clean sql ==> {}", loopTimes + 1, cleanSql);
            return getCleanSql(dataFetchingEnvironment, cleanSql, cacheKey, loopTimes + 1);
        }
    }

    private Tables getCleanTables(PlainSelect selectBody, Set<String> cleanTableAlias, Set<String> cleanSelectTablesAlias) {
        List<Join> originJoins = selectBody.getJoins();
        Table oldFromItem = (Table) selectBody.getFromItem();
        Set<Join> explicitJoins = getExplicitJoins(cleanTableAlias, originJoins);
        FromItem newFromItem = oldFromItem;
        List<Join> newJoins;
        if (explicitJoins.isEmpty()) {
            newJoins = Collections.emptyList();
        } else {
            Set<Join> cleanDisorderJoins = new HashSet<>();
            cleanDisorderJoins.addAll(explicitJoins);
            cleanDisorderJoins.addAll(getImplicitJoin(cleanTableAlias, originJoins, explicitJoins, oldFromItem));
            List<Join> cleanOrderedJoins = getCleanOrderedJoins(originJoins, cleanDisorderJoins);
            boolean isNeedFromTable = isNeedFromItem(explicitJoins, oldFromItem, cleanTableAlias);
            //不需要主表,从join表里找出一个替换主表
            if (!isNeedFromTable && cleanOrderedJoins.size() > 0) {
                int cleanOrderedJoinsSize = cleanOrderedJoins.size();
                //有两个关联表,但是查询的字段只是一张表里的  因为join表后边的on条件会增加一条附属表
                if (cleanOrderedJoinsSize == 2 && cleanSelectTablesAlias.size() == 1) {
                    FromItem result = oldFromItem;
                    for (Join cleanSortedJoin : cleanOrderedJoins) {
                        FromItem joinFromItem = cleanSortedJoin.getRightItem();
                        if (joinFromItem.getAlias().getName().equals(cleanSelectTablesAlias.stream().findFirst().get())) {
                            result = joinFromItem;
                            break;
                        }
                    }
                    newFromItem = result;
                    newJoins = Collections.emptyList();
                } else {
                    newFromItem = cleanOrderedJoins.get(0).getRightItem();
                    if (cleanOrderedJoinsSize <= 1) {
                        newJoins = Collections.emptyList();
                    } else {
                        newJoins = cleanOrderedJoins.subList(1, cleanOrderedJoinsSize);
                    }
                }
            } else {
                newJoins = cleanOrderedJoins;
            }
        }
        return new Tables(newFromItem, newJoins);
    }

    /**
     * 获取清理后的select的表
     *
     * @param plainSelect
     * @param allGraphQLFieldNames
     * @return
     */
    private Set<String> getCleanSelectTablesAlias(PlainSelect plainSelect, Set<String> allGraphQLFieldNames) {
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
     * 按原有排序,重新排序join
     * 隐式关联将破坏原有顺序
     *
     * @param originJoins
     * @param cleanJoinsNotSort
     * @returnc
     */
    @NotNull
    private List<Join> getCleanOrderedJoins(List<Join> originJoins, Set<Join> cleanJoinsNotSort) {
        List<Join> cleanJoinsSorted = new ArrayList<>(cleanJoinsNotSort.size());
        if (originJoins != null) {
            for (Join originJoin : originJoins) {
                if (cleanJoinsNotSort.contains(originJoin)) {
                    cleanJoinsSorted.add(originJoin);
                }
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
     * @return
     */
    private List<Join> getImplicitJoin(Set<String> cleanTableAlias, List<Join> originJoins, Set<Join> cleanJoinsNotSort, Table fromItem) {
        List<Join> implicitJoins = new ArrayList<>();
        for (Join join : cleanJoinsNotSort) {
            EqualsTo onExpression = (EqualsTo) join.getOnExpression();
            Column leftExpression = (Column) onExpression.getLeftExpression();
            Column rightExpression = (Column) onExpression.getRightExpression();
            String leftTableName = leftExpression.getTable().getName();
            String rightTableName = rightExpression.getTable().getName();
            Table rightItem = (Table) join.getRightItem();
            Join lostJoin;
            if (getTableAliasName(rightItem).equals(leftTableName)) {
                lostJoin = getLostJoin(cleanTableAlias, originJoins, fromItem, rightTableName);
            } else {
                lostJoin = getLostJoin(cleanTableAlias, originJoins, fromItem, leftTableName);
            }
            if (lostJoin != null) {
                implicitJoins.add(lostJoin);
            }
        }
        logger.debug("add implicit joins {}", implicitJoins);
        return implicitJoins;
    }

    private boolean isNeedFromItem(Set<Join> cleanJoinsNotSort, Table fromItem, Set<String> cleanTableAlias) {
        boolean needFromTable = false;
        String fromTableName = fromItem.getAlias().getName();
        if (cleanJoinsNotSort.size() != 1 || cleanTableAlias.contains(fromTableName)) {
            if (!cleanTableAlias.contains(fromTableName)) {
                for (Join join : cleanJoinsNotSort) {
                    EqualsTo onExpression = (EqualsTo) join.getOnExpression();
                    Column leftExpression = (Column) onExpression.getLeftExpression();
                    Column rightExpression = (Column) onExpression.getRightExpression();
                    String leftTableName = leftExpression.getTable().getName();
                    String rightTableName = rightExpression.getTable().getName();
                    //只要是所有表,有关联到了主表
                    if (fromTableName.equals(rightTableName) || fromTableName.equals(leftTableName)) {
                        needFromTable = true;
                        break;
                    }
                }
            } else {
                needFromTable = true;
            }
        }
        return needFromTable;
    }

    /**
     * 获取显式关联,即根据select 字段或者where条件获得的
     *
     * @param cleanTableAlias
     * @param originJoins
     * @return
     */
    private Set<Join> getExplicitJoins(Set<String> cleanTableAlias, List<Join> originJoins) {
        Set<Join> explicitJoins = new HashSet<>();
        if (originJoins != null) {
            for (Join join : originJoins) {
                Table table = (Table) join.getRightItem();
                if (cleanTableAlias.contains(getTableAliasName(table))) {
                    explicitJoins.add(join);
                }
            }
        }
        logger.debug("add explicit joins {}", explicitJoins);
        return explicitJoins;
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
     * @param fromItem
     * @param needTableName
     * @return
     */
    private Join getLostJoin(Set<String> cleanTableAlias, List<Join> originJoins, Table fromItem, String needTableName) {
        if (!cleanTableAlias.contains(needTableName) && !getTableAliasName(fromItem).equals(needTableName)) {
            for (Join originJoin : originJoins) {
                Table rightItem = (Table) originJoin.getRightItem();
                if (getTableAliasName(rightItem).equals(needTableName)) {
                    return originJoin;
                }
            }
        }
        return null;
    }

    /**
     * 添加where中用到的表
     *
     * @param selectBody
     * @return
     */
    private Set<String> getCleanWhereTables(PlainSelect selectBody) {
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
        String maxLoopDeepProp = properties.getProperty("maxLoopDeep");
        if (StringUtils.isNumeric(maxLoopDeepProp)) {
            maxLoopDeep = Integer.valueOf(maxLoopDeepProp);
        }
        logger.info("properties {}", properties);
        cache = CacheBuilder
                .newBuilder()
                .expireAfterWrite(Duration.ofMinutes(1))
                .maximumSize(1024)
                .build();
    }

}
