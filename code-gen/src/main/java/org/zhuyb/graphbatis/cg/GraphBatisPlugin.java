package org.zhuyb.graphbatis.cg;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;
import java.util.regex.Pattern;

public class GraphBatisPlugin extends PluginAdapter {
    public static final Pattern DEFAULT_PRIMARY_KEY_PATTERN = Pattern.compile("PrimaryKey");
    public static final Pattern DEFAULT_MAPPER_SUFFIX_PATTERN = Pattern.compile("Mapper$");
    public static final Pattern DEFAULT_MAPPER_XML_SUFFIX_PATTERN = Pattern.compile("Mapper.xml$");
    private String mapperSuffix;
    private String primaryKeyName;


    @Override
    public boolean validate(List<String> warnings) {
        mapperSuffix = properties.getProperty("mapperSuffix"); //$NON-NLS-1$
        primaryKeyName = properties.getProperty("primaryKeyName"); //$NON-NLS-1$
        return true;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        //更改Mapper文件名
        if (mapperSuffix != null) {
            introspectedTable.setMyBatis3JavaMapperType(DEFAULT_MAPPER_SUFFIX_PATTERN.matcher(introspectedTable.getMyBatis3JavaMapperType()).replaceAll(mapperSuffix));
            introspectedTable.setMyBatis3XmlMapperFileName(DEFAULT_MAPPER_XML_SUFFIX_PATTERN.matcher(introspectedTable.getMyBatis3XmlMapperFileName()).replaceAll(mapperSuffix + ".xml"));
        }
        //更改主键方法名
        if (primaryKeyName != null) {
            introspectedTable.setUpdateByPrimaryKeyStatementId(DEFAULT_PRIMARY_KEY_PATTERN.matcher(introspectedTable.getUpdateByPrimaryKeyStatementId()).replaceAll(primaryKeyName));
            introspectedTable.setSelectByPrimaryKeyStatementId(DEFAULT_PRIMARY_KEY_PATTERN.matcher(introspectedTable.getSelectByPrimaryKeyStatementId()).replaceAll(primaryKeyName));
            introspectedTable.setUpdateByPrimaryKeySelectiveStatementId(DEFAULT_PRIMARY_KEY_PATTERN.matcher(introspectedTable.getUpdateByPrimaryKeySelectiveStatementId()).replaceAll(primaryKeyName));
            introspectedTable.setDeleteByPrimaryKeyStatementId(DEFAULT_PRIMARY_KEY_PATTERN.matcher(introspectedTable.getDeleteByPrimaryKeyStatementId()).replaceAll(primaryKeyName));
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
        return true;
    }
}
