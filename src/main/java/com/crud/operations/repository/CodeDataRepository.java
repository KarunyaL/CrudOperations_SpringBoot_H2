package com.crud.operations.repository;

import org.springframework.data.repository.CrudRepository;

import com.crud.operations.Entity.CodeData;
import com.crud.operations.model.CodeDataModel;

public interface CodeDataRepository extends CrudRepository<CodeData,String> {

	CodeData save(CodeDataModel codeDataModel);

}

