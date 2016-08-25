/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.runner;

import org.testng.ISuite;
import org.testng.ISuiteListener;

/**
 *
 * @author yanamalp
 */
public class SuiteListener implements ISuiteListener{

private static ThreadLocal<ISuite> ACCESS = new ThreadLocal<ISuite>();

public static ISuite getAccess() {
         return ACCESS.get();
    }

@Override
public void onFinish(ISuite suite) {
    ACCESS.set(null);
}

@Override
public void onStart(ISuite arg0) {
    ACCESS.set(arg0);
}

 }
