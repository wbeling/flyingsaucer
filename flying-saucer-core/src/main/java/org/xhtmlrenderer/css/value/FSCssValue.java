package org.xhtmlrenderer.css.value;

import java.util.List;

import org.w3c.dom.css.*;
import org.xhtmlrenderer.css.constants.CSSValueType;
import org.xhtmlrenderer.css.constants.IdentValue;
import org.xhtmlrenderer.css.parser.FSColor;
import org.xhtmlrenderer.css.parser.FSFunction;
import org.xhtmlrenderer.css.parser.PropertyValue;
import org.xhtmlrenderer.css.parser.Token;
import org.xhtmlrenderer.util.XRRuntimeException;


/**
 * Implementation of a {@link org.w3c.dom.css.CSSPrimitiveValue}. The main
 * feature of this class is that on construction, values will be "normalized",
 * so that color idents (such as 'black') are converted to valid java.awt.Color
 * strings, and other idents are resolved as possible.
 *
 * @author empty
 */
public class FSCssValue implements PropertyValue {
    /** */
    //not used private String propName;
    /** Description of the Field */
    //not used private CSSName cssName;
    /**
     * Description of the Field
     */
    private String _cssText;
    /**
     * Description of the Field
     */
    private Counter counter;
    /**
     * Description of the Field
     */
    private float floatValue;
    /**
     * Description of the Field
     */
    private CSSValueType primitiveType;
    /**
     * Description of the Field
     */
    private Rect rectValue;
    /**
     * Description of the Field
     */
    private RGBColor rgbColorValue;

    /**
     * Constructor for the FSCssValue object
     *
     * @param primitive PARAM
     */
    public FSCssValue(PropertyValue primitive) {
        //not used this.cssName = cssName;
        //not used this.propName = cssName.toString();
        this.primitiveType = primitive.getPrimitiveTypeN();
        this._cssText = (primitiveType == CSSValueType.CSS_STRING ?
                primitive.getStringValue() :
                primitive.getCssText());

        // TODO
        // access on these values is not correctly supported in this class
        // right now. would need a switch/case on primitive type
        // as the getZZZ will fail if not the corresponding type
        // e.g. getCounterValue() fails if not actually a counter
        // (PWW 19-11-04)
        //this.floatValue = primitive.getFloatValue( primitiveType );

        // convert type as necessary
        switch (primitiveType) {
            case CSS_RGBCOLOR:
                this.rgbColorValue = primitive.getRGBColorValue();
                break;
            case CSS_IDENT:
                break;
            case CSS_STRING:
                // ASK: do we need this? not clear when a CSS_STRING is meaningful (PWW 24-01-05)
                break;
            case CSS_COUNTER:
                this.counter = primitive.getCounterValue();
                break;
            case CSS_RECT:
                this.rectValue = primitive.getRectValue();
                break;
            case CSS_URI:
                this._cssText = primitive.getStringValue();
                break;
            case CSS_IN:
                // fall-thru
            case CSS_CM:
                // fall-thru
            case CSS_EMS:
                // fall-thru
            case CSS_EXS:
                // fall-thru
            case CSS_MM:
                // fall-thru
            case CSS_NUMBER:
                // fall-thru
            case CSS_PC:
                // fall-thru
            case CSS_PERCENTAGE:
                // fall-thru
            case CSS_PT:
                // fall-thru
            case CSS_PX:
                this.floatValue = primitive.getFloatValue(primitiveType);
                break;
            default:
                // leave as is
        }
        if (_cssText == null) {
            throw new XRRuntimeException("CSSText is null for " + primitive + "   csstext " + primitive.getCssText() + "   string value " + primitive.getStringValue());
        }
    }

    /**
     * Use a given CSSPrimitiveValue, with an overriding internal text value
     *
     * @param primitive PARAM
     * @param newValue  PARAM
     */
    public FSCssValue(PropertyValue primitive, String newValue) {
        this(primitive);
        this._cssText = newValue;
    }

    FSCssValue(CSSValueType primitiveType, String value) {
        this.primitiveType = primitiveType;
        this._cssText = value;
    }

    public static FSCssValue getNewIdentValue(String identValue) {
        return new FSCssValue(CSSValueType.CSS_IDENT, identValue);
    }

    /**
     * Returns the string representation of the instance, in this case, the CSS
     * text value.
     *
     * @return A string representation of the object.
     */
    public String toString() {
        return getCssText();
    }

    /**
     * Not supported, class is immutable. Sets the string representation of the
     * current value.
     *
     * @param cssText The new cssText value
     */
    public void setCssText(String cssText) {
        this._cssText = cssText;
    }

    /**
     * Not supported, class is immutable. A method to set the float value with a
     * specified unit.
     *
     * @param unitType   The new floatValue value
     * @param floatValue The new floatValue value
     */
    public void setFloatValue(short unitType, float floatValue) {
        throw new XRRuntimeException("FSCssValue is immutable.");
    }

    /**
     * Not supported, class is immutable. A method to set the string value with
     * the specified unit.
     *
     * @param stringType  The new stringValue value
     * @param stringValue The new stringValue value
     */
    public void setStringValue(short stringType, String stringValue) {
        throw new XRRuntimeException("FSCssValue is immutable.");
    }

    /**
     * Gets the propName attribute of the FSCssValue object
     *
     * @return   The propName value
     */
    /*public String getPropName() {
        return propName;
    } tobe deleted: not used*/

    /**
     * Gets the cssName attribute of the FSCssValue object
     *
     * @return   The cssName value
     */
    /*public CSSName getCssName() {
        return cssName;
    } tobe deleted: not used */

    /**
     * A string representation of the current value.
     *
     * @return The _cssText value
     */
    public String getCssText() {
        return this._cssText;
    }

    /**
     * A code defining the type of the value as defined above.
     *
     * @return The cssValueType value
     */
    public short getCssValueType() {
        // HACK: we assume that, whatever value we are wrapping, we are, in effect, a single value
        // because shorthand-expansion creates us
        return CSSValue.CSS_PRIMITIVE_VALUE;
    }

    /**
     * Not supported. This method is used to get the Counter value.
     *
     * @return The counterValue value
     */
    public Counter getCounterValue() {
        return counter;
    }

    /**
     * This method is used to get a float value in a specified unit.
     *
     * @param unitType PARAM
     * @return The floatValue value
     */
    public float getFloatValue(short unitType) {
        return floatValue;
    }

    /**
     * The type of the value as defined by the constants specified above.
     *
     * @return The primitiveType value
     */
    @Deprecated
    @Override
    public short getPrimitiveType() {
    	assert(false);
    	return 0;
    }

    /**
     * Not supported. This method is used to get the Rect value.
     *
     * @return The rectValue value
     */
    public Rect getRectValue() {
        return rectValue;
    }

    /**
     * Not supported. This method is used to get the RGB color.
     *
     * @return The rGBColorValue value
     */
    public RGBColor getRGBColorValue() {
        return rgbColorValue;
    }

    /**
     * This method is used to get the string value.
     *
     * @return The stringValue value
     */
    public String getStringValue() {
        return this._cssText;
    }

	@Override
	public CSSValueType getPrimitiveTypeN() {
		return primitiveType;
	}

	@Override
	public float getFloatValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public FSFunction getFunction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IdentValue getIdentValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<?> getValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIdentValue(IdentValue identValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOperator(Token operatorToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FSColor getFSColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Token getOperator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getFloatValue(CSSValueType cssNumber) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setStringArrayValue(String[] strings) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public short getPropertyValueType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getFingerprint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getStringArrayValue() {
		// TODO Auto-generated method stub
		return null;
	}
}
