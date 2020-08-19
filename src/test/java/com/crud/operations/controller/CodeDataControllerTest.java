package com.crud.operations.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.crud.operations.Entity.Code;
import com.crud.operations.Entity.CodeData;
import com.crud.operations.Entity.SubTypeCodes;
import com.crud.operations.model.CodeDataModel;
import com.crud.operations.model.CodeModel;
import com.crud.operations.model.SubTypeCodesModel;
import com.crud.operations.service.CodeDataService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CodeDataControllerTest {

	@Mock
	CodeDataController codeDataController;

	@MockBean
	CodeDataService codeDataService;


	@Test 
	public void getCodeDataTest() {
		List<CodeData> codeDataList = new ArrayList<CodeData>();
		List<Code> codeList = new ArrayList<Code>();
		List<SubTypeCodesModel> subtypeCodesList = new ArrayList<SubTypeCodesModel>();

		CodeDataModel codeData = new CodeDataModel();
		CodeModel code = new CodeModel();
		SubTypeCodesModel subtypeCode = new SubTypeCodesModel();

		codeData.setId("1134789");
		codeData.setCode(codeList);
		code.setTypeCode("AB801");
		code.setSubtypeCodes(subtypeCodesList);
		subtypeCode.setSubtypeCode("xyz990");
		subtypeCode.setDescription("subtypeCode-1");
		subtypeCodesList.add(subtypeCode);
		codeDataList.add(toEntity(codeData));

		Mockito.when(codeDataService.getCodeData()).thenReturn(codeDataList);
		assertEquals("1134789", codeDataList.get(0).getId());
		assertNotNull(codeDataList);

	}


	@Test
	public void addCodeDataTest() throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper mapper = new ObjectMapper(); 
		CodeDataModel codeDataModel = mapper.readValue(new File("src/test/resources/codedata.json"),CodeDataModel.class);
		Mockito.when(codeDataService.add(codeDataModel)).thenReturn(toEntity(codeDataModel)); 
		String id = codeDataModel.getId();
		Assert.assertEquals("1134789", id);
		Assert.assertEquals("xyz915",codeDataModel.getCode().get(1).getSubtypeCodes().get(0).getSubtypeCode());
		assertNotNull(codeDataModel);

	}



	@Test
	public void filterGetTypeCodeDataTest() throws JsonParseException, JsonMappingException, IOException{
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper(); 
		CodeDataModel codeDataModel = mapper.readValue(new File("src/test/resources/sample_codedata.json"),CodeDataModel.class);
		List<String> returnList = new ArrayList<String>();
		String subtypeCode="xyz915";

		for (int i = 0; i < codeDataModel.getCode().size(); i++) {
			returnList.add(codeDataModel.getCode().get(i).getTypeCode());
		}

		Mockito.when(codeDataService.filterGetTypeCodeData(subtypeCode)).thenReturn(returnList);
		Assert.assertEquals("AB907", returnList.get(1));
		assertNotNull(returnList);
	}


	@Test public void filterGetSubtypeCodeData() throws JsonParseException, JsonMappingException, IOException, InterruptedException, ExecutionException {
		long start = System.currentTimeMillis();
		boolean flag = false;
		ObjectMapper mapper = new ObjectMapper();
		CodeDataModel codeDataModel =mapper.readValue(new File("src/test/resources/sample_codedata.json"),CodeDataModel.class);
		List<String> returnList1 = new ArrayList<String>();
		CompletableFuture<List<String>> returnList = new CompletableFuture<List<String>>(); 
		String typeCode="AB801";
		for (int i = 0; i < codeDataModel.getCode().size(); i++) {
			int size = codeDataModel.getCode().get(i).getSubtypeCodes().size(); 
			for(int j=0;j <size; j++) { 
				returnList1.add(codeDataModel.getCode().get(i).getSubtypeCodes().get(j).getSubtypeCode()); 
			}
		}
		Mockito.when(codeDataService.filterGetSubtypeCodeData(typeCode)).thenReturn(returnList); 
		long end = System.currentTimeMillis() - start ;
		if(end<=50) {
			flag = true;
		}
		Assert.assertTrue(flag);
		Assert.assertEquals("xyz990", returnList1.get(0));
		assertNotNull(returnList);

	}


	@Test 
	public void sortGetCodeDataTest() throws JsonParseException, JsonMappingException, IOException {

		List<String> returnList = new ArrayList<String>();
		List<SubTypeCodes> returnList1 = new ArrayList<SubTypeCodes>();
		ObjectMapper mapper = new ObjectMapper(); 
		CodeDataModel codeDataModel = mapper.readValue(new File("src/test/resources/sample_codedata.json"),CodeDataModel.class);

		for (int i = 0; i < codeDataModel.getCode().size(); i++) {
			int size = codeDataModel.getCode().get(i).getSubtypeCodes().size();
			for(int j=0;j < size; j++) {
				returnList.add(codeDataModel.getCode().get(i).getSubtypeCodes().get(j).getSubtypeCode());
				returnList1 = codeDataModel.getCode().get(i).getSubtypeCodes();
			}
		}

		Collections.sort(returnList);
		Mockito.when(codeDataService.sortGetCodeData()).thenReturn(returnList1); 
		assertNotNull(returnList1);
		Assert.assertEquals("xyz901", returnList.get(0));
	}

	@Test
	public void updateDataTest() throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		CodeDataModel codeData = mapper.readValue(new File("src/test/resources/codedata.json"), CodeDataModel.class);
		CodeDataModel codeDataModel = mapper.readValue(new File("src/test/resources/updated_codedata.json"), CodeDataModel.class);
		String id="1134789";
		codeData.setId(codeDataModel.getId());
		codeData.setCode(codeDataModel.getCode());
		Mockito.when(codeDataService.updateData(codeDataModel, id)).thenReturn(toEntity(codeDataModel));
		assertNotNull(codeDataModel);
		Assert.assertEquals("1134789", codeDataModel.getId());
		assertThat(codeDataModel.getCode().get(0).getTypeCode()).isEqualTo("AB900");
	}



	public static CodeData toEntity(CodeDataModel model) {
		CodeData entity = new CodeData();
		BeanUtils.copyProperties(model, entity);
		return entity;
	}


}
