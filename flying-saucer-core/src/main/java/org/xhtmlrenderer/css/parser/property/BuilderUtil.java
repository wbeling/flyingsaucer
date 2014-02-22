package org.xhtmlrenderer.css.parser.property;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;

import org.w3c.dom.css.CSSPrimitiveValue;
import org.xhtmlrenderer.css.constants.CSSName;
import org.xhtmlrenderer.css.constants.IdentValue;
import org.xhtmlrenderer.css.parser.CSSParseException;
import org.xhtmlrenderer.css.parser.PropertyValue;
import org.xhtmlrenderer.css.sheet.PropertyDeclaration;
import org.xhtmlrenderer.css.sheet.StylesheetInfo.CSSOrigin;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.util.LangId;
import org.xhtmlrenderer.util.XRLog;

/**
 * Static utility functions to check types, etc for builders to use.
 */
public class BuilderUtil {
	private BuilderUtil() {
	}

	public static void cssThrowError(LangId key, Object... args) 
	{
		String msg = String.format(SharedContext.ERRS.get().getString(key.toString()), args);
		SharedContext.USER_ERRORS.get().add(msg);
		throw new CSSParseException(msg, -1);
	}

	public static void cssNoThrowError(LangId key, Object... args) 
	{
		String msg = String.format(SharedContext.ERRS.get().getString(key.toString()), args);
		SharedContext.USER_ERRORS.get().add(msg);
		XRLog.cssParse(Level.WARNING, msg);
	}
	
	public static void checkValueCount(CSSName cssName, int expected, int found) {
		if (expected != found)
			cssThrowError(LangId.VALUE_COUNT_MISMATCH, found, cssName, expected);
	}

	public static void checkValueCount(CSSName cssName, int min, int max, int found) {
		if (!(found >= min && found <= max))
			cssThrowError(LangId.MIN_MAX_VALUE_COUNT_MISMATCH, found, cssName, min, max);
	}

	public static void checkIdentType(CSSName cssName, CSSPrimitiveValue value) {
		if (value.getPrimitiveType() != CSSPrimitiveValue.CSS_IDENT) 
			cssThrowError(LangId.MUST_BE_IDENTIFIER, cssName);
	}

	public static void checkIdentOrURIType(CSSName cssName, CSSPrimitiveValue value) {
		int type = value.getPrimitiveType();

		if (type != CSSPrimitiveValue.CSS_IDENT &&
			type != CSSPrimitiveValue.CSS_URI)
			cssThrowError(LangId.MUST_BE_URI_OR_IDENTIFIER, cssName);
	}

	public static void checkIdentOrColorType(CSSName cssName, CSSPrimitiveValue value) {
		int type = value.getPrimitiveType();

		if (type != CSSPrimitiveValue.CSS_IDENT
				&& type != CSSPrimitiveValue.CSS_RGBCOLOR)
		cssThrowError(LangId.MUST_BE_COLOR_OR_IDENTIFIER, cssName);
	}

	public static void checkIdentOrIntegerType(CSSName cssName, CSSPrimitiveValue value) {
		int type = value.getPrimitiveType();
		if ((type != CSSPrimitiveValue.CSS_IDENT && type != CSSPrimitiveValue.CSS_NUMBER)
			|| (type == CSSPrimitiveValue.CSS_NUMBER &&
			(int) value.getFloatValue(CSSPrimitiveValue.CSS_NUMBER) != 
			Math.round(value.getFloatValue(CSSPrimitiveValue.CSS_NUMBER)))) 
		{
			cssThrowError(LangId.MUST_BE_INT_OR_IDENTIFIER, cssName);
		}
	}

	public static void checkInteger(CSSName cssName, CSSPrimitiveValue value) {
		int type = value.getPrimitiveType();
		if (type != CSSPrimitiveValue.CSS_NUMBER
				|| (type == CSSPrimitiveValue.CSS_NUMBER && (int) value
						.getFloatValue(CSSPrimitiveValue.CSS_NUMBER) != Math
						.round(value
								.getFloatValue(CSSPrimitiveValue.CSS_NUMBER)))) {
			cssThrowError(LangId.MUST_BE_INT, cssName);
		}
	}

	public static void checkIdentOrLengthType(CSSName cssName, CSSPrimitiveValue value) {
		int type = value.getPrimitiveType();
		if (type != CSSPrimitiveValue.CSS_IDENT && !isLength(value)) {
			cssThrowError(LangId.MUST_BE_LENGTH_OR_IDENTIFIER, cssName);
		}
	}

