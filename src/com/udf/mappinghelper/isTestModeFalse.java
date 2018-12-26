package com.udf.mappinghelper;

import com.sap.aii.mapping.api.*;
import com.sap.aii.mappingtool.tf7.rt.*;

public class isTestModeFalse {

	public String isTestModeFalse(Container container) throws StreamTransformationException {

		// Returns false if MessageId is empty, otherwise true

		// Use to avoid errors in mapping steps that throw exceptions when
		// exectuted in ESB,
		// like DynConf, BIC communication etc.

		// Make these mapping steps conditional so they are executed only if
		// isTestModeFalse returns true

		// The function was named isTestModeFalse instead of just isTestMode
		// because it will probably always
		// be used in connection with something you wish to skip if this is test
		// mode ( test in ESB ). This way we
		// do not have to insert a NOT operator after the function. All we need
		// is typically just to insert an ifWithoutElse
		// as last step in the mapping, with isThisTestModeFalse as input for
		// the IF and the "troublemaker" mapping
		// as input for the THEN.

		if (container.getInputHeader().getMessageId().equals(""))
			return "false";
		else
			return "true";

	}

}
