package org.zhuyb.graphbatis.cg;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author zhuyb
 * 重命名插件
 */
@Slf4j
public class RenamePlugin extends PluginAdapter {
    public static final Pattern DEFAULT_PRIMARY_KEY_PATTERN = Pattern.compile("PrimaryKey");
    public static final Pattern DEFAULT_MAPPER_SUFFIX_PATTERN = Pattern.compile("Mapper");
    public static final Pattern DEFAULT_MAPPER_XML_SUFFIX_PATTERN = Pattern.compile("Mapper.xml");
    private String mapperSuffix;
    private String primaryKeyName;

    @Override
    public boolean validate(List<String> warnings) {
        String mapperSuffix = properties.getProperty("mapperSuffix");
        String primaryKeyName = properties.getProperty("primaryKeyName");
        if (tooLong(mapperSuffix) || tooLong(primaryKeyName)) {
            return false;
        }
        this.mapperSuffix = mapperSuffix;
        this.primaryKeyName = primaryKeyName;
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
        return true;
    }
}
