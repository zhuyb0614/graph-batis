package org.zhuyb.graphbatis.cg.ext;

import org.mybatis.generator.api.IntrospectedTable;

/**
 * 基于表信息的扩展生成
 *
 * @author zhuyb
 * @date 2020/11/18
 */
public interface ExtGenerator {
    /**
     * 生成扩展
     *
     * @param introspectedTable
     */
    void generate(IntrospectedTable introspectedTable);
}
