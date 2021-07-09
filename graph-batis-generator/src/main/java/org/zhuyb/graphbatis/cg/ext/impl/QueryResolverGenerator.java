package org.zhuyb.graphbatis.cg.ext.impl;

import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.IntrospectedTable;
import org.zhuyb.graphbatis.cg.ext.ExtGenerator;
import org.zhuyb.graphbatis.cg.ext.FreemarkerExtGenerator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author zhuyb
 */
@Slf4j
public class QueryResolverGenerator extends FreemarkerExtGenerator implements ExtGenerator {
    private String queryResolverTemplateFileName = "queryResolver.ftl";
    private String queryResolverOutputPath = new File("./src/main/java/org/zhuyb/graphbatis/controller").getAbsolutePath();
    private String packagePath;
    private String resolverTargetPackage;
    private String resolverTargetPath;

    @Override
    public void generate(IntrospectedTable introspectedTable) {
        graphQLQueryResolverGenerated(introspectedTable);
    }

    @Override
    public void setUp(Properties properties) {
        resolverTargetPath = properties.getProperty("resolverTargetPath");
        if (StringUtils.isNotEmpty(resolverTargetPath)) {
            queryResolverOutputPath = new File(resolverTargetPath).getAbsolutePath();
        }
        resolverTargetPackage = properties.getProperty("resolverTargetPackage");
        if (StringUtils.isNotEmpty(resolverTargetPackage)) {
            packagePath = resolverTargetPackage.replaceAll("\\.", "/");
        }
    }

    private void graphQLQueryResolverGenerated(IntrospectedTable introspectedTable) {
        Template template;
        try {
            template = getTemplate(queryResolverTemplateFileName);
        } catch (IOException e) {
            log.error("load template error", e);
            return;
        }
        String fileName = new StringBuilder()
                .append(queryResolverOutputPath)
                .append(File.separator)
                .append(packagePath)
                .append(File.separator)
                .append(getObjectName(introspectedTable))
                .append("QueryResolver.java")
                .toString();
        Map<String, Object> data = new HashMap<>();
        data.put("it", introspectedTable);
        data.put("package", resolverTargetPackage);
        writFile(template, data, fileName);
    }
}
