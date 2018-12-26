package com.udf.dynamicconfig;

import com.sap.aii.mapping.api.*;
import com.sap.aii.mappingtool.tf7.rt.*;

public class getGCvariable {
	public String getGCvariable(String name, Container container) throws StreamTransformationException {

		MappingTrace trace;
		trace = container.getTrace();
		GlobalContainer gc = container.getGlobalContainer();
		String value = (String) gc.getParameter(name);
		trace.addInfo("getGCvariable-------- : " + name + "=" + value);
		return value;

	}

}
