package com.udf.mappinghelper;

import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import com.sap.aii.mapping.api.StreamTransformationException;
import com.sap.aii.mappingtool.tf7.rt.Container;
import com.sap.aii.mappingtool.tf7.rt.ResultList;

public class selectXMLNodes {

	public selectXMLNodes(String[] xmllist, String keyfield, String keyvaluelist, String resultField, ResultList result, Container container) throws StreamTransformationException{
		
		try{
			for (String xml : xmllist) {
			if(xml.trim().length()>0){
				
			
			String[] keyvalues = keyvaluelist.split(",");
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
			.newDocumentBuilder();
				Document document = docBuilder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
			NodeList tagslist = document.getElementsByTagName(keyfield);
		
			
			for(int i = 0; i< tagslist.getLength();i++) {
				Node node = tagslist.item(i);
				String nodename = node.getFirstChild().getNodeValue();
				for (String keyvalue : keyvalues) {
					
					if(nodename.equals(keyvalue)){
						//add the content of the result field to the output
						//look from the parrent node
						Element parrentNode = (Element)node.getParentNode();
						NodeList resultList = parrentNode.getElementsByTagName(resultField);
						for(int j = 0; j< resultList.getLength();j++) {
							Node resultnode = resultList.item(j);
							
							result.addValue( resultnode.getFirstChild().getNodeValue());
						}
					}
				}
				
				
			}
			}
			}
			
		}catch(Exception e){
			throw new StreamTransformationException("Unable to parse xml", e);
	}
		
	}
	
}
