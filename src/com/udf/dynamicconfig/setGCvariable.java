package com.udf.dynamicconfig;

import com.sap.aii.mapping.api.*;
import com.sap.aii.mappingtool.tf7.rt.*;

public class setGCvariable {

	public String setGCvariable(String name, String value, Container container) throws StreamTransformationException {

		MappingTrace trace;
		trace = container.getTrace();
		trace.addInfo("setGCvariable-------- : " + name + "=" + value);
		GlobalContainer gc = container.getGlobalContainer();
		gc.setParameter(name, value);
		return "";
	}

}
