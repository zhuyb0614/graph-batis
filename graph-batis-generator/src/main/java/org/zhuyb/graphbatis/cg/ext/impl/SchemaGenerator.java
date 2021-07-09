package org.zhuyb.graphbatis.cg.ext.impl;

import freemarker.template.Template;
import graphql.Scalars;
import graphql.schema.GraphQLScalarType;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.zhuyb.graphbatis.cg.GraphQLSchema;
import org.zhuyb.graphbatis.cg.ext.ExtGenerator;
import org.zhuyb.graphbatis.cg.ext.FreemarkerExtGenerator;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @author zhuyb
 */
@Slf4j
public class SchemaGenerator extends FreemarkerExtGenerator implements ExtGenerator {
    public static final Map<Class<?>, GraphQLScalarType> CLASS_SCALAR_TYPE_MAP = new HashMap<>();

    static {
        CLASS_SCALAR_TYPE_MAP.put(String.class, Scalars.GraphQLString);
        CLASS_SCALAR_TYPE_MAP.put(Integer.class, Scalars.GraphQLInt);
        CLASS_SCALAR_TYPE_MAP.put(Long.class, Scalars.GraphQLLong);
        CLASS_SCALAR_TYPE_MAP.put(BigInteger.class, Scalars.GraphQLBigInteger);
        CLASS_SCALAR_TYPE_MAP.put(BigDecimal.class, Scalars.GraphQLBigDecimal);
        CLASS_SCALAR_TYPE_MAP.put(Boolean.class, Scalars.GraphQLBoolean);
        CLASS_SCALAR_TYPE_MAP.put(Short.class, Scalars.GraphQLShort);
        CLASS_SCALAR_TYPE_MAP.put(Float.class, Scalars.GraphQLFloat);
        CLASS_SCALAR_TYPE_MAP.put(Byte.class, Scalars.GraphQLByte);
        CLASS_SCALAR_TYPE_MAP.put(Character.class, Scalars.GraphQLChar);
    }

    String schemaTemplateFileName = "schama.ftl";
    String schemaOutputPath = new File("./src/main/resources/schema/").getAbsolutePath();

    @Override
    public void generate(IntrospectedTable introspectedTable) {
        graphQLSchemaGenerated(introspectedTable);
    }

    @Override
    public void setUp(Properties properties) {

    }

    private void graphQLSchemaGenerated(IntrospectedTable introspectedTable) {
        Template template;
        try {
            template = getTemplate(schemaTemplateFileName);
        } catch (IOException e) {
            log.error("load template error", e);
            return;
        }
        GraphQLSchema graphQLSchema = getGraphQLSchema(introspectedTable);
        String fileName = schemaOutputPath + File.separator + getObjectName(introspectedTable) + ".graphql";
        // 定义输出
        Map<String, Object> data = new HashMap<>();
        data.put("gqs", graphQLSchema);
        writFile(template, data, fileName);
    }

    @NotNull
    private GraphQLSchema getGraphQLSchema(IntrospectedTable introspectedTable) {
        // 定义数据
        GraphQLSchema graphQLSchema = new GraphQLSchema();
        graphQLSchema.setName(getObjectName(introspectedTable));
        ArrayList<GraphQLSchema.GraphQLSchemaField> fields = new ArrayList<>();
        List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
        for (IntrospectedColumn column : allColumns) {
            addColumn(fields, column);
        }
        graphQLSchema.setFields(fields);
        return graphQLSchema;
    }

    private void addColumn(ArrayList<GraphQLSchema.GraphQLSchemaField> fields, IntrospectedColumn column) {
        GraphQLSchema.GraphQLSchemaField schemaField = new GraphQLSchema.GraphQLSchemaField();
        String fullyQualifiedName = column.getFullyQualifiedJavaType().getFullyQualifiedName();
        try {
            Class<?> javaType = Class.forName(fullyQualifiedName);
            schemaField.setType(CLASS_SCALAR_TYPE_MAP.getOrDefault(javaType, Scalars.GraphQLString));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        schemaField.setName(column.getJavaProperty());
        fields.add(schemaField);
    }

}
