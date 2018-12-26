package com.udf.dynamicconfig;

import com.sap.aii.mapping.api.*;
import com.sap.aii.mappingtool.tf7.rt.*;

public class setMemoryItem {
	
	public void setMemoryItem(String[] ItemName, String[] ItemValue, Container container) throws StreamTransformationException{
		
		GlobalContainer gc = container.getGlobalContainer();

		gc.setParameter(ItemName[0], (Object) ItemValue[0]);

		
	}

}
