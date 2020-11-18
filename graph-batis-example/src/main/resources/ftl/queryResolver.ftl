package org.zhuyb.graphbatis.controller;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zhuyb.graphbatis.entity.${tableConfiguration.domainObjectName};
import org.zhuyb.graphbatis.mapper.${tableConfiguration.domainObjectName}Dao;

@Component
public class ${tableConfiguration.domainObjectName}QueryResolver implements GraphQLQueryResolver {
@Autowired
private ${tableConfiguration.domainObjectName}Dao ${tableConfiguration.domainObjectName?uncap_first}Dao;
}