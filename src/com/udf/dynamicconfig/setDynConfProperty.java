package com.udf.dynamicconfig;

import com.sap.aii.mapping.api.*;
import com.sap.aii.mappingtool.tf7.rt.*;

public class setDynConfProperty {
	
	public String setDynConfProperty(String name, String namespace, String value, Container container) throws StreamTransformationException{
		
		DynamicConfiguration conf = (DynamicConfiguration) container
	    .getTransformationParameters()
	    .get(StreamTransformationConstants.DYNAMIC_CONFIGURATION);
	DynamicConfigurationKey key = DynamicConfigurationKey.create(namespace, name);
	conf.put(key,value);

	return value;
		
	}

}
