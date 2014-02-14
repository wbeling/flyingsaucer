/*
 * {{{ header & license
 * Copyright (c) 2004, 2005 Joshua Marinacci
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * }}}
 */
package org.xhtmlrenderer.render;

import java.awt.BasicStroke;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import org.xhtmlrenderer.css.constants.IdentValue;
import org.xhtmlrenderer.css.parser.FSRGBColor;
import org.xhtmlrenderer.css.style.derived.BorderPropertySet;
import org.xhtmlrenderer.extend.OutputDevice;

public class BorderPainter {
    public static final int TOP = 1;
    public static final int LEFT = 2;
    public static final int BOTTOM = 4;
    public static final int RIGHT = 8;
    public static final int ALL = TOP + LEFT + BOTTOM + RIGHT;
    
    /**
    * Generates a full round rectangle that is made of bounds and border
    * @param bounds Dimmensions of the rect
    * @param border The border specs
    * @param Set true if you want the inner bounds of borders
    * @return A Path that is all sides of the round rectangle
    */
	public static Path2D generateBorderBounds(Rectangle bounds,
			BorderPropertySet border, boolean inside) {
		Path2D path = generateBorderShape(bounds, TOP, border, false,
				inside ? 1 : 0, 1);
		path.append(
				generateBorderShape(bounds, RIGHT, border, false, inside ? 1
						: 0, 1), true);
		path.append(
				generateBorderShape(bounds, BOTTOM, border, false, inside ? 1
						: 0, 1), true);
		path.append(
				generateBorderShape(bounds, LEFT, border, false,
						inside ? 1 : 0, 1), true);
		return path;
	}

	// helper function for bezier curves
	private static Point2D subT(double t, Point2D a, Point2D b) {
		return new Point2D.Double(a.getX() + t * (b.getX() - a.getX()),
				a.getY() + t * (b.getY() - a.getY()));
	}

	/**
	 * Cubic bezier curve function, takes in points and spits out the location
	 * of b(t) and 2 new bezier curves that both start and end at b(t)
	 * 
	 * @param t
	 *            as defined for bezier curves
	 * @param P0
	 *            start point
	 * @param P1
	 *            ctrl pt 1
	 * @param P2
	 *            ctrl pt 2
	 * @param P3
	 *            end point
	 * @return [[curve 1 starting at P0 and ending at B(t)], [curve 2 starting
	 *         at P(3) and ending at B(t)]]
	 */
	private static Point2D[][] getSubCurve(double t, Point2D P0, Point2D P1,
			Point2D P2, Point2D P3) {
		Point2D P4 = subT(t, P0, P1);
		Point2D P5 = subT(t, P1, P2);
		Point2D P6 = subT(t, P2, P3);
		Point2D P7 = subT(t, P4, P5);
		Point2D P8 = subT(t, P5, P6);
		Point2D P9 = subT(t, P7, P8);
		return new Point2D[][] { new Point2D[] { P0, P4, P7, P9 },
				new Point2D[] { P3, P6, P8, P9 } };
	}

	// 2 helper functions to reduce the number of params you have to see as the
	// last 2 are very option and rarely used
	public static Path2D generateBorderShape(Rectangle bounds, int side,
			BorderPropertySet border, boolean drawInterior) {
		return generateBorderShape(bounds, side, border, drawInterior, 0, 1);
	}

	public static Path2D generateBorderShape(Rectangle bounds, int side,
			BorderPropertySet border, boolean drawInterior, float scaledOffset) {
		return generateBorderShape(bounds, side, border, drawInterior,
				scaledOffset, 1);
	}

