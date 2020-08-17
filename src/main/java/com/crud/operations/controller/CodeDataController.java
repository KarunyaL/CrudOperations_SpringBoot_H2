package com.crud.operations.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crud.operations.Entity.CodeData;
import com.crud.operations.Entity.SubTypeCodes;
import com.crud.operations.model.CodeDataModel;
import com.crud.operations.service.CodeDataService;

@RestController
public class CodeDataController {

	private static final Logger logger = LoggerFactory.getLogger(CodeDataController.class);

	@Autowired
	private CodeDataService codeDataService;

	@PostMapping("/add")
	public CodeData addCodeData(@RequestBody CodeDataModel codeDataModel) {
		CodeData codeData = new CodeData();
		logger.info("Insertion of records into database");
		codeData = codeDataService.add(codeDataModel);
		return codeData;
		// TODO: handle exception
	}

	@GetMapping("/get")
	public List<CodeData> getCodeData() {
		List<CodeData> codeData = new ArrayList<CodeData>();
		try {
			logger.info("Reading all the records from database");
			codeData = codeDataService.getCodeData();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("Error in reading of records from database");
		}
		return codeData;
	}

	@GetMapping("/getbyId")
	public CodeData getById(@RequestParam String id) {
		CodeData codeData = new CodeData();
		try {
			logger.info("Fetching record of id " + id + " from database");
			codeData = codeDataService.getCodeDataById(id);
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("Error in fetching record of id " + id + " from database");
		}
		return codeData;
	}

	@DeleteMapping("/delete")
	public String delete(@RequestParam String id) {
		try {
			codeDataService.delete(id);
			logger.info("Deleting record of id " + id + " from database");
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("Error in deleting record of id " + id + " from database");
		}
		return "Id " + id + " is deleted from database";

	}

	@PutMapping("/update")
	public CodeData updateCodeData(@RequestBody CodeDataModel codeDataModel, @RequestParam String id) {
		CodeData codeData = new CodeData();
		codeData = codeDataService.updateData(codeDataModel, id);
		logger.info("Updating record of id " + id + " from database");
		return codeData;
	}

	@Async("asyncExecutor")
	@GetMapping("/filter_async_subtypecode") 
	public CompletableFuture<List<String>> filterGetSubtypeCodeData(@RequestParam String typeCode) throws InterruptedException { 
		return codeDataService.filterGetSubtypeCodeData(typeCode); 
	}

	@GetMapping("/filter_lambda_typecode") 
	public List<String> filterGetTypeCodeData(@RequestParam String subtypeCode) { 
		List<String> returnList = new ArrayList<String>();
		try {
			returnList =  codeDataService.filterGetTypeCodeData(subtypeCode); 
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(" Error in retreiving typecode for given subtypeCode ");
		}
		return returnList; 
	}

	@GetMapping("/sort_comparator") 
	public List<SubTypeCodes> sortGetCodeData() { 
		List<SubTypeCodes> returnList = new ArrayList<SubTypeCodes>();
		try {
			returnList = codeDataService.sortGetCodeData(); 
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(" Error in sorting and retreiving list of subtypeCodes ");
		}
		return returnList;
	}

}