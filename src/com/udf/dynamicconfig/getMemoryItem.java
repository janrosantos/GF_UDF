package com.udf.dynamicconfig;

import com.sap.aii.mapping.api.*;
import com.sap.aii.mappingtool.tf7.rt.*;

public class getMemoryItem {
	
	public String getMemoryItem(String ItemName, Container container) throws StreamTransformationException{
		
		GlobalContainer gc = container.getGlobalContainer();

		return (String)gc.getParameter(ItemName);
		
	}

}
