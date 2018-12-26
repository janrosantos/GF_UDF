package com.udf.dynamicconfig;

import com.sap.aii.mapping.api.*;
import com.sap.aii.mappingtool.tf7.rt.*;


public class setExportStringParamter {
	
	public String setExportStringParamter(String input, String paramterName, Container container) throws StreamTransformationException{
		
		GlobalContainer gc = container.getGlobalContainer();
		OutputParameters out = gc.getOutputParameters();

		out.setString (paramterName,input);
		return "";
		
	}

}
