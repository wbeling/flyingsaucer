package org.xhtmlrenderer.render;

public class BorderRadiusPair {

	private final float _left;
	private final float _right;
	
	public BorderRadiusPair(float l, float r) {
		this._left = l;
		this._right = r;
	}
	public boolean hasRadius() {
		return _left > 0 || _right > 0;
	}

	public float getMaxLeft(float max) {
		return _left;
	}

	public float getMaxRight(float max) {
		return _right;
	}

	public float left() {
		return _left;
	}
	public float right() {
		return _right;
	}	
}
