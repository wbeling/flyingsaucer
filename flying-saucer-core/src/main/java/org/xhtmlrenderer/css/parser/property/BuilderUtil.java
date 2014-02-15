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

/**
 * Static utility functions to check types, etc for builders to use.
 */
public class BuilderUtil
{
		private BuilderUtil() {}
	
		static void checkValueCount(CSSName cssName, int expected, int found) {
	        if (expected != found) {
	            throw new CSSParseException("Found " + found + " value(s) for " +
	                    cssName + " when " + expected + " value(s) were expected", -1);
	        }
	    }
	    
	    static void checkValueCount(CSSName cssName, int min, int max, int found) {
	        if (! (found >= min && found <= max)) {
	            throw new CSSParseException("Found " + found + " value(s) for " +
	                    cssName + " when between " + min + " and " + max + " value(s) were expected", -1);
	        }
	    }
	    
	    static void checkIdentType(CSSName cssName, CSSPrimitiveValue value) {
	        if (value.getPrimitiveType() != CSSPrimitiveValue.CSS_IDENT) {
	            throw new CSSParseException("Value for " + cssName + " must be an identifier", -1);
	        }
	    }
	    
	    static void checkIdentOrURIType(CSSName cssName, CSSPrimitiveValue value) {
	        int type = value.getPrimitiveType();
	        if (type != CSSPrimitiveValue.CSS_IDENT && type != CSSPrimitiveValue.CSS_URI) {
	            throw new CSSParseException("Value for " + cssName + " must be an identifier or a URI", -1);
	        }
	    }
	    
	    static void checkIdentOrColorType(CSSName cssName, CSSPrimitiveValue value) {
	        int type = value.getPrimitiveType();
	        if (type != CSSPrimitiveValue.CSS_IDENT && type != CSSPrimitiveValue.CSS_RGBCOLOR) {
	            throw new CSSParseException("Value for " + cssName + " must be an identifier or a color", -1);
	        }
	    }  
	    
	    static void checkIdentOrIntegerType(CSSName cssName, CSSPrimitiveValue value) {
	        int type = value.getPrimitiveType();
	        if ((type != CSSPrimitiveValue.CSS_IDENT && 
	                type != CSSPrimitiveValue.CSS_NUMBER) || 
	            (type == CSSPrimitiveValue.CSS_NUMBER && 
	                    (int)value.getFloatValue(CSSPrimitiveValue.CSS_NUMBER) !=
	                        Math.round(value.getFloatValue(CSSPrimitiveValue.CSS_NUMBER)))) {
	            throw new CSSParseException("Value for " + cssName + " must be an identifier or an integer", -1);
	        }
	    }
	    
	    static void checkInteger(CSSName cssName, CSSPrimitiveValue value) {
	        int type = value.getPrimitiveType();
	        if (type != CSSPrimitiveValue.CSS_NUMBER || 
	                (type == CSSPrimitiveValue.CSS_NUMBER && 
	                    (int)value.getFloatValue(CSSPrimitiveValue.CSS_NUMBER) !=
	                        Math.round(value.getFloatValue(CSSPrimitiveValue.CSS_NUMBER)))) {
	            throw new CSSParseException("Value for " + cssName + " must be an integer", -1);
	        }
	    }
	    
	    static void checkIdentOrLengthType(CSSName cssName, CSSPrimitiveValue value) {
	        int type = value.getPrimitiveType();
	        if (type != CSSPrimitiveValue.CSS_IDENT && ! isLength(value)) {
	            throw new CSSParseException("Value for " + cssName + " must be an identifier or a length", -1);
	        }
	    }
	    
	    static void checkIdentOrNumberType(CSSName cssName, CSSPrimitiveValue value) {
	        int type = value.getPrimitiveType();
	        if (type != CSSPrimitiveValue.CSS_IDENT && type != CSSPrimitiveValue.CSS_NUMBER) {
	            throw new CSSParseException("Value for " + cssName + " must be an identifier or a length", -1);
	        }
	    }
	    
	    static void checkIdentLengthOrPercentType(CSSName cssName, CSSPrimitiveValue value) {
	        int type = value.getPrimitiveType();
	        if (type != CSSPrimitiveValue.CSS_IDENT && ! isLength(value) && type != CSSPrimitiveValue.CSS_PERCENTAGE) {
	            throw new CSSParseException("Value for " + cssName + " must be an identifier, length, or percentage", -1);
	        }
	    }
	    
	    static void checkLengthOrPercentType(CSSName cssName, CSSPrimitiveValue value) {
	        int type = value.getPrimitiveType();
	        if (! isLength(value) && type != CSSPrimitiveValue.CSS_PERCENTAGE) {
	            throw new CSSParseException("Value for " + cssName + " must be a length or percentage", -1);
	        }
	    }
	    
