package com.udf.mappinghelper;

import java.io.*;
import java.text.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import com.sap.aii.mapping.api.StreamTransformationException;
import com.sap.aii.mappingtool.tf7.rt.Container;
import com.sap.aii.mappingtool.tf7.rt.ResultList;

public class formatIfNumeric {
	
	public String formatIfNumeric(String num, String format, Container container) throws StreamTransformationException{
		
		try{
			NumberFormat formatter = new DecimalFormat(format);
			return formatter.format(Long.parseLong(num));
			}
			catch(Exception e){

			       return num; 
			}
		
	}

}
