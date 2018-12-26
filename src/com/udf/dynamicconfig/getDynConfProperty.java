package com.udf.dynamicconfig;

import com.sap.aii.mapping.api.*;
import com.sap.aii.mappingtool.tf7.rt.*;

public class getDynConfProperty {
	
	public String getDynConfProperty(String name, String namespace, Container container) throws StreamTransformationException{
		

		DynamicConfiguration conf = (DynamicConfiguration) container
		    .getTransformationParameters()
		    .get(StreamTransformationConstants.DYNAMIC_CONFIGURATION);
		DynamicConfigurationKey key = DynamicConfigurationKey.create(namespace, name);

		try{
		   return conf.get(key);
		} catch (Exception e){
		   throw new RuntimeException("Dyn.Conf. property '"+namespace+"/"+name+"' was not found!");
		}

		
	}

}
