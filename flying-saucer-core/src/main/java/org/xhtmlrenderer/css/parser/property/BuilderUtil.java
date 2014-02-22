package org.xhtmlrenderer.css.parser.property;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;

import org.w3c.dom.css.CSSPrimitiveValue;
import org.xhtmlrenderer.css.constants.CSSName;
import org.xhtmlrenderer.css.constants.CSSValueType;
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

	public static void checkIdentType(CSSName cssName, PropertyValue value) {
		if (value.getPrimitiveTypeN() != CSSValueType.CSS_IDENT) 
			cssThrowError(LangId.MUST_BE_IDENTIFIER, cssName);
	}

	public static void checkIdentOrURIType(CSSName cssName, PropertyValue value) {
		CSSValueType type = value.getPrimitiveTypeN();

		if (type != CSSValueType.CSS_IDENT &&
			type != CSSValueType.CSS_URI)
			cssThrowError(LangId.MUST_BE_URI_OR_IDENTIFIER, cssName);
	}

	public static void checkIdentOrColorType(CSSName cssName, PropertyValue value) {
		CSSValueType type = value.getPrimitiveTypeN();

		if (type != CSSValueType.CSS_IDENT
			&& type != CSSValueType.CSS_RGBCOLOR)
		cssThrowError(LangId.MUST_BE_COLOR_OR_IDENTIFIER, cssName);
	}

	public static void checkIdentOrIntegerType(CSSName cssName, PropertyValue value) {
		CSSValueType type = value.getPrimitiveTypeN();
		if ((type != CSSValueType.CSS_IDENT && type != CSSValueType.CSS_NUMBER)
			|| (type == CSSValueType.CSS_NUMBER &&
			(int) value.getFloatValue(CSSValueType.CSS_NUMBER) != 
			Math.round(value.getFloatValue(CSSValueType.CSS_NUMBER)))) 
		{
			cssThrowError(LangId.MUST_BE_INT_OR_IDENTIFIER, cssName);
		}
	}

	public static void checkInteger(CSSName cssName, PropertyValue value) {
		CSSValueType type = value.getPrimitiveTypeN();
		if (type != CSSValueType.CSS_NUMBER ||
		   (type == CSSValueType.CSS_NUMBER && 
		    value.getFloatValue(CSSValueType.CSS_NUMBER) % 1f != 0f))
		{
			cssThrowError(LangId.MUST_BE_INT, cssName);
		}
	}

	public static void checkIdentOrLengthType(CSSName cssName, PropertyValue value) {
		CSSValueType type = value.getPrimitiveTypeN();
		if (type != CSSValueType.CSS_IDENT && !isLength(value)) {
			cssThrowError(LangId.MUST_BE_LENGTH_OR_IDENTIFIER, cssName);
		}
	}

	public static void checkIdentOrNumberType(CSSName cssName, PropertyValue value) {
		CSSValueType type = value.getPrimitiveTypeN();
		if (type != CSSValueType.CSS_IDENT
				&& type != CSSValueType.CSS_NUMBER) {
			cssThrowError(LangId.MUST_BE_NUMBER_OR_IDENTIFIER, cssName);
		}
	}

	public static void checkIdentLengthOrPercentType(CSSName cssName,
			PropertyValue value) {
		CSSValueType type = value.getPrimitiveTypeN();
		if (type != CSSValueType.CSS_IDENT && !isLength(value)
				&& type != CSSValueType.CSS_PERCENTAGE) {
			cssThrowError(LangId.MUST_BE_LENGTH_PERCENT_OR_IDENTIFIER, cssName);
		}
	}

	public static void checkLengthOrPercentType(CSSName cssName,
			PropertyValue value) {
		CSSValueType type = value.getPrimitiveTypeN();
		if (!isLength(value) && type != CSSValueType.CSS_PERCENTAGE) {
			cssThrowError(LangId.MUST_BE_LENGTH_OR_PERCENT, cssName);
		}
	}

	public static void checkLengthType(CSSName cssName, PropertyValue value) {
		if (!isLength(value)) {
			cssThrowError(LangId.MUST_BE_LENGTH, cssName);
		}
	}

	public static void checkNumberType(CSSName cssName, PropertyValue value) {
		if (value.getPrimitiveTypeN() != CSSValueType.CSS_NUMBER) {
			cssThrowError(LangId.MUST_BE_NUMBER, cssName);
		}
	}

	public static void checkStringType(CSSName cssName, PropertyValue value) {
		if (value.getPrimitiveTypeN() != CSSValueType.CSS_STRING) {
			cssThrowError(LangId.MUST_BE_STRING, cssName);
		}
	}

	public static void checkIdentOrString(CSSName cssName, PropertyValue value) {
		CSSValueType type = value.getPrimitiveTypeN();
		if (type != CSSValueType.CSS_STRING
				&& type != CSSValueType.CSS_IDENT) {
			cssThrowError(LangId.MUST_BE_STRING_OR_IDENTIFIER, cssName);
		}
	}

	public static void checkIdentLengthNumberOrPercentType(CSSName cssName,
			PropertyValue value) {
		CSSValueType type = value.getPrimitiveTypeN();
		if (type != CSSValueType.CSS_IDENT && !isLength(value)
				&& type != CSSValueType.CSS_PERCENTAGE
				&& type != CSSValueType.CSS_NUMBER) {
			cssThrowError(LangId.MUST_BE_LENGTH_NUMBER_PERCENT_OR_IDENTIFIER, cssName);
		}
	}

	public static boolean isLength(PropertyValue value) {
		CSSValueType unit = value.getPrimitiveTypeN();
		return unit == CSSValueType.CSS_EMS
				|| unit == CSSValueType.CSS_EXS
				|| unit == CSSValueType.CSS_PX
				|| unit == CSSValueType.CSS_IN
				|| unit == CSSValueType.CSS_CM
				|| unit == CSSValueType.CSS_MM
				|| unit == CSSValueType.CSS_PT
				|| unit == CSSValueType.CSS_PC
				|| (unit == CSSValueType.CSS_NUMBER && value
					.getFloatValue(CSSValueType.CSS_IN) == 0.0f);
	}

	public static void checkValidity(CSSName cssName, EnumSet<IdentValue> validValues,
			IdentValue value) {
		if (!validValues.contains(value)) {
			cssThrowError(LangId.UNSUPPORTED_IDENTIFIER, value, cssName);
		}
	}

	public static IdentValue checkIdent(CSSName cssName, PropertyValue value) {
		IdentValue result = IdentValue.fsValueOf(value.getStringValue());
		if (result == null) {
			cssThrowError(LangId.UNRECOGNIZED_IDENTIFIER, value.getStringValue(), cssName);
		}
		value.setIdentValue(result);
		return result;
	}

	public static PropertyDeclaration copyOf(PropertyDeclaration decl, CSSName newName) {
		return new PropertyDeclaration(newName, decl.getValue(),
				decl.isImportant(), decl.getOrigin());
	}

	public static void checkInheritAllowed(PropertyValue value,
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
			PropertyValue value = values.get(0);
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
