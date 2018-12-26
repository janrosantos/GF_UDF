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

public class mapWithDefautAdvanced {
	public void mapByDefAdv(String[] q1, String[] q2, String[] q3, ResultList result_q1, ResultList result_q2,
			Container container) throws StreamTransformationException {

		// q1 rule: if node is present, q1 field MUST be present
		// q2 rule: if node is present, q2 field CAN be present. If not, a blank
		// will be inserted
		//
		// q1 = e.g. e1edp19/qualf
		// q2 = e.g. e1edp19/ktext
		//
		// example input:
		// ---------------------------------------------------------------------------------------------------------
		// pos 10 -- qualf10_1 -- value10_1
		// qualf10_2 -- <not present>
		// qualf10_3 -- value10_3
		// pos 20 -- qualf20_1 -- <value element not present>
		// pos 30 -- <node not present>
		// pos 40 -- qualf40_1 -- <value40_1>
		//
		// example output (both queues synced to match structure of argument
		// <q3>) :
		// ---------------------------------------------------------------------------------------------------------
		// result_q1: qualf10_1 qualf10_2 qualf10_3 <CC> qualf20_1 <CC> [blank]
		// <CC> qualf40_1
		// result_q2: value10_1 [blank] value10_3 <CC> [blank] <CC> [blank] <CC>
		// value40_1

		boolean displayTrace = false;
		boolean doExecute = true;

		int ix1 = 0;
		int ix2 = 0;
		int j = 0;
		String[] temp1 = new String[9999]; // Changed from 1000 to 9999 by Mauri
		// 20.09.2013, to be tested by
		// Christian
		String[] temp2 = new String[9999];// Changed from 1000 to 9999 by Mauri
		// 20.09.2013, to be tested by
		// Christian

		MappingTrace trace;
		trace = container.getTrace();

		if (displayTrace) {
			trace.addInfo("mapByDefAdv:");
			trace.addInfo("q1.length=" + q1.length);
			trace.addInfo("q2.length=" + q2.length);
			trace.addInfo("q3.length=" + q3.length);
			trace.addInfo("+++++++++++++++++++++++++++++++++++++++++");
			for (int i = 0; i < q1.length; i++) {
				if (!q1[i].equals(ResultList.CC))
					trace.addInfo(q1[i]);
				if (q1[i].equals(ResultList.CC))
					trace.addInfo("---------- CC ----------");
			}
			trace.addInfo("+++++++++++++++++++++++++++++++++++++++++");
			for (int i = 0; i < q2.length; i++) {
				if (!q2[i].equals(ResultList.CC))
					trace.addInfo(q2[i]);
				if (q2[i].equals(ResultList.CC))
					trace.addInfo("---------- CC ----------");
			}
			trace.addInfo("+++++++++++++++++++++++++++++++++++++++++");
			for (int i = 0; i < q3.length; i++) {
				if (!q3[i].equals(ResultList.CC))
					trace.addInfo(q3[i]);
				if (q3[i].equals(ResultList.CC))
					trace.addInfo("---------- CC ----------");
			}
		}

		// step #1:
		// fill in the blanks in q2. This is exactly like a standard
		// mapByDefault operation
		// at the same time context changes are filtered out
		//
		ix1 = 0;
		j = 0;
		if (doExecute) {
			for (int i = 0; i < q1.length; i++) {
				if (displayTrace) {
					trace.addInfo("loop entry:: i=" + i + " // ix1=" + ix1 + " // j=" + j);
				}
				if (!q1[i].equals(ResultList.CC)) {
					temp1[ix1] = q1[i];
					if (displayTrace)
						trace.addInfo("tilføjer nu element " + i + " fra q1 til temp1 element " + ix1);
					if (j == q2.length) {
						temp2[ix1] = "";
						if (displayTrace)
							trace.addInfo("tilføjer nu blank til temp2 element " + ix1);
					} else {
						if (!q2[j].equals(ResultList.CC)) {
							temp2[ix1] = q2[j];
							if (displayTrace)
								trace.addInfo("tilføjer nu element " + j + " fra q2 til temp2 element " + ix1);
						} else {
							temp2[ix1] = "";
							if (displayTrace)
								trace.addInfo("tilføjer nu blank til temp2 element " + ix1);
						}
					}
					ix1++;
				}
				if (j < q2.length) {
					if (((q1[i].equals(ResultList.CC)) && (q2[j].equals(ResultList.CC)))
							|| ((!q1[i].equals(ResultList.CC)) && (!q2[j].equals(ResultList.CC))))
						j++;
				}

			}
		}

		if (displayTrace) {
			trace.addInfo("queue contents after step #1");
			for (int i = 0; i < temp1.length; i++) {
				if ((temp1[i] != null) || (temp2[i] != null)) {
					trace.addInfo("temp1[" + i + "]=" + temp1[i] + " // " + "temp2[" + i + "]=" + temp2[i]);
				}
			}
		}

		// step #2:
		// apply structure of q3 to q1 and q2
		//
		j = 0;
		for (int i = 0; i < q3.length; i++) {
			if (!q3[i].equals(ResultList.CC)) {
				result_q1.addValue(temp1[j]);
				result_q2.addValue(temp2[j]);
				j++;
			} else {
				if (i < (q3.length - 1)) {
					if (displayTrace)
						trace.addInfo("tilføjer CC. i=" + i);
					result_q1.addValue(ResultList.CC);
					result_q2.addValue(ResultList.CC);
				}
			}
		}

	}

}