	    static void checkLengthType(CSSName cssName, CSSPrimitiveValue value) {
	        if (! isLength(value)) {
	            throw new CSSParseException("Value for " + cssName + " must be a length", -1);
	        }
	    }
	    
	    static void checkNumberType(CSSName cssName, CSSPrimitiveValue value) {
	        if (value.getPrimitiveType() != CSSPrimitiveValue.CSS_NUMBER) {
	            throw new CSSParseException("Value for " + cssName + " must be a number", -1);
	        }
	    }
	    
	    static void checkStringType(CSSName cssName, CSSPrimitiveValue value) {
	        if (value.getPrimitiveType() != CSSPrimitiveValue.CSS_STRING) {
	            throw new CSSParseException("Value for " + cssName + " must be a string", -1);
	        }
	    }
	    
	    static void checkIdentOrString(CSSName cssName, CSSPrimitiveValue value) {
	        short type = value.getPrimitiveType();
	        if (type != CSSPrimitiveValue.CSS_STRING && type != CSSPrimitiveValue.CSS_IDENT) {
	            throw new CSSParseException("Value for " + cssName + " must be an identifier or string", -1);
	        }
	    }
	    
	    static void checkIdentLengthNumberOrPercentType(CSSName cssName, CSSPrimitiveValue value) {
	        int type = value.getPrimitiveType();
	        if (type != CSSPrimitiveValue.CSS_IDENT && 
	                ! isLength(value) && 
	                type != CSSPrimitiveValue.CSS_PERCENTAGE &&
	                type != CSSPrimitiveValue.CSS_NUMBER) {
	            throw new CSSParseException("Value for " + cssName + " must be an identifier, length, or percentage", -1);
	        }
	    }
	    
	    static boolean isLength(CSSPrimitiveValue value) {
	        int unit = value.getPrimitiveType();
	        return unit == CSSPrimitiveValue.CSS_EMS || unit == CSSPrimitiveValue.CSS_EXS
	                || unit == CSSPrimitiveValue.CSS_PX || unit == CSSPrimitiveValue.CSS_IN
	                || unit == CSSPrimitiveValue.CSS_CM || unit == CSSPrimitiveValue.CSS_MM
	                || unit == CSSPrimitiveValue.CSS_PT || unit == CSSPrimitiveValue.CSS_PC
	                || (unit == CSSPrimitiveValue.CSS_NUMBER && value.getFloatValue(CSSPrimitiveValue.CSS_IN) == 0.0f);
	    }
	    
	    static void checkValidity(CSSName cssName, EnumSet<IdentValue> validValues, IdentValue value) {
	        if (! validValues.contains(value)) {
	            throw new CSSParseException("Ident " + value + " is an invalid or unsupported value for " + cssName, -1);
	        }
	    }
	    
	    static IdentValue checkIdent(CSSName cssName, CSSPrimitiveValue value) {
	        IdentValue result = IdentValue.fsValueOf(value.getStringValue());
	        if (result == null) {
	            throw new CSSParseException("Value " + value.getStringValue() + " is not a recognized identifier", -1);
	        }
	        ((PropertyValue)value).setIdentValue(result);
	        return result;
	    }
	    
	    static PropertyDeclaration copyOf(PropertyDeclaration decl, CSSName newName) {
	        return new PropertyDeclaration(newName, decl.getValue(), decl.isImportant(), decl.getOrigin());
	    }
	    
	    static void checkInheritAllowed(CSSPrimitiveValue value, boolean inheritAllowed) {
	        if (value.getCssValueType() == CSSPrimitiveValue.CSS_INHERIT && ! inheritAllowed) {
	            throw new CSSParseException("Invalid use of inherit", -1);
	        }
	    }

	    static List<PropertyDeclaration> checkInheritAll(CSSName[] all, List<PropertyValue> values, CSSOrigin origin, boolean important, boolean inheritAllowed) {
	        if (values.size() == 1) {
	            CSSPrimitiveValue value = (CSSPrimitiveValue)values.get(0);
	            checkInheritAllowed(value, inheritAllowed);
	            if (value.getCssValueType() == CSSPrimitiveValue.CSS_INHERIT) {
	                List<PropertyDeclaration> result = new ArrayList<PropertyDeclaration>(all.length);
	                for (int i = 0; i < all.length; i++) {
	                    result.add(
	                            new PropertyDeclaration(all[i], value, important, origin));
	                }
	                return result;
	            }
	        }
	        
	        return null;
	    }    
}
