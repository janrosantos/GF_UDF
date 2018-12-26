package com.udf.dynamicconfig;

import com.sap.aii.mapping.api.*;
import com.sap.aii.mappingtool.tf7.rt.*;
import com.seeburger.functions.permstore.*;

public class readSeeburgerVariable {

	public String readSeeburgerVariable(String value, String group, Container container)
			throws StreamTransformationException {

		try {
			VariableBean vb = null;
			Object obj = container.getParameter("VB" + group);
			if (obj == null) {
				// get a new VB and save it as a paramter
				vb = VariableFactory.getVariableInstance(group);

				container.setParameter("VB" + group, vb);
				container.getTrace().addInfo("Variable bean for gorup " + group + " lookedup");
			} else {
				vb = (VariableBean) obj;
			}
			String result = vb.getStringVariable(value);
			if (result == null)
				result = "";
			return result;
		} catch (Exception e) {
			return " ";
		}

	}
}
