package org.xhtmlrenderer.css.parser;

import java.util.List;

import org.w3c.dom.css.RGBColor;
import org.xhtmlrenderer.css.constants.CSSValueType;
import org.xhtmlrenderer.css.constants.IdentValue;

public interface PropertyValue
{
	public CSSValueType getPrimitiveTypeN();
	public CSSValueType getCssValueTypeN();
	
	public float getFloatValue();
	public FSFunction getFunction();
	public IdentValue getIdentValue();
	public List<?> getValues();
	public Token getOperator();
	public String[] getStringArrayValue();
	public FSColor getFSColor();
	public RGBColor getRGBColorValue();
	
	public void setIdentValue(IdentValue identValue);
	public void setOperator(Token operatorToken);
	public void setStringArrayValue(String[] strings);
	public short getPropertyValueType();

	public String getFingerprint();
	public String getStringValue();
	public String getCssText();
}
