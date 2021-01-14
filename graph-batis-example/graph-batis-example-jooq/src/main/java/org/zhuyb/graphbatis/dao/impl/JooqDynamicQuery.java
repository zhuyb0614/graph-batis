package org.zhuyb.graphbatis.dao.impl;

import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.zhuyb.graphbatis.FetchingDataHolder;
import org.zhuyb.graphbatis.dao.TableJoinCondition;
import org.zhuyb.graphbatis.entity.FetchingData;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author zhuyb
 */

public class JooqDynamicQuery<T> {

    private SelectFieldOrAsterisk[] selectFields;
    private TableJoinCondition[] tableJoinConditions;
    private Class<T> fetchInto;
    private DSLContext create;
    private TableLike<?> defaultFromTable;

    public JooqDynamicQuery(SelectFieldOrAsterisk[] selectFields, TableJoinCondition[] tableJoinConditions, Class<T> fetchInto, DSLContext create, TableLike<?> defaultFromTable) {
        this.selectFields = selectFields;
        this.tableJoinConditions = tableJoinConditions;
        this.fetchInto = fetchInto;
        this.create = create;
        this.defaultFromTable = defaultFromTable;
    }

    /**
     * @param arguments
     * @return
     */
    public List<T> findAll(Map<String, Object> arguments) {
        FetchingData fetchingData = FetchingDataHolder.get();
        Set<String> fieldNames = fetchingData.getFieldNames();
        List<SelectFieldOrAsterisk> selectFields = getSelectFields(fieldNames, this.selectFields);
        Set<TableLike<?>> queryTables = getQueryTables(fieldNames, this.selectFields);
        if (queryTables.size() == 1) {
            return create.select(selectFields).from(queryTables.stream().findFirst().get()).fetchInto(fetchInto);
        } else {
            TableLike<?> fromTable = getFromTable(queryTables, tableJoinConditions);
            SelectJoinStep<Record> selectJoinStep = create.select(selectFields).from(fromTable);
            finalQueryTables(queryTables, tableJoinConditions, fromTable);
            for (TableJoinCondition tableJoinCondition : tableJoinConditions) {
                if (queryTables.contains(tableJoinCondition.getTable())) {
                    JoinType joinType = tableJoinCondition.getJoinType();
                    switch (joinType) {
                        case JOIN:
                            selectJoinStep.join(tableJoinCondition.getTable()).on(tableJoinCondition.getCondition());
                            break;
                        default:
                            throw new IllegalArgumentException(String.format("unsupported join type: %s", joinType.name()));
                    }
                }
            }
            return selectJoinStep.fetchInto(fetchInto);
        }
    }

    @NotNull
    protected Set<TableLike<?>> getQueryTables(Set<String> fieldNames, SelectFieldOrAsterisk[] allSelectFieldOrAsterisks) {
        Set<TableLike<?>> queryTables = new HashSet<>();
        filterQueryFieldOrAsterisks(fieldNames, allSelectFieldOrAsterisks, tableField -> {
            Table table = tableField.getTable();
            queryTables.add(table);
        });
        return queryTables;
    }

    @NotNull
    protected List<SelectFieldOrAsterisk> getSelectFields(Set<String> fieldNames, SelectFieldOrAsterisk[] allSelectFieldOrAsterisks) {
        List<SelectFieldOrAsterisk> selectFields = new ArrayList<>(fieldNames.size());
        filterQueryFieldOrAsterisks(fieldNames, allSelectFieldOrAsterisks, tableField -> {
            selectFields.add(tableField);
        });
        return selectFields;
    }

    @NotNull
    protected Set<TableLike<?>> finalQueryTables(Set<TableLike<?>> tables, TableJoinCondition[] tableJoinConditions, TableLike<?> fromTable) {
        tables.add(fromTable);

        Map<? extends TableLike<?>, TableJoinCondition> tableJoinMap = Arrays.stream(tableJoinConditions).collect(Collectors.toMap(TableJoinCondition::getTable, tableJoinCondition -> tableJoinCondition));
        for (TableJoinCondition tableJoinCondition : tableJoinConditions) {
            if (tables.contains(tableJoinCondition.getTable())) {
                tables.add(tableJoinCondition.getTable());
                addRelTables(tables, tableJoinMap, tableJoinCondition);
            }
        }
        return tables;
    }

    private void addRelTables(Set<TableLike<?>> allTables, Map<? extends TableLike<?>, TableJoinCondition> tableJoinMap, TableJoinCondition tableJoinCondition) {
        if (!allTables.contains(tableJoinCondition.getRelationTable())) {
            allTables.add(tableJoinCondition.getRelationTable());
            TableJoinCondition nextRelationTable = tableJoinMap.get(tableJoinCondition.getRelationTable());
            if (nextRelationTable != null) {
                addRelTables(allTables, tableJoinMap, nextRelationTable);
            }
        }
    }

    private void filterQueryFieldOrAsterisks(Set<String> fieldNames, SelectFieldOrAsterisk[] allSelectFieldOrAsterisks, Consumer<TableField> consumer) {
        for (SelectFieldOrAsterisk fieldOrAsterisk : allSelectFieldOrAsterisks) {
            if (fieldOrAsterisk instanceof TableField) {
                TableField tableField = (TableField) fieldOrAsterisk;
                if (fieldNames.contains(tableField.getName())) {
                    consumer.accept(tableField);
                }
            }
        }
    }

    protected TableLike<?> getFromTable(Set<TableLike<?>> tables, TableJoinCondition[] tableJoinConditions) {
        TableLike<?> fromTable = defaultFromTable;
        if (!tables.contains(fromTable)) {
            fromTable = tableJoinConditions[0].getTable();
        }
        return fromTable;
    }


}
