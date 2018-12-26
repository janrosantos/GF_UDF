package com.udf.dynamicconfig;

import com.sap.aii.mapping.api.*;
import com.sap.aii.mappingtool.tf7.rt.*;


public class setExportIntParamter {
	
	public String setExportIntParamter(String input, String paramterName, Container container) throws StreamTransformationException{
		
		GlobalContainer gc = container.getGlobalContainer();
		OutputParameters out = gc.getOutputParameters();

		out.setInt (paramterName,Integer.parseInt(input));
		return "";
		
	}

}
