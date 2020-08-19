package com.crud.operations.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.json.simple.parser.ParseException;
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
import com.crud.operations.repository.CodeDataRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CodeDataServiceTest {


	@Mock
	CodeDataService codeDataService;

	@MockBean
	CodeDataRepository codeDataRepository;

	@Test
	public void getCodeDataTest() throws FileNotFoundException, IOException, ParseException {

		List<CodeData> codeDataList = new ArrayList<CodeData>();
		ObjectMapper mapper = new ObjectMapper();
		CodeData codeData = mapper.readValue(new File("src/test/resources/codedata.json"), CodeData.class);
		codeDataList.add(codeData);
		when(codeDataRepository.findAll()).thenReturn((codeDataList));
		assertEquals(0, codeDataService.getCodeData().size());
		Assert.assertEquals("AB801", codeDataList.get(0).getCode().get(0).getTypeCode());
		assertNotNull(codeDataList);

	}

	@Test
	public void addTest() {
		List<Code> codeList = new ArrayList<Code>();
		List<SubTypeCodes> subtypeCodesList = new ArrayList<SubTypeCodes>();
		CodeDataModel codeData = new CodeDataModel();
		Code code = new Code();
		SubTypeCodes subtypeCode = new SubTypeCodes();

		codeData.setId("1134789");
		codeData.setCode(codeList);
		codeList.add(code);
		code.setTypeCode("AB801");
		code.setSubtypeCodes(subtypeCodesList);
		subtypeCode.setSubtypeCode("xyz990");
		subtypeCode.setDescription("subtypeCode-1");
		subtypeCodesList.add(subtypeCode);

		Mockito.when(codeDataRepository.save(codeData)).thenReturn(toEntity(codeData));
		String id = codeData.getId();
		Assert.assertEquals("1134789", id);
		assertNotNull(codeData);
	}


	@Test 
	public void deleteTest() { 
		String id="1234567";
		boolean isExistsBeforeDeletion = codeDataRepository.findById(id).isPresent();
		codeDataRepository.deleteById(id);
		boolean notExistsAfterDeletion = codeDataRepository.findById(id).isPresent();
		assertFalse("Deleted", notExistsAfterDeletion);
	}

	@Test
	public void updateDataTest() throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		CodeData codeData = mapper.readValue(new File("src/test/resources/codedata.json"), CodeData.class);
		CodeData codeDataModel = mapper.readValue(new File("src/test/resources/updated_codedata.json"), CodeData.class);
		String id="1134789";
		codeData.setId(codeDataModel.getId());
		codeData.setCode(codeDataModel.getCode());
		Mockito.when(codeDataRepository.save(codeData)).thenReturn(codeData);
		assertNotNull(codeDataModel);
		Assert.assertEquals("1134789", codeDataModel.getId());
		assertThat(codeDataModel.getCode().get(0).getTypeCode()).isEqualTo("AB900");
	}

	@Test
	public void dateConversionTest() throws JsonParseException, JsonMappingException, IOException { 
		ObjectMapper mapper = new ObjectMapper();
		CodeData codeData = mapper.readValue(new File("src/test/resources/codedata.json"), CodeData.class);
		Date date =	codeData.getCode().get(0).getSubtypeCodes().get(0).getUpdatedOn();
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		Assert.assertEquals(date,sqlDate);
		assertThat(sqlDate);
	}

	@Test
	public void typeCodevalidationTest() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		CodeData codeData = mapper.readValue(new File("src/test/resources/codedata.json"), CodeData.class);
		boolean flag = false;
		String typeCode = codeData.getCode().get(0).getTypeCode();
		if ((Pattern.matches("[[A-Z&&[AB]]{2}\\d{3}]{5}", typeCode))) {
			flag = true;
		}
		Assert.assertEquals(true,flag);
	}

	@Test
	public void subtypeCodevalidationTest() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		CodeData codeData = mapper.readValue(new File("src/test/resources/codedata.json"), CodeData.class);
		boolean flag = false;
		String subtypeCode = codeData.getCode().get(0).getSubtypeCodes().get(0).getSubtypeCode();
		if ((Pattern.matches("[[a-z&&[xyz]]{3}\\d{3}]{6}", subtypeCode))) {
			flag = true;
		}
		Assert.assertEquals(true,flag);
	}

	public static CodeData toEntity(CodeDataModel model) {
		CodeData entity = new CodeData();
		BeanUtils.copyProperties(model, entity);
		return entity;
	}

}
