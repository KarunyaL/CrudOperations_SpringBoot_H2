package com.crud.operations.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crud.operations.Entity.CodeData;
import com.crud.operations.Entity.SubTypeCodes;
import com.crud.operations.model.CodeDataModel;
import com.crud.operations.repository.CodeDataRepository;
import com.crud.operations.comparator.CodeComparator;
import com.crud.operations.filterinterface.Filter;

@Service
public class CodeDataService {

	@Autowired
	CodeDataRepository codeDataRepository;

	private static final Logger logger = LoggerFactory.getLogger(CodeDataService.class);

	int typecodeList_size = 0;
	int subtypecodeList_size = 0;

	boolean flag=false;


	public CodeData add(CodeDataModel codeData) throws IllegalArgumentException {


		typecodeList_size = codeData.getCode().size();
		int size[] = new int[typecodeList_size];
		for (int i = 0; i < typecodeList_size; i++) {
			size[i] = codeData.getCode().get(i).getSubtypeCodes().size();
		}
		Arrays.sort(size);
		subtypecodeList_size = size[ (size.length) - 1 ];

		validation(codeData);

		codeDataRepository.save(toEntity(codeData));
		logger.info("Inserting records into database");

		dateConversion(codeData);

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

		typecodeList_size = codeDataModel.getCode().size();
		int size[] = new int[typecodeList_size];
		for (int i = 0; i < typecodeList_size; i++) {
			size[i] = codeDataModel.getCode().get(i).getSubtypeCodes().size();
		}
		Arrays.sort(size);
		subtypecodeList_size = size[ (size.length) - 1 ];

		validation(codeDataModel);

		codeDataRepository.deleteById(id);
		codeData.setId(codeDataModel.getId());
		codeData.setCode(codeDataModel.getCode());
		codeData = codeDataRepository.save(codeData);
		logger.info(" Updating records of " + id + " into database ");

		dateConversion(codeDataModel);

		return toEntity(codeDataModel);
	}

	public static CodeData toEntity(CodeDataModel model) {
		CodeData entity = new CodeData();
		BeanUtils.copyProperties(model, entity);
		return entity;
	}


	public List<SubTypeCodes> sortGetCodeData() {
		List<CodeData> codeDataList = new ArrayList<CodeData>(); 

		codeDataList = getallrecords();

		List<SubTypeCodes> returnList = new ArrayList<SubTypeCodes>();
		List<SubTypeCodes> subtypeCodeObjList = new ArrayList<SubTypeCodes>();

		for(int k=0;k<codeDataList.size();k++) {
			for (int i = 0; i < typecodeList_size; i++) {
				subtypeCodeObjList = codeDataList.get(k).getCode().get(i).getSubtypeCodes();
				System.out.println("After sorting subtypescode list using Comparator");
				CodeComparator codeComp = new CodeComparator();
				Collections.sort(subtypeCodeObjList, codeComp);
				logger.info("Sorting SubtypeCode using Comparator");
				Iterator itr=subtypeCodeObjList.iterator();  
				while(itr.hasNext()){  
					SubTypeCodes sub=(SubTypeCodes)itr.next();  
					returnList.add(sub);
					System.out.println(sub.getSubtypeCode()+" "+sub.getDescription());  

				}
			}  

		}
		logger.info("Retreiving SubtypeCode using Comparator");
		return returnList;
	}


