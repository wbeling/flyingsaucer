package org.joshy.html.painter;

import org.joshy.u;
import org.joshy.html.*;
import org.joshy.html.box.*;

public class ListItemPainter {
    public static void paint(Context c, Box box) {
        String type = c.css.getStringProperty(box.node,"list-style-type");
        
        if(type.equals("none")) {
            return;
        }
        
        if(type.equals("disc")) {
            int rad = 8;  // change this to use the glyph height
            int baseline = box.height; // change this to use the real baseline
            c.getGraphics().fillOval(box.x-rad-2,box.y+baseline/2 - rad/2 + 2,rad,rad);
            return;
        }
        if(type.equals("square")) {
            int rad = 8;  // change this to use the glyph height
            int baseline = box.height; // change this to use the real baseline
            c.getGraphics().fillRect(box.x-rad-2,box.y+baseline/2 - rad/2 + 2,rad,rad);
            return;
        }
        if(type.equals("circle")) {
            int rad = 8;  // change this to use the glyph height
            int baseline = box.height; // change this to use the real baseline
            c.getGraphics().drawOval(box.x-rad-2,box.y+baseline/2 - rad/2 + 2,rad,rad);
            return;
        }
        
        if(type.equals("lower-greek")) { type = "decimal"; }
        if(type.equals("decimal-leading-zero")) { type = "decimal"; }
        if(type.equals("decimal")) {
            c.getGraphics().drawString(""+box.list_count,box.x-15, box.y+box.height/2+8);
            return;
        }
        
        if(type.equals("lower-latin")) {
            c.getGraphics().drawString(""+toLatin(box.list_count).toLowerCase(),box.x-15, box.y+box.height/2+8);
            return;
        }
        
        if(type.equals("upper-latin")) {
            c.getGraphics().drawString(""+toLatin(box.list_count).toUpperCase(),box.x-15, box.y+box.height/2+8);
            return;
        }
        
        if(type.equals("lower-roman")) {
            c.getGraphics().drawString(""+toRoman(box.list_count).toLowerCase(),box.x-15, box.y+box.height/2+8);
            return;
        }
        
        if(type.equals("upper-roman")) {
            c.getGraphics().drawString(""+toRoman(box.list_count).toUpperCase(),box.x-15, box.y+box.height/2+8);
            return;
        }
    }
    
    protected static String toLatin(int val) {
        if(val > 26) {
            int val1 = val % 26;
            int val2 = val / 26;
            return toLatin(val2) + toLatin(val1);
        }
        return ((char)(val + 64)) + "";
    }
    
    protected static String toRoman(int val) {
        int[] ints =    { 1000,   900,  500,  400, 100,   90,  50,   40, 10,    9,    5,    4,   1 };
        String[] nums = {  "M",  "CM",  "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };
        StringBuffer sb = new StringBuffer();
        for(int i=0; i< ints.length; i++) {
            int count = (int)(val/ints[i]);
            for(int j=0; j<count; j++) {
                sb.append(nums[i]);
            }
            val -= ints[i]*count;
        }
        return sb.toString();
    }
}
