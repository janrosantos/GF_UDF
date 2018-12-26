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

public class genericRFCHash {

	public String lookupRFC(String RFCQuery, String prefix, Channel channel, Container container)
			throws StreamTransformationException {

		setTrace(container);
		String service = "RFCLookup";

		trace("RFCLookup called ");

		Node responseNode = null;
		TeeInputStream tee = null;

		try {
			// Get channel and accessor
			RfcAccessor accessor = LookupService.getRfcAccessor(channel);

			byte[] requestBytes = RFCQuery.getBytes();
			trace("RFC Request: " + new String(requestBytes));
			// Create input stream representing the function module request
			// message
			InputStream inputStream = new ByteArrayInputStream(requestBytes);
			// Create XmlPayload
			XmlPayload requestPayload = LookupService.getXmlPayload(inputStream);
			// Execute lookup
			XmlPayload responsePayload = accessor.call(requestPayload);
			InputStream responseStream = responsePayload.getContent();
			parseRequest(responseStream, prefix);
		} catch (NullPointerException n) {
			StringWriter sw = new StringWriter();
			n.printStackTrace(new PrintWriter(sw));
			trace(sw.toString());
		} catch (Throwable t) {

			StringWriter sw = new StringWriter();
			t.printStackTrace(new PrintWriter(sw));
			trace(sw.toString());
		}

		return "ok";
	}

	public String getItem(String table, String name, String prefix, Container container)
			throws StreamTransformationException {

		TableContent selectedTable = storage.get(prefix + table);
		try {
			return selectedTable.getEntry(name);
		} catch (Exception e) {
			return "";
		}
	}

	public void getTableColumn(String table, String column, ResultList result, String[] prefix, Container container)
			throws StreamTransformationException {

		TableContent selectedTable = storage.get(prefix[0] + table);

		String[] data = selectedTable.getColumn(column);
		for (int i = 0; i < data.length; i++) {
			result.addValue(data[i]);

		}
	}

	public void getValueFromId(String table, String keyField, String field, ResultList result, String[] keyvalues,
			String[] prefix, Container container) throws StreamTransformationException {

		TableContent selectedTable = storage.get(prefix[0] + table);
		for (int i = 0; i < keyvalues.length; i++) {

			result.addValue(selectedTable.getValueForField(keyField, keyvalues[i], field));

		}
	}

	public String getItem(String table, String name) {
		TableContent selectedTable = storage.get(table);
		return selectedTable.getEntry(name);
	}

	public String[] getTableColumn(String table, String column) {
		TableContent selectedTable = storage.get(table);
		return selectedTable.getColumn(column);

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
		 * (non-Javadoc)
		 * 
		 * @see java.io.InputStream#read()
		 */
		public int read() throws IOException {
			int r = wrappedInputStream.read();
			if (r > -1)
				baos.write(r);
			return r;
		}
	}

	class TableContent {

		public static final int ENTRIES = 1;
		public static final int TABLES = 2;
		public static final int PARAMETER = 1;
		public static final int NOTFOUND = 88;
		int type = 100;

		/**
		 * Store the table data in this function
		 */
		ArrayList<Hashtable<String, String>> tabledata = new ArrayList<Hashtable<String, String>>();
		/**
		 * Store the entry data
		 */
		Hashtable<String, String> entrydata = new Hashtable<String, String>();

		public TableContent() {
			this.type = type;
		}

		public int getType() {
			return type;
		}

		public String[] getColumn(String column) {
			String[] res = new String[tabledata.size()];
			for (int i = 0; i < res.length; i++) {
				String value = tabledata.get(i).get(column);
				if (value == null)
					value = "";
				res[i] = value;
			}
			return res;
		}

		/**
		 * Get the value from a line which has the keyfield and value
		 */
		public String getValueForField(String keyField, String keyValue, String targetField) {

			for (int i = 0; i < tabledata.size(); i++) {
				String value = tabledata.get(i).get(keyField);
				if (value.equals(keyValue)) {
					value = tabledata.get(i).get(targetField);
					if (value == null)
						return "";
					return value;

				}

			}

			return "NOTFOUND";
		}

		public String getEntry(String name) {
			String res = entrydata.get(name);
			if (res == null)
				return "";
			return res;

		}

		public void addEntries(String key, String value) {
			entrydata.put(key, value);
			System.out.println("put " + key + "x" + value);
		}

		@Override
		public String toString() {
			return "type " + type + (type == ENTRIES ? " entry " + entrydata : " table " + tabledata) + "\n";
		}

		public void setType(int type) {
			this.type = type;

		}

		public void addTable(Hashtable<String, String> tablecont) {
			tabledata.add(tablecont);

		}

	}

	Hashtable<String, TableContent> storage = new Hashtable<String, TableContent>();

	private void trace(String message) {

		container.getTrace().addInfo(message);
	}

	Container container;

	private void setTrace(Container container) {
		this.container = container;
	}

	private String parseRequest(InputStream is, String prefix) {
		try {
			TeeInputStream tee = new TeeInputStream(is);
			// Create DOM tree for response
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = docBuilder.parse(tee);
			trace("Response " + tee.getStringContent());
			// trace("RFC Response2: " + tee.getStringContent());
			Node responseNode = document.getFirstChild();
			// contains the main data
			NodeList nodeList = responseNode.getChildNodes();
			// parse level 1

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					String area = node.getNodeName();

					TableContent content = new TableContent();
					NodeList level2 = node.getChildNodes();

					// If it is parameter values then this wil be filled
					// if (node.getFirstChild().getNodeValue()!=null) {
					// trace("AREA " + area);
					// // it is just a plain content type
					// content.setType(TableContent.ENTRIES);
					// content.addEntries("ROOT",
					// node.getFirstChild().getNodeValue()+ "");
					// }// end node parameters

					for (int j = 0; j < level2.getLength(); j++) {
						try {
							Node node2 = level2.item(j);

							if (node2.getNodeType() == Node.ELEMENT_NODE)
								// trace("node2 " + node2.getNodeName());
								// test to see if this is an itemnode or not.
								if (node2.getNodeName().equals("item")) {
									// it is a table
									content.setType(TableContent.TABLES);
									NodeList level3 = node2.getChildNodes();
									Hashtable<String, String> tablecont = new Hashtable<String, String>();
									for (int l3 = 0; l3 < level3.getLength(); l3++) {
										Node node3 = level3.item(l3);
										// trace("leve 3 ");
										try {
											if (node3.getNodeType() == Node.ELEMENT_NODE) {
												tablecont.put(node3.getNodeName(), ""
														+ node3.getFirstChild().getNodeValue());

											}
										} catch (Exception e) {
										}

									}
									content.addTable(tablecont);
								} else if (!node2.getNodeName().equals("#text")) {
									// it is just a plain content type
									content.setType(TableContent.ENTRIES);
									content.addEntries(node2.getNodeName(), node2.getFirstChild().getNodeValue() + "");
								}
						} catch (Exception e) {
							// Ignore if we have fields without any content.
							// trace(e.getMessage());
							// e.printStackTrace();
						}
					}
					if (content.getType() > TableContent.NOTFOUND) {

						// it is just a plain content type
						try {
							content.setType(TableContent.PARAMETER);
							content.addEntries("ROOT", node.getFirstChild().getNodeValue() + "");
						} catch (Exception e) {
						}
					}

					storage.put(prefix + area, content);
				}

			}

		} catch (Exception e) {
			// trace(e.getMessage());
			// e.printStackTrace();
		}
		trace(storage.toString());
		return "OK";
	}

	public static void main(String args[]) {

	}

}
