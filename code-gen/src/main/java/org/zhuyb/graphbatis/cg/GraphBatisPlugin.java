package org.zhuyb.graphbatis.cg;

import org.mybatis.generator.api.PluginAdapter;

import java.util.List;

public class GraphBatisPlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> list) {
        return false;
    }
}
