/*
 * {{{ header & license
 * Copyright (c) 2007 Vianney le Clément
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * }}}
 */
package org.xhtmlrenderer.simple.xhtml.controls;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.xhtmlrenderer.simple.xhtml.FormControl;
import org.xhtmlrenderer.simple.xhtml.FormControlListener;
import org.xhtmlrenderer.simple.xhtml.FormListener;
import org.xhtmlrenderer.simple.xhtml.XhtmlForm;
import org.xhtmlrenderer.util.JsoupUtil;

public abstract class AbstractControl implements FormControl {

    private XhtmlForm _form;
    private Element _element;
    private String _name;

    private String _initialValue;
    private String _value;
    private boolean _successful;
    private boolean _enabled;

    private List<FormControlListener> _listeners = new ArrayList<FormControlListener>();

    public AbstractControl(XhtmlForm form, Element e) {
        _form = form;
        _element = e;
        _name = e.attr("name");
        if (_name.length() == 0) {
            _name = e.attr("id");
        }
        _initialValue = e.attr("value");
        _value = _initialValue;
        _enabled = (e.attr("disabled").length() == 0);
        _successful = _enabled;

        if (form != null) {
            form.addFormListener(new FormListener() {
                public void submitted(XhtmlForm form) {
                }

                public void resetted(XhtmlForm form) {
                    reset();
                }
            });
        }
    }

    protected void fireChanged() {
        for (Iterator<FormControlListener> iter = _listeners.iterator(); iter.hasNext();) {
            iter.next().changed(this);
        }
    }

    protected void fireSuccessful() {
        for (Iterator<FormControlListener> iter = _listeners.iterator(); iter.hasNext();) {
            iter.next().successful(this);
        }
    }

    protected void fireEnabled() {
        for (Iterator<FormControlListener> iter = _listeners.iterator(); iter.hasNext();) {
            iter.next().enabled(this);
        }
    }

    public void addFormControlListener(FormControlListener listener) {
        _listeners.add(listener);
    }

    public void removeFormControlListener(FormControlListener listener) {
        _listeners.remove(listener);
    }

    public Element getElement() {
        return _element;
    }

    public XhtmlForm getForm() {
        return _form;
    }

    public String getName() {
        return _name;
    }

    public String getInitialValue() {
        return _initialValue;
    }
    
    protected void setInitialValue(String value) {
        _initialValue = value;
        _value = value;
    }

    public String getValue() {
        if (isMultiple()) {
            return null;
        } else {
            return _value;
        }
    }

    public void setValue(String value) {
        if (!isMultiple()) {
            _value = value;
            fireChanged();
        }
    }

    public String[] getMultipleValues() {
        return null;
    }

    public void setMultipleValues(String[] values) {
        // do nothing
    }

    public boolean isHidden() {
        return false;
    }

    public boolean isEnabled() {
        return _enabled;
    }

    public boolean isSuccessful() {
        return _successful && _enabled;
    }

    public boolean isMultiple() {
        return false;
    }

    public void setSuccessful(boolean successful) {
        _successful = successful;
        fireSuccessful();
    }

    public void setEnabled(boolean enabled) {
        _enabled = enabled;
        fireEnabled();
    }

    public void reset() {
        setValue(_initialValue);
    }

    public static String collectText(Element e) {
        StringBuilder result = new StringBuilder();
        Node node = JsoupUtil.firstChild(e);
        if (node != null) {
            do {
                if (JsoupUtil.isText(node)) {
                    TextNode text = (TextNode) node;
                    result.append(text.text());
                }
            } while ((node = node.nextSibling()) != null);
        }
        return result.toString().trim();
    }

    public static int getIntAttribute(Element e, String attribute, int def) {
        int result = def;
        String str = e.attr(attribute);
        if (str.length() > 0) {
            try {
                result = Integer.parseInt(str);
            } catch (NumberFormatException ex) {
            }
        }
        return result;
    }

}
