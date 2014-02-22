package org.xhtmlrenderer.css.parser;

import java.util.List;

import org.w3c.dom.css.CSSPrimitiveValue;
import org.xhtmlrenderer.css.constants.CSSValueType;
import org.xhtmlrenderer.css.constants.IdentValue;

public interface PropertyValue extends CSSPrimitiveValue
{
	public CSSValueType getPrimitiveTypeN();
	public float getFloatValue();
	public FSFunction getFunction();
	public IdentValue getIdentValue();
	public List<?> getValues();
	public void setIdentValue(IdentValue identValue);
	public void setOperator(Token operatorToken);
	public FSColor getFSColor();
	public Token getOperator();
	public float getFloatValue(CSSValueType cssNumber);
	public void setStringArrayValue(String[] strings);
	public short getPropertyValueType();
	public String getFingerprint();
	public String[] getStringArrayValue();
}