	/**
	 * Generates one side of a border
	 * 
	 * @param bounds
	 *            bounds of the container
	 * @param side
	 *            what side you want
	 * @param border
	 *            border props
	 * @param drawInterior
	 *            if you want it to be 2d or not, if false it will be just a
	 *            line
	 * @param scaledOffset
	 *            insets the border by multipling border widths by this
	 *            variable, best use would be 1 or .5, cant see it for much
	 *            other than that
	 * @param widthScale
	 *            scales the border widths by this factor, useful for drawing
	 *            half borders for border types like groove or double
	 * @return a path for the side chosen!
	 */
	public static Path2D generateBorderShape(Rectangle bounds, int side,
			BorderPropertySet border, boolean drawInterior, float scaledOffset,
			float widthScale) {

		float sideWidth = -1, topWidth = widthScale, leftWidth = widthScale, rightWidth = widthScale;
		double rotation = 0;
		float interiorWidth = 0, interiorHeight = 0, exteriorWidth = 0, exteriorHeight = 0;

		float leftRadius1 = 0;
		float leftRadius2 = 0;
		float rightRadius1 = 0;
		float rightRadius2 = 0;
		
		int xOffset = 0, yOffset = 0;

		if ((side & BorderPainter.TOP) == BorderPainter.TOP) {
			sideWidth = bounds.width;

			topWidth = widthScale * border.top();
			leftWidth = widthScale * border.left();
			rightWidth = widthScale * border.right();

			leftRadius1 = border.radiusTopRightOne();
			leftRadius2 = border.radiusTopRightTwo();
			
			rightRadius1 = border.radiusTopLeftOne();
			rightRadius2 = border.radiusTopLeftTwo();
			
			interiorWidth = bounds.width - (1 + scaledOffset) * widthScale
					* border.left() - (1 + scaledOffset) * widthScale
					* border.right();
			interiorHeight = bounds.height - (1 + scaledOffset) * widthScale
					* border.top() - (1 + scaledOffset) * widthScale
					* border.bottom();
			exteriorWidth = bounds.width - scaledOffset * widthScale
					* border.left() - scaledOffset * widthScale
					* border.right();
			exteriorHeight = bounds.height - scaledOffset * widthScale
					* border.top() - scaledOffset * widthScale
					* border.bottom();

			rotation = 0;
		} else if ((side & BorderPainter.RIGHT) == BorderPainter.RIGHT) {
			sideWidth = bounds.height;

			topWidth = widthScale * border.right();
			leftWidth = widthScale * border.top();
			rightWidth = widthScale * border.bottom();

			leftRadius1 = border.radiusBottomRightOne();
			leftRadius2 = border.radiusBottomRightTwo();
			
			rightRadius1 = border.radiusTopRightOne();
			rightRadius2 = border.radiusTopRightTwo();


			interiorHeight = bounds.width - (1 + scaledOffset) * widthScale
					* border.left() - (1 + scaledOffset) * widthScale
					* border.right();
			interiorWidth = bounds.height - (1 + scaledOffset) * widthScale
					* border.top() - (1 + scaledOffset) * widthScale
					* border.bottom();
			exteriorHeight = bounds.width - scaledOffset * widthScale
					* border.left() - scaledOffset * widthScale
					* border.right();
			exteriorWidth = bounds.height - scaledOffset * widthScale
					* border.top() - scaledOffset * widthScale
					* border.bottom();

			xOffset = bounds.width;
			yOffset = 0;
			rotation = Math.PI / 2;
		} else if ((side & BorderPainter.BOTTOM) == BorderPainter.BOTTOM) {
			sideWidth = bounds.width;

			topWidth = widthScale * border.bottom();
			leftWidth = widthScale * border.right();
			rightWidth = widthScale * border.left();

			leftRadius1 = border.radiusBottomRightOne();
			leftRadius2 = border.radiusBottomRightTwo();
			
			rightRadius1 = border.radiusBottomLeftOne();
			rightRadius2 = border.radiusBottomLeftTwo();
			
			interiorWidth = bounds.width - (1 + scaledOffset) * widthScale
					* border.left() - (1 + scaledOffset) * widthScale
					* border.right();
			interiorHeight = bounds.height - (1 + scaledOffset) * widthScale
					* border.top() - (1 + scaledOffset) * widthScale
					* border.bottom();
			exteriorWidth = bounds.width - scaledOffset * widthScale
					* border.left() - scaledOffset * widthScale
					* border.right();
			exteriorHeight = bounds.height - scaledOffset * widthScale
					* border.top() - scaledOffset * widthScale
					* border.bottom();

			xOffset = bounds.width;
			yOffset = bounds.height;
			rotation = Math.PI;
		} else if ((side & BorderPainter.LEFT) == BorderPainter.LEFT) {
			sideWidth = bounds.height;

			topWidth = widthScale * border.left();
			leftWidth = widthScale * border.bottom();
			rightWidth = widthScale * border.top();

			leftRadius1 = border.radiusBottomLeftOne();
			leftRadius2 = border.radiusBottomLeftTwo();

			rightRadius1 = border.radiusTopLeftOne();
			rightRadius2 = border.radiusTopLeftTwo();			

			interiorHeight = bounds.width - (1 + scaledOffset) * widthScale
					* border.left() - (1 + scaledOffset) * widthScale
					* border.right();
			interiorWidth = bounds.height - (1 + scaledOffset) * widthScale
					* border.top() - (1 + scaledOffset) * widthScale
					* border.bottom();
			exteriorHeight = (bounds.width - scaledOffset * widthScale
					* border.left() - scaledOffset * widthScale
					* border.right());
			exteriorWidth = bounds.height - scaledOffset * widthScale
					* border.top() - scaledOffset * widthScale
					* border.bottom();

			xOffset = 0;
			yOffset = bounds.height;
			rotation = 3 * Math.PI / 2;
		}

		float tco = scaledOffset * topWidth;
		float lco = scaledOffset * leftWidth;
		float rco = scaledOffset * rightWidth;

		float curveConstant = .45f;

		// top left corner % of side space
		float lp = 1;
		if (leftWidth != 0)
			lp = leftWidth / (topWidth + leftWidth);
		else
			lp = 0;

		// top right corner % of side space
		float rp = 1;
		if (rightWidth != 0)
			rp = rightWidth / (topWidth + rightWidth);
		else
			rp = 0;

		Path2D path = new Path2D.Float();

		if (leftRadius1 > 0) {

			Point2D[][] leftCurvePoints = getSubCurve(
					1 - lp,
					new Point2D.Double(leftRadius2
							+ lco, tco),
					new Point2D.Double(curveConstant
							* (leftRadius2) + lco,
							tco),
					new Point2D.Double(lco, tco + curveConstant
							* (leftRadius1)),
					new Point2D.Double(lco, tco
							+ leftRadius2));

			path.moveTo(leftCurvePoints[0][3].getX(),
					leftCurvePoints[0][3].getY());
			path.curveTo(leftCurvePoints[0][2].getX(),
					leftCurvePoints[0][2].getY(), leftCurvePoints[0][1].getX(),
					leftCurvePoints[0][1].getY(), leftCurvePoints[0][0].getX(),
					leftCurvePoints[0][0].getY());
		} else {
			path.moveTo(lco, tco);
		}

		if (rightRadius1 > 0) {

			Point2D[][] rightCurvePoints = getSubCurve(
					1 - rp,
					new Point2D.Double(sideWidth
							- rightRadius1 - rco, tco),
					new Point2D.Double(sideWidth - curveConstant
							* (rightRadius1) - rco,
							tco),
					new Point2D.Double(sideWidth - rco, tco + curveConstant
							* (rightRadius2)),
					new Point2D.Double(sideWidth - rco, tco
							+ rightRadius2));

			path.lineTo(rightCurvePoints[0][0].getX(),
					rightCurvePoints[0][0].getY());
			path.curveTo(rightCurvePoints[0][1].getX(),
					rightCurvePoints[0][1].getY(),
					rightCurvePoints[0][2].getX(),
					rightCurvePoints[0][2].getY(),
					rightCurvePoints[0][3].getX(),
					rightCurvePoints[0][3].getY());
		} else {
			path.lineTo(sideWidth - rightRadius1
					- rco, tco);
		}

		if (drawInterior) {
			// start drawing interior
			tco = (1 + scaledOffset) * topWidth;
			lco = (1 + scaledOffset) * leftWidth;
			rco = (1 + scaledOffset) * rightWidth;

			if (rightRadius1 > 0) {

				Point2D[][] rightCurvePoints = getSubCurve(
						1 - rp,
						new Point2D.Double(sideWidth
								- rightRadius1 - rco,
								tco),
						new Point2D.Double(
								sideWidth
										- curveConstant
										* (rightRadius1)
										- rco, tco),
						new Point2D.Double(sideWidth - rco, tco + curveConstant
								* (rightRadius2)),
						new Point2D.Double(sideWidth - rco, tco
								+ rightRadius2));

				path.lineTo(rightCurvePoints[0][3].getX(),
						rightCurvePoints[0][3].getY());
				path.curveTo(rightCurvePoints[0][2].getX(),
						rightCurvePoints[0][2].getY(),
						rightCurvePoints[0][1].getX(),
						rightCurvePoints[0][1].getY(),
						rightCurvePoints[0][0].getX(),
						rightCurvePoints[0][0].getY());
			} else {
				path.lineTo(sideWidth - rco, tco);
			}

			if (leftRadius2 > 0) {

				Point2D[][] leftCurvePoints = getSubCurve(
						1 - lp,
						new Point2D.Double(leftRadius2 + lco, tco),
						new Point2D.Double(
								curveConstant
										* (leftRadius2)
										+ lco, tco),
						new Point2D.Double(lco, tco + curveConstant
								* (leftRadius1)),
						new Point2D.Double(lco, tco
								+ leftRadius1));

				path.lineTo(leftCurvePoints[0][0].getX(),
						leftCurvePoints[0][0].getY());
				path.curveTo(leftCurvePoints[0][1].getX(),
						leftCurvePoints[0][1].getY(),
						leftCurvePoints[0][2].getX(),
						leftCurvePoints[0][2].getY(),
						leftCurvePoints[0][3].getX(),
						leftCurvePoints[0][3].getY());
			} else {
				path.lineTo(leftRadius2 + lco, tco);
			}

			path.closePath();
		}

		path.transform(AffineTransform.getRotateInstance(rotation, 0, 0));
		path.transform(AffineTransform.getTranslateInstance(bounds.x + xOffset,
				bounds.y + yOffset));

		return path;
	}
	
	
	
