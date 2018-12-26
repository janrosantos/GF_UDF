package com.udf.mappinghelper;

import com.sap.aii.mapping.api.*;
import com.sap.aii.mappingtool.tf7.rt.*;

public class isProductionSystem {

	public String isProductionSystem(Container container) throws StreamTransformationException {

		// return true if on P01
		if (System.getenv("SAPSYSTEMNAME").startsWith("P")) {
			return "true";
		}
		// else return false
		return "false";

	}

}
