package com.crud.operations.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crud.operations.Entity.CodeData;
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
		}catch (Exception e) {
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
		}catch (IllegalArgumentException e) {
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
		}catch (IllegalArgumentException e) {
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


}