    /**
     * @param xOffset for determining starting point for patterns
     */
    public static void paint(
            Rectangle bounds, int sides, BorderPropertySet border, 
            RenderingContext ctx, int xOffset, boolean bevel) {
        if ((sides & BorderPainter.TOP) == BorderPainter.TOP && border.noTop()) {
            sides -= BorderPainter.TOP;
        }
        if ((sides & BorderPainter.LEFT) == BorderPainter.LEFT && border.noLeft()) {
            sides -= BorderPainter.LEFT;
        }
        if ((sides & BorderPainter.BOTTOM) == BorderPainter.BOTTOM && border.noBottom()) {
            sides -= BorderPainter.BOTTOM;
        }
        if ((sides & BorderPainter.RIGHT) == BorderPainter.RIGHT && border.noRight()) {
            sides -= BorderPainter.RIGHT;
        }

        //Now paint!
        if ((sides & BorderPainter.TOP) == BorderPainter.TOP && border.topColor() != FSRGBColor.TRANSPARENT) {
            paintBorderSide(ctx.getOutputDevice(), 
                    border, bounds, sides, BorderPainter.TOP, border.topStyle(), xOffset, bevel);
        }
        if ((sides & BorderPainter.LEFT) == BorderPainter.LEFT && border.leftColor() != FSRGBColor.TRANSPARENT) {
            paintBorderSide(ctx.getOutputDevice(), 
                    border, bounds, sides, BorderPainter.LEFT, border.leftStyle(), xOffset, bevel);
        }
        if ((sides & BorderPainter.BOTTOM) == BorderPainter.BOTTOM && border.bottomColor() != FSRGBColor.TRANSPARENT) {
            paintBorderSide(ctx.getOutputDevice(), 
                    border, bounds, sides, BorderPainter.BOTTOM, border.bottomStyle(), xOffset, bevel);
        }
        if ((sides & BorderPainter.RIGHT) == BorderPainter.RIGHT && border.rightColor() != FSRGBColor.TRANSPARENT) {
            paintBorderSide(ctx.getOutputDevice(), 
                    border, bounds, sides, BorderPainter.RIGHT, border.rightStyle(), xOffset, bevel);
        }
    }

