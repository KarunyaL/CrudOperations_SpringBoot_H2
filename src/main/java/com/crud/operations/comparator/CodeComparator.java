package com.crud.operations.comparator;

import java.util.Comparator;

import com.crud.operations.Entity.SubTypeCodes;


public class CodeComparator implements Comparator<SubTypeCodes>{

	@Override
	public int compare(SubTypeCodes o1, SubTypeCodes o2) {
		// TODO Auto-generated method stub

		return o1.getSubtypeCode().compareTo(o2.getSubtypeCode());  

	}


}  
