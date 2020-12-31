package org.zhuyb.graphbatis.util;

import com.google.common.base.CaseFormat;
import graphql.language.Field;
import graphql.language.Selection;
import graphql.language.SelectionSet;
import graphql.schema.DataFetchingEnvironment;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
public class GraphQLTransformUtil {
    /**
     * 获取GraphQL查询的字段名
     *
     * @param dataFetchingEnvironment
     * @return
     */
    public static Set<String> getAllGraphQLFieldNames(DataFetchingEnvironment dataFetchingEnvironment) {
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
                log.debug("all graphQL field names {}", fieldNames);
            }
        }
        if (fieldNames == null || fieldNames.isEmpty()) {
            log.debug("all graphQL field names is empty");
        }
        return fieldNames;
    }

    /**
     * 获取GraphQL传入的参数名
     *
     * @param dataFetchingEnvironment
     * @return
     */
    private static Set<String> getAllGraphQLArgumentsNames(DataFetchingEnvironment dataFetchingEnvironment) {
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
     * 递归获取GraphQL所有查询的字段名
     *
     * @param fieldNames
     * @param field
     */
    private static void getAllGraphQLFieldNames(Set<String> fieldNames, Field field) {
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
}
