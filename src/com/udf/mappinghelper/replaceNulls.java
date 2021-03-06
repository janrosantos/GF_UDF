package com.udf.mappinghelper;

import java.io.*;
import java.text.*;
import java.util.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;

import com.sap.aii.mapping.lookup.*;
import com.sap.aii.mapping.api.*;
import com.sap.aii.mappingtool.tf7.rt.Container;
import com.sap.aii.mappingtool.tf7.rt.ResultList;

public class replaceNulls {

	public void replaceNulls(String[] q1, String[] value, ResultList result, Container container)
			throws StreamTransformationException {

		// replaces <null> values in beginning of queues resulting in a context
		// change

		MappingTrace trace;
		trace = container.getTrace();

		boolean aftervalue = false;
		boolean displayTrace = false;

		if (displayTrace) {
			trace.addInfo("replaceNulls");
			trace.addInfo("q1.length=" + q1.length);
			trace.addInfo("+++++++++++++++++++++++++++++++++++++++++");
		}

		for (int i = 0; i < q1.length; i++) {
			if (displayTrace)
				trace.addInfo(q1[i]);
			if (q1[i].equals(ResultList.CC)) {
				if (aftervalue)
					result.addValue(q1[i]);
				else {
					result.addValue(value[0]);
					result.addValue(q1[i]);
				}
				aftervalue = false;
			} else {
				result.addValue(q1[i]);
				aftervalue = true;
			}
		}

	}

}
