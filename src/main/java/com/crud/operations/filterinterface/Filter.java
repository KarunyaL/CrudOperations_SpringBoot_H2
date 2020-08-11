package com.crud.operations.filterinterface;

import com.crud.operations.Entity.SubTypeCodes;

@FunctionalInterface
public interface Filter {

	public boolean filter(SubTypeCodes subTypeCodes);


}
