package org.zhuyb.graphbatis.cg.ext;

import org.mybatis.generator.api.IntrospectedTable;

/**
 * 基于表信息的扩展生成
 *
 * @author zhuyb
 */
public interface ExtGenerator {
    /**
     * 生成扩展
     */
    void generate(IntrospectedTable introspectedTable);
}
