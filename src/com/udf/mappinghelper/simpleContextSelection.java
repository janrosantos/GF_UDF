package com.udf.mappinghelper;

import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import com.sap.aii.mapping.api.StreamTransformationException;
import com.sap.aii.mappingtool.tf7.rt.Container;
import com.sap.aii.mappingtool.tf7.rt.ResultList;

public class simpleContextSelection {
	
public simpleContextSelection(String[] qualifier, String[] values, String okQualifiers, ResultList result, Container container) throws StreamTransformationException{
	
	String[] ok = okQualifiers.split(",");
	for(int i=0; i< qualifier.length;i++){
	     for( int j = 0; j < ok.length; j++){
					if(ok[j].equalsIgnoreCase(qualifier[i])){
								if(i < values.length){
									result.addValue(values[i]);
	 								return;
								}
					}
		}

	}

}
}