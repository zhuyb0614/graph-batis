package org.zhuyb.graphbatis.cleaner;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.zhuyb.graphbatis.entity.FetchingData;
import org.zhuyb.graphbatis.entity.Tables;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
public class MybatisSqlCleanerImpl implements SqlCleaner {
    public static final String CACHE_KEY_DELIMITER = "&";
    public static final String D_S = "%d:%s";
    private Integer maxLoopDeep = -1;
    private Integer maxCacheSize = 1024;
    private Map<String, String> cache;

    @Override
    public String cleanSql(FetchingData fetchingData, String originSql) {
        try {
            return getCleanSql(fetchingData, originSql, 0);
        } catch (JSQLParserException e) {
            log.error("parse sql error [==> {}]", originSql, e);
            return originSql;
        }
    }

    @Override
    public void setUp(Properties properties) {
        String maxLoopDeepProp = properties.getProperty("maxLoopDeep");
        String maxCacheSize = properties.getProperty("maxCacheSize");
        if (StringUtils.isNumeric(maxLoopDeepProp)) {
            this.maxLoopDeep = Integer.valueOf(maxLoopDeepProp);
        }
        if (StringUtils.isNumeric(maxCacheSize)) {
            this.maxCacheSize = Integer.valueOf(maxCacheSize);
        }
        cache = new ConcurrentHashMap((int) (this.maxCacheSize / 0.75 + 1));
    }

    private String getCleanSql(FetchingData fetchingData, String originSQL, int loopTimes) throws JSQLParserException {
        return getCleanSql(fetchingData, originSQL, null, loopTimes);
    }

    /**
     * 获取过滤后的SQL
     *
     * @param fetchingData
     * @param originSQL
     * @return
     * @throws JSQLParserException
     */
    private String getCleanSql(FetchingData fetchingData, String originSQL, String cacheKey, int loopTimes) throws JSQLParserException {
        //获取所有查询字段
        Set<String> allGraphQLFieldNames = fetchingData.getFieldNames();
        if (loopTimes == 0) {
            if (cacheKey == null) {
                cacheKey = String.format(D_S, originSQL.hashCode(), allGraphQLFieldNames.stream().sorted().collect(Collectors.joining(CACHE_KEY_DELIMITER)));
            }
            String cacheSQL = cache.get(cacheKey);
            if (cacheSQL != null) {
                return cacheSQL;
            }
        } else if (maxLoopDeep != -1 && loopTimes > maxLoopDeep) {
            log.warn("loop times {} break", loopTimes);
            return originSQL;
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
            log.debug("loop times {} clean sql ==> {}", loopTimes + 1, cleanSql);
            return getCleanSql(fetchingData, cleanSql, cacheKey, loopTimes + 1);
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
        log.debug("sorted joins {}", cleanJoinsNotSort);
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
        log.debug("add implicit joins {}", implicitJoins);
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
        log.debug("add explicit joins {}", explicitJoins);
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
            log.debug("where table {}", expression.getTable().toString());
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
                log.debug("column {} removed", column.getColumnName());
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

}
