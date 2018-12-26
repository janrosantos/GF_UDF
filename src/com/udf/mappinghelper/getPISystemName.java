package com.udf.mappinghelper;

import com.sap.aii.mapping.api.*;
import com.sap.aii.mappingtool.tf7.rt.*;

public class getPISystemName {
	
	public String getPISystemName(Container container) throws StreamTransformationException{
		
		return  System.getenv("SAPSYSTEMNAME");
		
	}

}
