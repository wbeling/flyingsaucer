package org.xhtmlrenderer.css.parser.property;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.w3c.dom.css.CSSPrimitiveValue;
import org.xhtmlrenderer.css.constants.CSSName;
import org.xhtmlrenderer.css.constants.IdentValue;
import org.xhtmlrenderer.css.parser.CSSParseException;
import org.xhtmlrenderer.css.parser.PropertyValue;
import org.xhtmlrenderer.css.sheet.PropertyDeclaration;
import org.xhtmlrenderer.css.sheet.StylesheetInfo.CSSOrigin;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.util.LangId;

/**
 * Static utility functions to check types, etc for builders to use.
 */
public class BuilderUtil {
	private BuilderUtil() {
	}

	static void cssError(LangId key, Object... args) {
		throw new CSSParseException(String.format(SharedContext.ERRS.get().getString(key.toString()), args), -1);
	}

	static void checkValueCount(CSSName cssName, int expected, int found) {
		if (expected != found)
			cssError(LangId.VALUE_COUNT_MISMATCH, found, cssName, expected);
	}

	static void checkValueCount(CSSName cssName, int min, int max, int found) {
		if (!(found >= min && found <= max))
			cssError(LangId.MIN_MAX_VALUE_COUNT_MISMATCH, found, cssName, min, max);
	}

	static void checkIdentType(CSSName cssName, CSSPrimitiveValue value) {
		if (value.getPrimitiveType() != CSSPrimitiveValue.CSS_IDENT) 
			cssError(LangId.MUST_BE_IDENTIFIER, cssName);
	}

	static void checkIdentOrURIType(CSSName cssName, CSSPrimitiveValue value) {
		int type = value.getPrimitiveType();

		if (type != CSSPrimitiveValue.CSS_IDENT &&
			type != CSSPrimitiveValue.CSS_URI)
			cssError(LangId.MUST_BE_URI_OR_IDENTIFIER, cssName);
	}

	static void checkIdentOrColorType(CSSName cssName, CSSPrimitiveValue value) {
		int type = value.getPrimitiveType();

		if (type != CSSPrimitiveValue.CSS_IDENT
				&& type != CSSPrimitiveValue.CSS_RGBCOLOR)
		cssError(LangId.MUST_BE_COLOR_OR_IDENTIFIER, cssName);
	}

	static void checkIdentOrIntegerType(CSSName cssName, CSSPrimitiveValue value) {
		int type = value.getPrimitiveType();
		if ((type != CSSPrimitiveValue.CSS_IDENT && type != CSSPrimitiveValue.CSS_NUMBER)
			|| (type == CSSPrimitiveValue.CSS_NUMBER &&
			(int) value.getFloatValue(CSSPrimitiveValue.CSS_NUMBER) != 
			Math.round(value.getFloatValue(CSSPrimitiveValue.CSS_NUMBER)))) 
		{
			cssError(LangId.MUST_BE_INT_OR_IDENTIFIER, cssName);
		}
	}

	static void checkInteger(CSSName cssName, CSSPrimitiveValue value) {
		int type = value.getPrimitiveType();
		if (type != CSSPrimitiveValue.CSS_NUMBER
				|| (type == CSSPrimitiveValue.CSS_NUMBER && (int) value
						.getFloatValue(CSSPrimitiveValue.CSS_NUMBER) != Math
						.round(value
								.getFloatValue(CSSPrimitiveValue.CSS_NUMBER)))) {
			cssError(LangId.MUST_BE_INT, cssName);
		}
	}

	static void checkIdentOrLengthType(CSSName cssName, CSSPrimitiveValue value) {
		int type = value.getPrimitiveType();
		if (type != CSSPrimitiveValue.CSS_IDENT && !isLength(value)) {
			cssError(LangId.MUST_BE_LENGTH_OR_IDENTIFIER, cssName);
		}
	}

	static void checkIdentOrNumberType(CSSName cssName, CSSPrimitiveValue value) {
		int type = value.getPrimitiveType();
		if (type != CSSPrimitiveValue.CSS_IDENT
				&& type != CSSPrimitiveValue.CSS_NUMBER) {
			cssError(LangId.MUST_BE_NUMBER_OR_IDENTIFIER, cssName);
		}
	}

	static void checkIdentLengthOrPercentType(CSSName cssName,
			CSSPrimitiveValue value) {
		int type = value.getPrimitiveType();
		if (type != CSSPrimitiveValue.CSS_IDENT && !isLength(value)
				&& type != CSSPrimitiveValue.CSS_PERCENTAGE) {
			cssError(LangId.MUST_BE_LENGTH_PERCENT_OR_IDENTIFIER, cssName);
		}
	}

