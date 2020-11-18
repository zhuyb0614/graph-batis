package org.zhuyb.graphbatis.cg;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.zhuyb.graphbatis.cg.ext.ExtGenerator;
import org.zhuyb.graphbatis.cg.ext.impl.QueryResolverGenerator;
import org.zhuyb.graphbatis.cg.ext.impl.SchemaExtGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuyb
 * @date 2020/6/8 10:46
 * GraphQL生成器
 */
@Slf4j
public class GraphBatisPlugin extends PluginAdapter {
    public static final List<ExtGenerator> extGenerators = new ArrayList<>();

    static {
        extGenerators.add(new SchemaExtGenerator());
        extGenerators.add(new QueryResolverGenerator());
    }

    @Override
    public boolean validate(List<String> warnings) {
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
