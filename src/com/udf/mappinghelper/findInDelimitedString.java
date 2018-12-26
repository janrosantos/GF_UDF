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

public class findInDelimitedString {
	
	public String findInDelimitedString(String search, String source, String delimiter, Container container) throws StreamTransformationException{
		
		//example: search="B", source="A#B#C#", delimiter="#" --> "B"

		String result = "";
		String s[] = source.split("\\" + delimiter);

		for(int i=0; i<s.length; i++) {
			if(s[i].indexOf(search) >= 0) {
				result = s[i];
				break;
			}
		}

		return result;
	}

}
