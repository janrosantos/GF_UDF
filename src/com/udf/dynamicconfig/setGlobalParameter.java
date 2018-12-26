package com.udf.dynamicconfig;

import com.sap.aii.mapping.api.*;
import com.sap.aii.mappingtool.tf7.rt.*;

public class setGlobalParameter {
	
	public String setGlobalParameter(String parameter, String value, Container container) throws StreamTransformationException{
		
		DynamicConfiguration conf = (DynamicConfiguration) container
	    .getTransformationParameters()
	    .get(StreamTransformationConstants.DYNAMIC_CONFIGURATION);
	DynamicConfigurationKey key = DynamicConfigurationKey.create("http://grundfos.com/PI/KeyValues/"+"/"+parameter, parameter); //container.getTransformationParameters().get(StreamTransformationConstants.MESSAGE_ID)+
	container.getTrace().addInfo("Store ID = http://grundfos.com/PI/KeyValues/"+container.getTransformationParameters().get(StreamTransformationConstants.MESSAGE_ID)+"/"+parameter);
	conf.put(key,value);

	return "";
		
	}

}
