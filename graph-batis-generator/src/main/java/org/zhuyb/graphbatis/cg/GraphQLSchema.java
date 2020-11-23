package org.zhuyb.graphbatis.cg;

import graphql.schema.GraphQLScalarType;
import lombok.Data;

import java.util.List;

/**
 * @author zhuyb
 * graphQL数据结构
 */
@Data
public class GraphQLSchema {
    private String name;
    private List<GraphQLSchemaField> fields;

    @Data
    public static class GraphQLSchemaField {
        private String name;
        private GraphQLScalarType type;
    }
}

