package com.udf.dynamicconfig;

import com.sap.aii.mapping.api.*;
import com.sap.aii.mappingtool.tf7.rt.*;


public class getGlobalParameter {
	
	public String getGlobalParameter(String parameter, Container container) throws StreamTransformationException{
		

		DynamicConfiguration conf = (DynamicConfiguration) container
		    .getTransformationParameters()
		    .get(StreamTransformationConstants.DYNAMIC_CONFIGURATION);
		DynamicConfigurationKey key = DynamicConfigurationKey.create("http://grundfos.com/PI/KeyValues/"+container.getTransformationParameters().get(StreamTransformationConstants.MESSAGE_ID)+"/"+parameter,parameter);

		container.getTrace().addInfo("ResponseID = http://grundfos.com/PI/KeyValues/"+container.getTransformationParameters().get(StreamTransformationConstants.MESSAGE_ID)+"/"+parameter);
		try{
		   return conf.get(key);
		} catch (Exception e){
		   throw new RuntimeException("The parameter '"+parameter+"' was not found");
		}


		
	}

}
