/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hp.test.framework.MD5Hash.hashvalue;

/**
 *
 * @author sayedmo
 */
public class driver {
    public static void main(String[] args) {
        TestStringHashGenerator.StringHashGenerator("admin");
        TestFileHashGenerator.FileHashGenerator("C:\\parse.log.2");
    }
}
