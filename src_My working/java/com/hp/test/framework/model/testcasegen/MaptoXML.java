/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hp.test.framework.model.testcasegen;


import java.util.Map;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author sayedmo
 */
public class MaptoXML {
    
    public static String toXML(Map<String, String> map, String root) {
        String value;
        StringBuilder sb = new StringBuilder("<");
        sb.append(root);
        sb.append(">");

        for (Map.Entry<String, String> e : map.entrySet()) {
            sb.append("<");
            sb.append(e.getKey());
            sb.append(">");
            value=StringEscapeUtils.escapeXml(e.getValue());
            
            //sb.append(e.getValue());
            sb.append(value);

            sb.append("</");
            sb.append(e.getKey());
            sb.append(">");
        }

        sb.append("</");
        sb.append(root);
        sb.append(">");

        return sb.toString();
    }
}