    private static Rectangle shrinkRect(final Rectangle rect, final BorderPropertySet border, int sides) {
        Rectangle r2 = new Rectangle();
        r2.x = rect.x + ((sides & BorderPainter.LEFT) == 0 ? 0 : (int) border.left());
        r2.width = rect.width - ((sides & BorderPainter.LEFT) == 0 ? 0 : (int) border.left()) - ((sides & BorderPainter.RIGHT) == 0 ? 0 : (int) border.right());
        r2.y = rect.y + ((sides & BorderPainter.TOP) == 0 ? 0 : (int) border.top());
        r2.height = rect.height - ((sides & BorderPainter.TOP) == 0 ? 0 : (int) border.top()) - ((sides & BorderPainter.BOTTOM) == 0 ? 0 : (int) border.bottom());
        return r2;
    }

    private static void paintBorderSide(OutputDevice outputDevice, 
            final BorderPropertySet border, final Rectangle bounds, final int sides, 
            int currentSide, final IdentValue borderSideStyle, int xOffset, boolean bevel) {
        if (borderSideStyle == IdentValue.RIDGE || borderSideStyle == IdentValue.GROOVE) {
            BorderPropertySet bd2 = new BorderPropertySet((int) (border.top() / 2),
                    (int) (border.right() / 2),
                    (int) (border.bottom() / 2),
                    (int) (border.left() / 2));
//            if (borderSideStyle == IdentValue.RIDGE) {
//                paintBorderSideShape(
//                        outputDevice, bounds, border, border.darken(borderSideStyle), 
//                        border.lighten(borderSideStyle), sides, currentSide, bevel);
//                paintBorderSidePolygon(
//                        outputDevice, bounds, bd2, border.lighten(borderSideStyle), 
//                        border.darken(borderSideStyle), sides, currentSide, bevel);
//            } else {
//                paintBorderSidePolygon(
//                        outputDevice, bounds, border, border.lighten(borderSideStyle),
//                        border.darken(borderSideStyle), sides, currentSide, bevel);
//                paintBorderSidePolygon(
//                        outputDevice, bounds, bd2, border.darken(borderSideStyle),
//                        border.lighten(borderSideStyle), sides, currentSide, bevel);
//            }
        } else if (borderSideStyle == IdentValue.OUTSET) {
//            paintBorderSidePolygon(outputDevice, bounds, border,
//                    border.lighten(borderSideStyle),
//                    border.darken(borderSideStyle), sides, currentSide, bevel);
        } else if (borderSideStyle == IdentValue.INSET) {
//            paintBorderSidePolygon(outputDevice, bounds, border,
//                    border.darken(borderSideStyle),
//                    border.lighten(borderSideStyle), sides, currentSide, bevel);
        } else if (borderSideStyle == IdentValue.SOLID) {

        	        	outputDevice.setStroke(new BasicStroke(1f));

        	        	if(currentSide == TOP) {

        	            	outputDevice.setColor(border.topColor());
        	            	outputDevice.fill(generateBorderShape(bounds, TOP, border, true, 0, 1));

        	        	}

        	        	if(currentSide == RIGHT) {

            	outputDevice.setColor(border.rightColor());
        	            	outputDevice.fill(generateBorderShape(bounds, RIGHT, border, true, 0, 1));

        	        	}

        	        	if(currentSide == BOTTOM) {

        	            	outputDevice.setColor(border.bottomColor());

        	            	outputDevice.fill(generateBorderShape(bounds, BOTTOM, border, true, 0, 1));

        	        	}

        	        	if(currentSide == LEFT) {

        	            	outputDevice.setColor(border.leftColor());

        	            	outputDevice.fill(generateBorderShape(bounds, LEFT, border, true, 0, 1));

        	        	}
        	
        	//paintSolid(outputDevice, bounds, border, border, sides, currentSide, bevel);
        } else if (borderSideStyle == IdentValue.DOUBLE) {
            paintDoubleBorder(outputDevice, border, bounds, sides, currentSide, bevel);
        } else {
            int thickness = 0;
            if (currentSide == BorderPainter.TOP) thickness = (int) border.top();
            if (currentSide == BorderPainter.BOTTOM) thickness = (int) border.bottom();
            if (currentSide == BorderPainter.RIGHT) thickness = (int) border.right();
            if (currentSide == BorderPainter.LEFT) thickness = (int) border.left();
            if (borderSideStyle == IdentValue.DASHED) {
                outputDevice.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                paintPatternedRect(outputDevice, bounds, border, border, new float[]{8.0f + thickness * 2, 4.0f + thickness}, sides, currentSide, xOffset);
                outputDevice.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
            if (borderSideStyle == IdentValue.DOTTED) {
                // turn off anti-aliasing or the dots will be all blurry
                outputDevice.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                paintPatternedRect(outputDevice, bounds, border, border, new float[]{thickness, thickness}, sides, currentSide, xOffset);
                outputDevice.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
        }
    }
    
    private static DoubleBorderInfo calcDoubleBorderInfo(int width) {
        DoubleBorderInfo result = new DoubleBorderInfo();
        if (width == 1) {
            result.setOuter(1);
        } else if (width == 2) {
            result.setOuter(1);
            result.setInner(1);
        } else {
            int extra = width % 3;
            switch (extra) {
                case 0:
                    result.setOuter(width / 3);
                    result.setCenter(width / 3);
                    result.setInner(width / 3);
                    break;
                case 1:
                    result.setOuter((width + 2) / 3 - 1);
                    result.setCenter((width + 2) / 3);
                    result.setInner((width + 2) / 3 - 1);
                    break;                    
                case 2:
                    result.setOuter((width + 1) / 3);
                    result.setCenter((width + 1) / 3 - 1);
                    result.setInner((width + 1) / 3);
                    break;
            }
        }
        return result;
    }

    private static void paintDoubleBorder(
            OutputDevice outputDevice, BorderPropertySet border, 
            Rectangle bounds, int sides, int currentSide, boolean bevel) {
        DoubleBorderInfo topBorderInfo = calcDoubleBorderInfo((int)border.top());
        DoubleBorderInfo rightBorderInfo = calcDoubleBorderInfo((int)border.right());
        DoubleBorderInfo bottomBorderInfo = calcDoubleBorderInfo((int)border.bottom());
        DoubleBorderInfo leftBorderInfo = calcDoubleBorderInfo((int)border.left());
        
        BorderPropertySet outer = new BorderPropertySet(
                topBorderInfo.getOuter(), rightBorderInfo.getOuter(), 
                bottomBorderInfo.getOuter(), leftBorderInfo.getOuter());
        
        BorderPropertySet center = new BorderPropertySet(
                topBorderInfo.getCenter(), rightBorderInfo.getCenter(), 
                bottomBorderInfo.getCenter(), leftBorderInfo.getCenter());
        
        BorderPropertySet inner = new BorderPropertySet(
                topBorderInfo.getInner(), rightBorderInfo.getInner(), 
                bottomBorderInfo.getInner(), leftBorderInfo.getInner());

        Rectangle b2 = shrinkRect(bounds, outer, bevel ? sides : currentSide);
        b2 = shrinkRect(b2, center, bevel ? sides : currentSide);
        // draw outer border
        //paintSolid(outputDevice, bounds, outer, border, sides, currentSide, bevel);
        // draw inner border
        //paintSolid(outputDevice, b2, inner, border, sides, currentSide, bevel);
    }

    /**
     * Gets the polygon to be filled for the border
     */
    private static Polygon getBorderSidePolygon(
            final Rectangle bounds, final BorderPropertySet border, final int sides, 
            int currentSide, boolean bevel) {
        int rightCorner = 0;
        int leftCorner = 0;
        int topCorner = 0;
        int bottomCorner = 0;
        if (bevel) {
            rightCorner = (((sides & BorderPainter.RIGHT) == BorderPainter.RIGHT) ? (int) border.right() : 0);
            leftCorner = (((sides & BorderPainter.LEFT) == BorderPainter.LEFT) ? (int) border.left() : 0);
            topCorner = (((sides & BorderPainter.TOP) == BorderPainter.TOP) ? (int) border.top() : 0);
            bottomCorner = (((sides & BorderPainter.BOTTOM) == BorderPainter.BOTTOM) ? (int) border.bottom() : 0);
        }
        Polygon poly = null;
        if (currentSide == BorderPainter.TOP) {
            if ((int) border.top() != 1) {
                // use polygons for borders over 1px wide
                poly = new Polygon();
                poly.addPoint(bounds.x, bounds.y);
                poly.addPoint(bounds.x + bounds.width, bounds.y);
                poly.addPoint(bounds.x + bounds.width - rightCorner, bounds.y + (int) border.top() - 0);
                poly.addPoint(bounds.x + leftCorner, bounds.y + (int) border.top() - 0);
            }
        } else if (currentSide == BorderPainter.BOTTOM) {
            if ((int) border.bottom() != 1) {
                poly = new Polygon();
                // upper right
                poly.addPoint(bounds.x + bounds.width - rightCorner, bounds.y + bounds.height - (int) border.bottom());
                // upper left
                poly.addPoint(bounds.x + leftCorner, bounds.y + bounds.height - (int) border.bottom());
                // lower left
                poly.addPoint(bounds.x, bounds.y + bounds.height);
                // lower right
                poly.addPoint(bounds.x + bounds.width, bounds.y + bounds.height - 0);
            }
        } else if (currentSide == BorderPainter.RIGHT) {
            if ((int) border.right() != 1) {
                poly = new Polygon();
                poly.addPoint(bounds.x + bounds.width, bounds.y);
                poly.addPoint(bounds.x + bounds.width - (int) border.right(), bounds.y + topCorner);
                poly.addPoint(bounds.x + bounds.width - (int) border.right(), bounds.y + bounds.height - bottomCorner);
                poly.addPoint(bounds.x + bounds.width, bounds.y + bounds.height);
            }
        } else if (currentSide == BorderPainter.LEFT) {
            if ((int) border.left() != 1) {
                poly = new Polygon();
                poly.addPoint(bounds.x, bounds.y);
                poly.addPoint(bounds.x + (int) border.left(), bounds.y + topCorner);
                poly.addPoint(bounds.x + (int) border.left(), bounds.y + bounds.height - bottomCorner);
                poly.addPoint(bounds.x, bounds.y + bounds.height);
            }
        }
        return poly;
    }

    /**
     * @param xOffset     for inline borders, to determine dash_phase of top and bottom
     */
    private static void paintPatternedRect(OutputDevice outputDevice, 
            final Rectangle bounds, final BorderPropertySet border, 
            final BorderPropertySet color, final float[] pattern, 
            final int sides, final int currentSide, int xOffset) {
        Stroke old_stroke = outputDevice.getStroke();

        if (currentSide == BorderPainter.TOP) {
            outputDevice.setColor(color.topColor());
            outputDevice.setStroke(new BasicStroke((int) border.top(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, pattern, xOffset));
            outputDevice.drawBorderLine(
                    bounds, BorderPainter.TOP, (int)border.top(), false);
        } else if (currentSide == BorderPainter.LEFT) {
            outputDevice.setColor(color.leftColor());
            outputDevice.setStroke(new BasicStroke((int) border.left(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, pattern, 0));
            outputDevice.drawBorderLine(
                    bounds, BorderPainter.LEFT, (int)border.left(), false);
        } else if (currentSide == BorderPainter.RIGHT) {
            outputDevice.setColor(color.rightColor());
            outputDevice.setStroke(new BasicStroke((int) border.right(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, pattern, 0));
            outputDevice.drawBorderLine(
                    bounds, BorderPainter.RIGHT, (int)border.right(), false);
        } else if (currentSide == BorderPainter.BOTTOM) {
            outputDevice.setColor(color.bottomColor());
            outputDevice.setStroke(new BasicStroke((int) border.bottom(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, pattern, xOffset));
            outputDevice.drawBorderLine(
                    bounds, BorderPainter.BOTTOM, (int)border.bottom(), false);
        }

        outputDevice.setStroke(old_stroke);
    }

    private static void paintBorderSideShape(OutputDevice outputDevice, 
            final Rectangle bounds, final BorderPropertySet border, 
            final BorderPropertySet high, final BorderPropertySet low, 
            final float offset, final float scale,
            final int sides, int currentSide, boolean bevel) {
        if (currentSide == BorderPainter.TOP) {
            paintSolid(outputDevice, bounds, high, offset, scale, sides, currentSide, bevel);
        } else if (currentSide == BorderPainter.BOTTOM) {
            paintSolid(outputDevice, bounds, low, offset, scale, sides, currentSide, bevel);
        } else if (currentSide == BorderPainter.RIGHT) {
            paintSolid(outputDevice, bounds, low, offset, scale, sides, currentSide, bevel);
        } else if (currentSide == BorderPainter.LEFT) {
            paintSolid(outputDevice, bounds, high, offset, scale, sides, currentSide, bevel);
        }
    }

    private static void paintSolid(OutputDevice outputDevice, 
            final Rectangle rect, final BorderPropertySet border, 
            final float offset, final float scale, final int sides, int currentSide,
            boolean bevel) {
        Shape bounds = generateBorderBounds(rect, border, bevel);

        if (currentSide == BorderPainter.TOP) {
            outputDevice.setColor(border.topColor());

            // draw a 1px border with a line instead of a polygon
            if ((int) border.top() == 1) {
                outputDevice.drawBorderLine(bounds, BorderPainter.TOP, 
                        (int)border.top(), true);
            } else {
                // use polygons for borders over 1px wide
               // outputDevice.fill(poly);
            }
        } else if (currentSide == BorderPainter.BOTTOM) {
            outputDevice.setColor(border.bottomColor());
            if ((int) border.bottom() == 1) {
                outputDevice.drawBorderLine(bounds, BorderPainter.BOTTOM, 
                        (int)border.bottom(), true);
            } else {
                //outputDevice.fill(poly);
            }
        } else if (currentSide == BorderPainter.RIGHT) {
            outputDevice.setColor(border.rightColor());
            if ((int) border.right() == 1) {
                outputDevice.drawBorderLine(bounds, BorderPainter.RIGHT, 
                        (int)border.right(), true);
            } else {
                //outputDevice.fill(poly);
            }
        } else if (currentSide == BorderPainter.LEFT) {
            outputDevice.setColor(border.leftColor());
            if ((int) border.left() == 1) {
                outputDevice.drawBorderLine(bounds, BorderPainter.LEFT, 
                        (int)border.left(), true);
            } else {
               // outputDevice.fill(poly);
            }
        }
    }
    
    private static class DoubleBorderInfo {
        private int _outer;
        private int _center;
        private int _inner;
        
        public int getCenter() {
            return _center;
        }
        
        public void setCenter(int center) {
            _center = center;
        }
        
        public int getInner() {
            return _inner;
        }
        
        public void setInner(int inner) {
            _inner = inner;
        }
        
        public int getOuter() {
            return _outer;
        }
        
        public void setOuter(int outer) {
            _outer = outer;
        }
    }
}
