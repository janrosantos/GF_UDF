package com.udf.rfc;

import java.io.*;
import java.text.*;
import java.util.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;

import com.sap.aii.mapping.lookup.*;
import com.sap.aii.mapping.api.*;
import com.sap.aii.mappingtool.tf7.rt.Container;
import com.sap.aii.mappingtool.tf7.rt.ResultList;

public class partnerLookup {

	public String partnerLookup(String partnerNumber, Container container) throws StreamTransformationException {

		String partnervalue = lookupPartner(partnerNumber, container);
		if (partnervalue == null || partnervalue.length() == 0) {
			AbstractTrace trace = container.getTrace();
			trace.addDebugMessage("RFCLookup partner not found for input " + partnerNumber);
			return "";
		}
		return partnervalue;
	}

	/**
	 * Helper class which collects stream input while reading.
	 */
	static class TeeInputStream extends InputStream {
		private ByteArrayOutputStream baos;
		private InputStream wrappedInputStream;

		TeeInputStream(InputStream inputStream) {
			baos = new ByteArrayOutputStream();
			wrappedInputStream = inputStream;
		}

		/**
		 * @return stream content as String
		 */
		String getStringContent() {
			return baos.toString();
		}

		/*
		 * 
		 * @see java.io.InputStream#read()
		 */
		public int read() throws IOException {
			int r = wrappedInputStream.read();
			baos.write(r);
			return r;
		}
	}

	Hashtable partnerkey = new Hashtable();

	private String lookupPartner(String partnerNumber, Container container) {

		AbstractTrace trace = container.getTrace();
		// test if the values are in the cache
		Object obj = partnerkey.get(partnerNumber);

		if (obj != null) {
			trace.addDebugMessage("Partner cached " + partnerNumber);
			return (String) obj;
		}

		String service = "RFCLookup";
		String channelName = "CC_Lookup_Receiver_RFC";

		trace.addDebugMessage("RFCLookup called ");

		Node responseNode = null;
		TeeInputStream tee = null;

		String errorCode = null;
		String EPARTNER = null;
		try {
			// Get channel and accessor
			Channel channel = LookupService.getChannel(service, channelName);
			RfcAccessor accessor = LookupService.getRfcAccessor(channel);

			String request = "<ns0:Z_READ_TABLE_ZE31 xmlns:ns0=\"urn:sap-com:document:sap:rfc:functions\">"
					+ "<PARTNER>" + partnerNumber + "</PARTNER></ns0:Z_READ_TABLE_ZE31>";

			byte[] requestBytes = request.getBytes();
			trace.addDebugMessage("RFC Request: " + new String(requestBytes));
			// Create input stream representing the function module request
			// message
			InputStream inputStream = new ByteArrayInputStream(requestBytes);
			// Create XmlPayload
			XmlPayload requestPayload = LookupService.getXmlPayload(inputStream);
			// Execute lookup
			XmlPayload responsePayload = accessor.call(requestPayload);
			InputStream responseStream = responsePayload.getContent();
			tee = new TeeInputStream(responseStream);
			// Create DOM tree for response
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = docBuilder.parse(tee);
			trace.addDebugMessage("RFC Response2: " + tee.getStringContent());
			responseNode = document.getFirstChild();
			NodeList nodeList = responseNode.getChildNodes();

			for (int i = 0; i < nodeList.getLength(); i++) {
				try {
					Node node = nodeList.item(i);
					if (node.getNodeName().equalsIgnoreCase("E_RETURNCODE")) {
						errorCode = node.getFirstChild().getNodeValue();
					}
					if (node.getNodeName().equalsIgnoreCase("PARTNER_ID")) {
						EPARTNER = node.getFirstChild().getNodeValue();
						partnerkey.put(partnerNumber, EPARTNER);
					}
					// cache the default values for the other information if
					// there is any information
					if (node.getNodeName() != null && node.getFirstChild() != null) {
						String value = node.getFirstChild().getNodeValue();

						if (value != null && node.getNodeName() != null)
							partnerkey.put(partnerNumber + node.getNodeName(), value);
					}
				} catch (NullPointerException n) {
					// do not show an error. This occurs when no data has been
					// found in the selec.t
					trace.addWarning("No values found for partner number " + partnerNumber + " in ZE31 ");
				}
			}

		} catch (Throwable t) {

			StringWriter sw = new StringWriter();
			t.printStackTrace(new PrintWriter(sw));
			trace.addWarning(sw.toString());
		}
		if (errorCode.equals("0")) {
			trace.addDebugMessage("RFCLookup partner found " + EPARTNER);
			return EPARTNER;
		} else {
			trace.addDebugMessage("RFCLookup partner not found for input " + partnerNumber);
			return "";
		}

	}

}
