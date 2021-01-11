package ${package};

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zhuyb.graphbatis.entity.${it.tableConfiguration.domainObjectName};
import org.zhuyb.graphbatis.mapper.${it.tableConfiguration.domainObjectName}Dao;

@Component
public class ${it.tableConfiguration.domainObjectName}QueryResolver implements GraphQLQueryResolver {
@Autowired
private ${it.tableConfiguration.domainObjectName}Dao ${it.tableConfiguration.domainObjectName?uncap_first}Dao;
}