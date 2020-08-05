package com.crud.operations.service;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crud.operations.Entity.CodeData;
import com.crud.operations.model.CodeDataModel;
import com.crud.operations.repository.CodeDataRepository;

@Service
public class CodeDataService {

	@Autowired
	CodeDataRepository codeDataRepository;

	private static final Logger logger = LoggerFactory.getLogger(CodeDataService.class);

	public CodeData add(CodeDataModel codeData) throws IllegalArgumentException {
		int subtypecodeList_size = 0;
		int typecodeList_size = codeData.getCode().size();

		for (int i = 0; i < typecodeList_size; i++) {
			subtypecodeList_size = codeData.getCode().get(i).getSubtypeCodes().size();
		}


		boolean flag1[] = new boolean[typecodeList_size];
		boolean flag2[] = new boolean[subtypecodeList_size];

		for (int i = 0; i < typecodeList_size; i++) {
			String typeCode = codeData.getCode().get(i).getTypeCode();
			if ((Pattern.matches("[[A-Z&&[AB]]{2}\\d{3}]{5}", typeCode))) {
				flag1[i] = true;
				subtypecodeList_size = codeData.getCode().get(i).getSubtypeCodes().size();
				for (int j = 0; j < subtypecodeList_size; j++) {
					String subTypeCode = codeData.getCode().get(i).getSubtypeCodes().get(j).getSubtypeCode();
					if ((Pattern.matches("[[a-z&&[xyz]]{3}\\d{3}]{6}", subTypeCode))) {
						flag2[j] = true;
					} else {
						flag2[j] = false;
						logger.error("Subtypecode input is not in correct format");
						throw new IllegalArgumentException("Subtypecode input not in given format");

					}
				}
			} else {
				flag1[i] = false;
				logger.error("Typecode input is not in correct format");
				throw new IllegalArgumentException("Typecode input is not in correct format");

			}
		}
		codeDataRepository.save(toEntity(codeData));
		logger.info("Inserting records into database");
		for (int i = 0; i < typecodeList_size; i++) {
			subtypecodeList_size = codeData.getCode().get(i).getSubtypeCodes().size();
			for (int j = 0; j < subtypecodeList_size; j++) {
				Date date = codeData.getCode().get(i).getSubtypeCodes().get(j).getUpdatedOn();
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				codeData.getCode().get(i).getSubtypeCodes().get(j).setUpdatedOn(sqlDate);
			}
		}
		return toEntity(codeData);
	}

	public void delete(String id) {
		codeDataRepository.deleteById(id);
		logger.info("Record of " + id + " has been deleted");
	}

	public List<CodeData> getCodeData() {
		logger.info("Fetching records from database");
		return (List<CodeData>) codeDataRepository.findAll();

	}

	public CodeData getCodeDataById(String id) {
		CodeData codeData = new CodeData();
		logger.info("Record of " + id + " has been fetched");
		codeData = codeDataRepository.findById(id).get();
		return codeData;
	}

	public CodeData updateData(CodeDataModel codeDataModel, String id) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		CodeData codeData = new CodeData();
		codeData = codeDataRepository.findById(id).get();

		int subtypecodeList_size = 0;
		int typecodeList_size = codeDataModel.getCode().size();
		for (int i = 0; i < typecodeList_size; i++) {
			subtypecodeList_size = codeDataModel.getCode().get(i).getSubtypeCodes().size();
		}
		boolean flag1[] = new boolean[typecodeList_size];
		boolean flag2[] = new boolean[subtypecodeList_size];

		for (int i = 0; i < typecodeList_size; i++) {
			String typeCode = codeDataModel.getCode().get(i).getTypeCode();
			if ((Pattern.matches("[[A-Z&&[AB]]{2}\\d{3}]{5}", typeCode))) {
				flag1[i] = true;
				subtypecodeList_size = codeDataModel.getCode().get(i).getSubtypeCodes().size();
				for (int j = 0; j < subtypecodeList_size; j++) {
					String subTypeCode = codeDataModel.getCode().get(i).getSubtypeCodes().get(j).getSubtypeCode();
					if ((Pattern.matches("[[a-z&&[xyz]]{3}\\d{3}]{6}", subTypeCode))) {
						flag2[j] = true;
					} else {
						flag2[j] = false;
						logger.error("Subtypecode input is not in correct format");
						throw new IllegalArgumentException("Subtypecode input not in given format");

					}
				}
			} else {
				flag1[i] = false;
				logger.error("Typecode input is not in correct format");
				throw new IllegalArgumentException("Typecode input is not in correct format");

			}
		}

		codeDataRepository.deleteById(id);
		codeData.setId(codeDataModel.getId());
		codeData.setCode(codeDataModel.getCode());
		codeData = codeDataRepository.save(codeData);

		for (int i = 0; i < typecodeList_size; i++) {
			for (int j = 0; j < subtypecodeList_size; j++) {
				Date date = codeData.getCode().get(i).getSubtypeCodes().get(j).getUpdatedOn();
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				codeData.getCode().get(i).getSubtypeCodes().get(j).setUpdatedOn(sqlDate);
			}
		}

		return codeData;
	}

	public static CodeData toEntity(CodeDataModel model) {
		CodeData entity = new CodeData();
		BeanUtils.copyProperties(model, entity);
		return entity;
	}


}
