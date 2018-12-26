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

public class concatWithDelimiter {
	
	public void concatWithDelimiter(String[] source, String[] delimiter, ResultList result, Container container) throws StreamTransformationException{
		
		//example: source=[A] [B] [C], delimiter="#" --> "A#B#C#"

		String s = "";

		// TRACE
		//MappingTrace trace;
		//trace = container.getTrace();
		//trace.addInfo("concatWithDelimiter:");
		//for(int i=0; i<source.length; i++) {
//			if(!source[i].equals(ResultList.CC)) trace.addInfo(source[i]);
//			if(source[i].equals(ResultList.CC)) trace.addInfo("---------- CC ----------");
		//}

		for (int i=0; i<source.length; i++) {
			if(!source[i].equals(ResultList.CC)) s = s + source[i] + delimiter[0];
			if(source[i].equals(ResultList.CC)) {
//				trace.addInfo("CC mødt: s=" + s);
				result.addValue(s);
				result.addValue(ResultList.CC);
				s = "";
			}
		}

		if(!s.equals("")) result.addValue(s);

		//trace.addInfo("result=" + s);
		//result.addValue(s);
		
	}

}
