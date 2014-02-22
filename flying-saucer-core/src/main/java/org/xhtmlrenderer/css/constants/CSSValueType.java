package org.xhtmlrenderer.css.constants;


/**
 * Aims to enumerate all CSS unit types. From Webkit, not all units
 * are supported by Flyingsaucer.
 */
public enum CSSValueType
{
	CSS_ATTR,
	CSS_CM,
	CSS_COUNTER,
	CSS_DEG,
	CSS_DIMENSION,
	CSS_EMS,
	CSS_EXS,
	CSS_GRAD,
	CSS_HZ,
	CSS_IDENT,
	CSS_IN,
	CSS_KHZ,
	CSS_MM,
	CSS_MS,
	CSS_NUMBER,
	CSS_PC,
	CSS_PERCENTAGE,
	CSS_PT,
	CSS_PX,
	CSS_RAD,
	CSS_RECT,
	CSS_RGBCOLOR,
	CSS_S,
	CSS_STRING,
	CSS_UNKNOWN,
	CSS_URI,

	// From CSS Values and Units. Viewport-percentage Lengths (vw/vh/vmin/vmax).
    CSS_VW,
    CSS_VH,
    CSS_VMIN,
    CSS_VMAX,
    CSS_DPPX,
    CSS_DPI,
    CSS_DPCM,
    CSS_FR,
    CSS_PAIR, // We envision this being exposed as a means of getting computed style values for pairs (border-spacing/radius, background-position, etc.)
    CSS_UNICODE_RANGE,

    // These are from CSS3 Values and Units, but that isn't a finished standard yet
    CSS_TURN,
    CSS_REMS,
    CSS_CHS,

    // This is used by the CSS Shapes draft
    CSS_SHAPE,

    // Used by border images.
    CSS_QUAD,

    CSS_CALC,
    CSS_CALC_PERCENTAGE_WITH_NUMBER,
    CSS_CALC_PERCENTAGE_WITH_LENGTH,

    CSS_PROPERTY_ID,
    CSS_VALUE_ID,
    
    // TODO: These should go into a separate enum
    // as they are types of types.
    CSS_INHERIT,
    CSS_CUSTOM,
    CSS_PRIMITIVE_VALUE,
    CSS_VALUE_LIST;
}
