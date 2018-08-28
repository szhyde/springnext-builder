package ${projectInfo.packageName}.${projectInfo.modelName}.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ${projectInfo.packageName}.base.service.BaseService;
import ${projectInfo.packageName}.${projectInfo.modelName}.entity.${po.type.typeName};
import ${projectInfo.packageName}.${projectInfo.modelName}.repository.jpa.BaseDao;
import ${projectInfo.packageName}.${projectInfo.modelName}.repository.jpa.${po.type.typeName}Dao;

/**
 * ${po.remark}业务类.
 * 
 * @author ${projectInfo.author}
 */
@Service
@Transactional(readOnly=true)
public class ${po.type.typeName}Service extends BaseService<${po.type.typeName}, String>{

	@Autowired
	private ${po.type.typeName}Dao ${po.type.typeName?lower_case}Dao;

	@Override
	protected BaseDao<${po.type.typeName}, String> initBaseDao() {
		return ${po.type.typeName?lower_case}Dao;
	}

}
