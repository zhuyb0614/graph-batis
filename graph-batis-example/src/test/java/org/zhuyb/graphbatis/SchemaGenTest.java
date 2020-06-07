package org.zhuyb.graphbatis;

import graphql.Scalars;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLScalarType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static graphql.Scalars.GraphQLBoolean;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;

/**
 * @author zhuyb
 * @date 2020/6/1 22:24
 * 测试生成schema.graphqls
 */
public class SchemaGenTest {



    @Test
    public void gen() {
        GraphQLObjectType aObj = newObject()
                .name("aObj")
                .description("A Simpson character")
                .field(newFieldDefinition()
                        .name("name")
                        .description("The name of the character.")
                        .type(GraphQLString))
                .build();

        GraphQLObjectType simpsonCharacter = newObject()
                .name("SimpsonCharacter")
                .description("A Simpson character")
                .field(newFieldDefinition()
                        .name("name")
                        .description("The name of the character.")
                        .type(GraphQLString))
                .field(newFieldDefinition()
                        .name("mainCharacter")
                        .description("One of the main Simpson characters?")
                        .type(GraphQLBoolean))
                .field(newFieldDefinition()
                        .name("OtherObject")
                        .description("other object type")
                        .type(aObj)
                )
                .build();
        System.out.println(simpsonCharacter.toString());
    }
}
