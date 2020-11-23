package org.zhuyb.graphbatis.cg.ext.impl;

import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.generator.api.IntrospectedTable;
import org.zhuyb.graphbatis.cg.ext.ExtGenerator;
import org.zhuyb.graphbatis.cg.ext.FreemarkerExtGenerator;

import java.io.File;
import java.io.IOException;

/**
 * @author zhuyb
 */
@Slf4j
public class QueryResolverGenerator extends FreemarkerExtGenerator implements ExtGenerator {
    String queryResolverTemplateFileName = "queryResolver.ftl";
    String queryResolverOutputPath = new File("./src/main/java/org/zhuyb/graphbatis/controller").getAbsolutePath();

    @Override
    public void generate(IntrospectedTable introspectedTable) {
        graphQLQueryResolverGenerated(introspectedTable);
    }

    private void graphQLQueryResolverGenerated(IntrospectedTable introspectedTable) {
        Template template;
        try {
            template = getTemplate(queryResolverTemplateFileName);
        } catch (IOException e) {
            log.error("load template error", e);
            return;
        }
        String fileName = queryResolverOutputPath + File.separator + getObjectName(introspectedTable) + "QueryResolver.java";
        writFile(template, introspectedTable, fileName);
    }
}