	public static void checkIdentOrNumberType(CSSName cssName, CSSPrimitiveValue value) {
		int type = value.getPrimitiveType();
		if (type != CSSPrimitiveValue.CSS_IDENT
				&& type != CSSPrimitiveValue.CSS_NUMBER) {
			cssThrowError(LangId.MUST_BE_NUMBER_OR_IDENTIFIER, cssName);
		}
	}

	public static void checkIdentLengthOrPercentType(CSSName cssName,
			CSSPrimitiveValue value) {
		int type = value.getPrimitiveType();
		if (type != CSSPrimitiveValue.CSS_IDENT && !isLength(value)
				&& type != CSSPrimitiveValue.CSS_PERCENTAGE) {
			cssThrowError(LangId.MUST_BE_LENGTH_PERCENT_OR_IDENTIFIER, cssName);
		}
	}

	public static void checkLengthOrPercentType(CSSName cssName,
			CSSPrimitiveValue value) {
		int type = value.getPrimitiveType();
		if (!isLength(value) && type != CSSPrimitiveValue.CSS_PERCENTAGE) {
			cssThrowError(LangId.MUST_BE_LENGTH_OR_PERCENT, cssName);
		}
	}

	public static void checkLengthType(CSSName cssName, CSSPrimitiveValue value) {
		if (!isLength(value)) {
			cssThrowError(LangId.MUST_BE_LENGTH, cssName);
		}
	}

	public static void checkNumberType(CSSName cssName, CSSPrimitiveValue value) {
		if (value.getPrimitiveType() != CSSPrimitiveValue.CSS_NUMBER) {
			cssThrowError(LangId.MUST_BE_NUMBER, cssName);
		}
	}

	public static void checkStringType(CSSName cssName, CSSPrimitiveValue value) {
		if (value.getPrimitiveType() != CSSPrimitiveValue.CSS_STRING) {
			cssThrowError(LangId.MUST_BE_STRING, cssName);
		}
	}

	public static void checkIdentOrString(CSSName cssName, CSSPrimitiveValue value) {
		short type = value.getPrimitiveType();
		if (type != CSSPrimitiveValue.CSS_STRING
				&& type != CSSPrimitiveValue.CSS_IDENT) {
			cssThrowError(LangId.MUST_BE_STRING_OR_IDENTIFIER, cssName);
		}
	}

	public static void checkIdentLengthNumberOrPercentType(CSSName cssName,
			CSSPrimitiveValue value) {
		int type = value.getPrimitiveType();
		if (type != CSSPrimitiveValue.CSS_IDENT && !isLength(value)
				&& type != CSSPrimitiveValue.CSS_PERCENTAGE
				&& type != CSSPrimitiveValue.CSS_NUMBER) {
			cssThrowError(LangId.MUST_BE_LENGTH_NUMBER_PERCENT_OR_IDENTIFIER, cssName);
		}
	}

	public static boolean isLength(CSSPrimitiveValue value) {
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

	public static void checkValidity(CSSName cssName, EnumSet<IdentValue> validValues,
			IdentValue value) {
		if (!validValues.contains(value)) {
			cssThrowError(LangId.UNSUPPORTED_IDENTIFIER, value, cssName);
		}
	}

	public static IdentValue checkIdent(CSSName cssName, CSSPrimitiveValue value) {
		IdentValue result = IdentValue.fsValueOf(value.getStringValue());
		if (result == null) {
			cssThrowError(LangId.UNRECOGNIZED_IDENTIFIER, value.getStringValue(), cssName);
		}
		((PropertyValue) value).setIdentValue(result);
		return result;
	}

	public static PropertyDeclaration copyOf(PropertyDeclaration decl, CSSName newName) {
		return new PropertyDeclaration(newName, decl.getValue(),
				decl.isImportant(), decl.getOrigin());
	}

	public static void checkInheritAllowed(CSSPrimitiveValue value,
			boolean inheritAllowed) {
		if (value.getCssValueType() == CSSPrimitiveValue.CSS_INHERIT
				&& !inheritAllowed) {
			cssThrowError(LangId.INVALID_INHERIT);
		}
	}

	public static List<PropertyDeclaration> checkInheritAll(CSSName[] all,
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
