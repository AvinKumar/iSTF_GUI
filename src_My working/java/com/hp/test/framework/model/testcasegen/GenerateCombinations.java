/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.model.testcasegen;

/**
 *
 * @author sayedmo
 */
//import XMLDOM.*;
import java.util.*;

public class GenerateCombinations {
    static int counter = 0;

    // method called to generate combinations using map, putting the combinations in list
    public static <K, V> void combinations(Map<K, Set<V>> map, List<Map<K, V>> list) {
        recurse(map, new LinkedList<K>(map.keySet()).listIterator(), new HashMap<K, V>(), list);
    }

    // helper method to do the recursion
    private static <K, V> void recurse(Map<K, Set<V>> map, ListIterator<K> iter, Map<K, V> cur, List<Map<K, V>> list) {
    
        // we're at a leaf node in the recursion tree, add solution to list
        if (!iter.hasNext()) {
            Map<K, V> entry = new HashMap<K, V>();

            for (K key : cur.keySet()) {
                String keyStr = key.toString();
                int index = keyStr.indexOf('_');
                if(keyStr.substring(index+1, index+5).equalsIgnoreCase("auto")){
                    index = keyStr.lastIndexOf('_');              
                    keyStr = keyStr.substring(0,index) + "." + counter + keyStr.substring(index);
                    counter++;
                   entry.put((K) keyStr, cur.get(key));
                }
                else 
                    entry.put(key, cur.get(key));
            }
            
            list.add(entry);
        } else {
            K key = iter.next();
            Set<V> set = map.get(key);

            for (V value : set) {
                //String keyStr = key.toString();
                //int index = keyStr.lastIndexOf('_');
                //keyStr = keyStr.substring(0,index) + "." + counter + keyStr.substring(index);
                //counter++;
                //System.out.println(key.toString());
                //System.out.println(value.toString());
                //System.out.println("===============");

                cur.put(key, value);
                recurse(map, iter, cur, list);
                cur.remove(key);
            }

            iter.previous();
        }
    }

}
