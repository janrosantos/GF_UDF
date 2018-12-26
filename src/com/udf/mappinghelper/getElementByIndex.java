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

public class getElementByIndex {

	public void getElementByIndex(String[] source, int[] index, ResultList result, Container container)
			throws StreamTransformationException {
		// example: source=[A] [B] [C]
		// index = 2
		// result = [C]

		boolean doTrace = false;

		// TRACE
		if (doTrace) {
			MappingTrace trace;
			trace = container.getTrace();
			trace.addInfo("getElementByIndex:");
			for (int i = 0; i < source.length; i++) {
				if (!source[i].equals(ResultList.CC))
					trace.addInfo(i + ": " + source[i]);
				if (source[i].equals(ResultList.CC))
					trace.addInfo("---------- CC ---------");
			}
		}

		if (index[0] < source.length) {
			result.addValue(source[index[0]]);
		} else {
			result.addValue("");
		}

	}

}
