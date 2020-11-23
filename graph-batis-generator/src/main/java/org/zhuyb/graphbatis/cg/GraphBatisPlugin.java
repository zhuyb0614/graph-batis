package org.zhuyb.graphbatis.cg;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.zhuyb.graphbatis.cg.ext.ExtGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuyb
 * GraphQL生成器
 */
@Slf4j
public class GraphBatisPlugin extends PluginAdapter {
    public static final List<ExtGenerator> extGenerators = new ArrayList<>();

    @Override
    public boolean validate(List<String> warnings) {
        String extGeneratorNames = properties.getProperty("ext-generators");
        if (StringUtils.isEmpty(extGeneratorNames)) {
            log.warn("ext-generators is empty you can use QueryResolver,Schema");
        } else {
            String[] names = extGeneratorNames.split(",");
            for (String name : names) {
                try {
                    extGenerators.add((ExtGenerator) Class.forName(String.format("org.zhuyb.graphbatis.cg.ext.impl.%sGenerator", name)).newInstance());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {
        //解决有泛型时 继承全类名问题
        topLevelClass.getSuperClass().ifPresent(fullyQualifiedJavaType -> {
                    topLevelClass.setSuperClass(fullyQualifiedJavaType.getShortName());
                }
        );
        for (ExtGenerator extGenerator : extGenerators) {
            try {
                extGenerator.generate(introspectedTable);
            } catch (Exception e) {
                log.error("ext generator {} error", extGenerator, e);
            }
        }
        return true;
    }
}