	@Transactional
	@Async("asyncExecutor")
	public CompletableFuture<List<String>> filterGetSubtypeCodeData(String typeCode) throws InterruptedException {
		List<String> returnList = new ArrayList<String>();

		long start = System.currentTimeMillis();
		List<CodeData> codeDataList = getallrecords();

		List<SubTypeCodes> subtypeCodeObjList = new ArrayList<SubTypeCodes>();
		List<String> list = new ArrayList<String>();			

		for(int k=0;k<codeDataList.size();k++) {
			for (int i = 0; i < typecodeList_size; i++) {
				list.add(codeDataList.get(k).getCode().get(i).getTypeCode());
			}
		}
		if(list.contains(typeCode)){
			for(int k=0;k<codeDataList.size();k++) {
				for (int i = 0; i < typecodeList_size; i++) {
					if( (codeDataList.get(k).getCode().get(i).getTypeCode().equals(typeCode)) ){
						logger.info("Given typeCode is present in database");
						logger.info("Retreiving the list of subtypeCodes for given typeCode");
						int size = codeDataList.get(k).getCode().get(i).getSubtypeCodes().size();
						for(int j=0;j<size;j++) {
							subtypeCodeObjList = codeDataList.get(k).getCode().get(i).getSubtypeCodes();
							returnList.add(subtypeCodeObjList.get(j).getSubtypeCode());
						}
					}
				}
			}
		}

		else {

			logger.error("Given typeCode is not present in database");
			throw new IllegalArgumentException("Given typeCode is not present in database");

		}			

		logger.info("Retreiving list SubtypeCodes for given Typecode using Async call");
		System.out.println("Async call " + returnList);
		logger.info("Elapsed time: " + (System.currentTimeMillis() - start));
		Thread.sleep(2000L);    //Intentional delay
		return CompletableFuture.completedFuture(returnList);

	}

	public List<String> filterGetTypeCodeData(String subtypeCode) {

		List<CodeData> codeDataList = getallrecords();

		List<SubTypeCodes> subtypeCodeObjList = new ArrayList<SubTypeCodes>();
		List<String> typecodeList = new ArrayList<String>();
		String typecode = " ";
		String filteredtypeCode = " ";

		System.out.println("Iterating main list using Lambda: ");

		List<String> list = codeDataList.stream() 
				.flatMap(a -> a.getCode().stream()) 
				.flatMap(b -> b.getSubtypeCodes().stream()) 
				.map(c -> c.getSubtypeCode()) 
				.collect(Collectors.toList()); 
		System.out.println(" List of Subtypecodes:  " +  list);

		if(list.contains(subtypeCode)){			
			for(int k=0;k<codeDataList.size();k++) {
				for (int i = 0; i < typecodeList_size; i++) {
					typecode = codeDataList.get(k).getCode().get(i).getTypeCode();
					subtypeCodeObjList = codeDataList.get(k).getCode().get(i).getSubtypeCodes();
					Filter filter = subTypeCodes -> subTypeCodes.getSubtypeCode().equals(subtypeCode);
					filteredtypeCode = filterId(typecode,subtypeCodeObjList, filter);
					if( !(filteredtypeCode.equals(" ")) && flag == true ) {
						typecodeList.add(filteredtypeCode);
						logger.info("Retreiving TypeCodes for given Subtypecode using Lambda");
						System.out.println("Lambda Filter " + typecodeList);
					}

				}
			}
		}
		else {
			logger.error("Given SubtypeCode is not present in database");
			throw new IllegalArgumentException("Given SubtypeCode is not present in database");
		}

		return typecodeList;

	}

	public List<CodeData> getallrecords() {

		List<CodeData> codeDataList = new ArrayList<CodeData>(); 

		logger.info("Fetching records from database"); 
		codeDataList=(List<CodeData>) codeDataRepository.findAll();

		for(int k=0;k<codeDataList.size();k++) {
			typecodeList_size = codeDataList.get(k).getCode().size();
			for (int i = 0; i < typecodeList_size; i++) {
				subtypecodeList_size = codeDataList.get(k).getCode().get(i).getSubtypeCodes().size();
			}
		}
		return codeDataList;

	}

	public void validation(CodeDataModel codeData) {

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
	}


	public void dateConversion(CodeDataModel codeData) { 
		for (int i = 0; i < typecodeList_size; i++) {
			int size = codeData.getCode().get(i).getSubtypeCodes().size();
			for (int j = 0; j < size; j++) { 
				Date date =	codeData.getCode().get(i).getSubtypeCodes().get(j).getUpdatedOn();
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				codeData.getCode().get(i).getSubtypeCodes().get(j).setUpdatedOn(sqlDate); } }

	}

	public String filterId(String typeCode, List<SubTypeCodes> subtypeCodeObjList, Filter filter) {

		String filtered_typeCode = " ";
		for(SubTypeCodes code : subtypeCodeObjList) {

			if(filter.filter(code)) {
				flag = true;
				filtered_typeCode = typeCode;
				System.out.println("Filtered Code " + code);
			}

		}
		return filtered_typeCode;

	}
} 