	static void checkLengthOrPercentType(CSSName cssName,
			CSSPrimitiveValue value) {
		int type = value.getPrimitiveType();
		if (!isLength(value) && type != CSSPrimitiveValue.CSS_PERCENTAGE) {
			cssError(LangId.MUST_BE_LENGTH_OR_PERCENT, cssName);
		}
	}

	static void checkLengthType(CSSName cssName, CSSPrimitiveValue value) {
		if (!isLength(value)) {
			cssError(LangId.MUST_BE_LENGTH, cssName);
		}
	}

	static void checkNumberType(CSSName cssName, CSSPrimitiveValue value) {
		if (value.getPrimitiveType() != CSSPrimitiveValue.CSS_NUMBER) {
			cssError(LangId.MUST_BE_NUMBER, cssName);
		}
	}

	static void checkStringType(CSSName cssName, CSSPrimitiveValue value) {
		if (value.getPrimitiveType() != CSSPrimitiveValue.CSS_STRING) {
			cssError(LangId.MUST_BE_STRING, cssName);
		}
	}

	static void checkIdentOrString(CSSName cssName, CSSPrimitiveValue value) {
		short type = value.getPrimitiveType();
		if (type != CSSPrimitiveValue.CSS_STRING
				&& type != CSSPrimitiveValue.CSS_IDENT) {
			cssError(LangId.MUST_BE_STRING_OR_IDENTIFIER, cssName);
		}
	}

	static void checkIdentLengthNumberOrPercentType(CSSName cssName,
			CSSPrimitiveValue value) {
		int type = value.getPrimitiveType();
		if (type != CSSPrimitiveValue.CSS_IDENT && !isLength(value)
				&& type != CSSPrimitiveValue.CSS_PERCENTAGE
				&& type != CSSPrimitiveValue.CSS_NUMBER) {
			cssError(LangId.MUST_BE_LENGTH_NUMBER_PERCENT_OR_IDENTIFIER, cssName);
		}
	}

	static boolean isLength(CSSPrimitiveValue value) {
		int unit = value.getPrimitiveType();
		return unit == CSSPrimitiveValue.CSS_EMS
				|| unit == CSSPrimitiveValue.CSS_EXS
				|| unit == CSSPrimitiveValue.CSS_PX
				|| unit == CSSPrimitiveValue.CSS_IN
				|| unit == CSSPrimitiveValue.CSS_CM
				|| unit == CSSPrimitiveValue.CSS_MM
				|| unit == CSSPrimitiveValue.CSS_PT
				|| unit == CSSPrimitiveValue.CSS_PC
				|| (unit == CSSPrimitiveValue.CSS_NUMBER && value
						.getFloatValue(CSSPrimitiveValue.CSS_IN) == 0.0f);
	}

	static void checkValidity(CSSName cssName, EnumSet<IdentValue> validValues,
			IdentValue value) {
		if (!validValues.contains(value)) {
			cssError(LangId.UNSUPPORTED_IDENTIFIER, value, cssName);
		}
	}

	static IdentValue checkIdent(CSSName cssName, CSSPrimitiveValue value) {
		IdentValue result = IdentValue.fsValueOf(value.getStringValue());
		if (result == null) {
			cssError(LangId.UNRECOGNIZED_IDENTIFIER, value.getStringValue(), cssName);
		}
		((PropertyValue) value).setIdentValue(result);
		return result;
	}

	static PropertyDeclaration copyOf(PropertyDeclaration decl, CSSName newName) {
		return new PropertyDeclaration(newName, decl.getValue(),
				decl.isImportant(), decl.getOrigin());
	}

	static void checkInheritAllowed(CSSPrimitiveValue value,
			boolean inheritAllowed) {
		if (value.getCssValueType() == CSSPrimitiveValue.CSS_INHERIT
				&& !inheritAllowed) {
			cssError(LangId.INVALID_INHERIT);
		}
	}

	static List<PropertyDeclaration> checkInheritAll(CSSName[] all,
			List<PropertyValue> values, CSSOrigin origin, boolean important,
			boolean inheritAllowed) {
		if (values.size() == 1) {
			CSSPrimitiveValue value = values.get(0);
			checkInheritAllowed(value, inheritAllowed);
			if (value.getCssValueType() == CSSPrimitiveValue.CSS_INHERIT) {
				List<PropertyDeclaration> result = new ArrayList<PropertyDeclaration>(
						all.length);
				for (int i = 0; i < all.length; i++) {
					result.add(new PropertyDeclaration(all[i], value,
							important, origin));
				}
				return result;
			}
		}

		return null;
	}
}
