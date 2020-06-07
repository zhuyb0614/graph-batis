package org.zhuyb.graphbatis.cg;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import graphql.Scalars;
import graphql.schema.GraphQLScalarType;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.TableConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
public class GraphBatisPlugin extends PluginAdapter {
    public static final Pattern DEFAULT_PRIMARY_KEY_PATTERN = Pattern.compile("PrimaryKey");
    public static final Pattern DEFAULT_MAPPER_SUFFIX_PATTERN = Pattern.compile("Mapper$");
    public static final Pattern DEFAULT_MAPPER_XML_SUFFIX_PATTERN = Pattern.compile("Mapper.xml$");
    public static final Map<Class<?>, GraphQLScalarType> CLASS_SCALAR_TYPE_MAP = new HashMap<>();
    public static final String MAPPER_SUFFIX = "mapperSuffix";
    public static final String PRIMARY_KEY_NAME = "primaryKeyName";
    public static final String TEMPLATE_DIR_PATH = "templateDirPath";

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

    private String mapperSuffix;
    private String primaryKeyName;
    String templateDirPath = FTL_DIR_PATH;
    String schemaTemplatePath = SCHEMA_TEMPLATE_PATH;
    String schemaOutputPath = OUT_PATH;
    public static final String OUT_PATH = "/data/graphql/schema";
    public static final String FTL_DIR_PATH = OUT_PATH + "/ftl";
    public static final String SCHEMA_TEMPLATE_PATH = "schama.ftl";

    @Override
    public boolean validate(List<String> warnings) {
        String mapperSuffix = properties.getProperty(MAPPER_SUFFIX);
        String primaryKeyName = properties.getProperty(PRIMARY_KEY_NAME);
        if (tooLong(mapperSuffix) || tooLong(primaryKeyName)) {
            return false;
        }
        this.mapperSuffix = mapperSuffix; //$NON-NLS-1$
        this.primaryKeyName = primaryKeyName; //$NON-NLS-1$
        return true;
    }

    private boolean tooLong(String str) {
        if (str != null && str.length() > 10) {
            log.error("str {} too long,max length 10", str);
            return true;
        }
        return false;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        renameMapper(introspectedTable);
        renamePrimaryKey(introspectedTable);
    }

    /**
     * 更改主键方法名
     *
     * @param introspectedTable
     */
    private void renamePrimaryKey(IntrospectedTable introspectedTable) {
        if (primaryKeyName != null) {
            introspectedTable.setUpdateByPrimaryKeyStatementId(DEFAULT_PRIMARY_KEY_PATTERN.matcher(introspectedTable.getUpdateByPrimaryKeyStatementId()).replaceAll(primaryKeyName));
            introspectedTable.setSelectByPrimaryKeyStatementId(DEFAULT_PRIMARY_KEY_PATTERN.matcher(introspectedTable.getSelectByPrimaryKeyStatementId()).replaceAll(primaryKeyName));
            introspectedTable.setUpdateByPrimaryKeySelectiveStatementId(DEFAULT_PRIMARY_KEY_PATTERN.matcher(introspectedTable.getUpdateByPrimaryKeySelectiveStatementId()).replaceAll(primaryKeyName));
            introspectedTable.setDeleteByPrimaryKeyStatementId(DEFAULT_PRIMARY_KEY_PATTERN.matcher(introspectedTable.getDeleteByPrimaryKeyStatementId()).replaceAll(primaryKeyName));
        }
    }

    /**
     * 更改Mapper文件名
     *
     * @param introspectedTable
     */
    private void renameMapper(IntrospectedTable introspectedTable) {
        if (mapperSuffix != null) {
            introspectedTable.setMyBatis3JavaMapperType(DEFAULT_MAPPER_SUFFIX_PATTERN.matcher(introspectedTable.getMyBatis3JavaMapperType()).replaceAll(mapperSuffix));
            introspectedTable.setMyBatis3XmlMapperFileName(DEFAULT_MAPPER_XML_SUFFIX_PATTERN.matcher(introspectedTable.getMyBatis3XmlMapperFileName()).replaceAll(mapperSuffix + ".xml"));
        }
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {
        //解决有泛型时 继承全类名问题
        topLevelClass.getSuperClass().ifPresent(fullyQualifiedJavaType -> {
                    topLevelClass.setSuperClass(fullyQualifiedJavaType.getShortName());
                }
        );
        graphQLSchemaGenerated(introspectedTable);
        return true;
    }

    private void graphQLSchemaGenerated(IntrospectedTable introspectedTable) {
        Template template = getTemplate();
        GraphQLSchema graphQLSchema = getGraphQLSchema(introspectedTable);
        writeSchema(introspectedTable, template, graphQLSchema);
    }

    private void writeSchema(IntrospectedTable introspectedTable, Template template, GraphQLSchema graphQLSchema) {
        // 定义输出
        Writer out = null;
        String fileName = schemaOutputPath + File.separator + getObjectName(introspectedTable) + ".graphql";
        try {
            out = new FileWriter(fileName);
            template.process(graphQLSchema, out);
        } catch (IOException | TemplateException e) {
            log.error("write {} error", fileName, e);
            return;
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    log.error("close output error", e);
                }
            }
        }
    }

    @Nullable
    private Template getTemplate() {
        Template template = null;
        Configuration conf = new Configuration(Configuration.VERSION_2_3_30);
        //加载模板文件(模板的路径)
        try {
            conf.setDirectoryForTemplateLoading(new File(templateDirPath));
        } catch (IOException e) {
            log.error("load template dir {} error", templateDirPath, e);
            return template;
        }
        // 加载模板
        try {
            template = conf.getTemplate(schemaTemplatePath);
        } catch (IOException e) {
            log.error("load schema template file {} error", schemaTemplatePath, e);
            return template;
        }
        return template;
    }

    private String getObjectName(IntrospectedTable introspectedTable) {
        TableConfiguration tableConfiguration = introspectedTable.getTableConfiguration();
        return tableConfiguration.getDomainObjectName();
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
            schemaField.setType(CLASS_SCALAR_TYPE_MAP.get(javaType));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        schemaField.setName(column.getJavaProperty());
        fields.add(schemaField);
    }
}
