/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hp.test.framework.objrepo;


import java.util.Map;

/**
 *
 * @author sayedmo
 */
public class MaptoXML {
    
    public static String toXML(Map<String, String> map, String root) {
        StringBuilder sb = new StringBuilder("<");
        sb.append(root);
        sb.append(">");

        for (Map.Entry<String, String> e : map.entrySet()) {
            sb.append("<");
            sb.append(e.getKey());
            sb.append(">");

            sb.append(e.getValue());

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
