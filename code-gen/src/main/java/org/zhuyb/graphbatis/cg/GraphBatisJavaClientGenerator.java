package org.zhuyb.graphbatis.cg;

import org.mybatis.generator.codegen.mybatis3.javamapper.SimpleJavaClientGenerator;

public class GraphBatisJavaClientGenerator extends SimpleJavaClientGenerator {
    public GraphBatisJavaClientGenerator(String project) {
        super(project);
    }

    public GraphBatisJavaClientGenerator(String project, boolean requiresMatchedXMLGenerator) {
        super(project, requiresMatchedXMLGenerator);
    }
}
