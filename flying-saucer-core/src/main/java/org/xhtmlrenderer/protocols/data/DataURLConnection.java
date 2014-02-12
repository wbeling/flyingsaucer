/*
 * {{{ header & license
 * Copyright (c) 2008 Sean Bright
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
package org.xhtmlrenderer.protocols.data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.Map;

public class DataURLConnection extends URLConnection {

    private Map<String, String> _headers = new HashMap<String, String>();
    private byte [] _data;

    DataURLConnection(URL u) {
        super(u);
    }

    public void connect() throws IOException {
        parseURL();
    }

    public String getContentType() {
        String type = _headers.get("Content-Type");
        
        if (type == null) {
            return "Content-Type: text/plain; charset=US-ASCII";
        }

        return type;
    }
    
    public int getContentLength() {
        if (_data == null)
            return 0;

        return _data.length;
    }
    
    public InputStream getInputStream() throws IOException {
        connect();
        
        if (_data == null)
            return new ByteArrayInputStream(new byte [] {});

        return new ByteArrayInputStream(_data);
    }

    protected void parseURL() throws UnsupportedEncodingException {
        String sub = getURL().getPath();

        int comma = sub.indexOf(',');
        
        if (comma < 0) {
            throw new RuntimeException("Improperly formatted data URL");
        }
        
        String meta = sub.substring(0, comma);
        String data = sub.substring(comma + 1);

        boolean isBase64 = false;
        Map<String, String> properties = new HashMap<String, String>();
        
        properties.put("charset", "US-ASCII");
        
        if (meta.length() > 0) {
            String [] parts = meta.split(";");

            if (parts.length > 0) {
                int index = 0;

                // See if a media type is specified
                if (meta.charAt(0) != ';') {
                    // We have a media type
                    _headers.put("Content-Type", parts[index++]);
                }
                
                for (; index < parts.length; index++) {
                    if (parts[index].indexOf("=") >= 0) {
                        String [] nameValuePair = parts[index].split("=");
                        
                        if (nameValuePair.length > 1) {
                            _headers.put(nameValuePair[0], nameValuePair[1]);
                        }
                    } else {
                        if (parts[index].compareTo("base64") == 0) {
                            isBase64 = true;
                        }
                    }
                }
            }
        }
        
        String charset = properties.get("charset");

        // Make sure we have a supported charset
        if (!Charset.isSupported(charset)) {
            throw new UnsupportedCharsetException(charset);
        }
        
        // Now we parse the data
        if (isBase64) {
            _data = Base64.decode(data);
        } else {
            _data = URLByteDecoder.decode(data);
        }
    }
}

class URLByteDecoder {
    
    public static byte [] decode(String s) {

        byte [] buffer = new byte [s.length()];
        
        int index = 0;
        int bindex = 0;
        char c;
        
        while (index < s.length()) {
            c =  s.charAt(index);
            
            switch (c) {
                case '+':
                    buffer[bindex++] = ' ';
                    break;
                case '%':
                    buffer[bindex++] = (byte) Integer
                        .parseInt(s.substring(index + 1, index + 3), 16);
                    index += 2;
                    break;
                default:
                    buffer[bindex++] = (byte) c;
                    break;
            }
            
            index++;
        }
        
        byte [] result = new byte [bindex];
        
        System.arraycopy(buffer, 0, result, 0, bindex);
        
        return result;
    }
    
}

class Base64 {
    
    public static byte [] decode(String s) 
    {
    	return s == null ? new byte[0] : org.apache.commons.codec.binary.Base64.decodeBase64(s);
    }
}
