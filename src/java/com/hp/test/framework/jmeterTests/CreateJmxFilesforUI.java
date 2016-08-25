/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.jmeterTests;

import com.hp.test.framework.ReadProps.ReadJmeterConfigProps;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

/**
 *
 * @author yanamalp
 */
public class CreateJmxFilesforUI {

    public void createJmxFilesforui() {
        BufferedReader br;
        BufferedWriter bw;
        ReadJmeterConfigProps readjmeterconfigprops = new ReadJmeterConfigProps();
        String template_path = readjmeterconfigprops.getProperty("location.template.file");
        String testplans_path = readjmeterconfigprops.getProperty("location.jmeter.testplans");

        List<String> jmetertests = GetJmeterTestCaseFileList.getjmeterTestcaseList();
        //  File jmx_file=new File("C:\\Users\\yanamalp\\Desktop\\TestngSuite\\jp@gc - Web Driver Sampler.jmx");

        try {
            for (int i = 0; i < jmetertests.size(); i++) {
                File inputfile = new File(template_path);

                br = new BufferedReader(new FileReader(inputfile));
                bw = new BufferedWriter(new FileWriter(new File(testplans_path + jmetertests.get(i) + ".jmx")));
                String str = "";
     //   String jmx_foramt_script = "";
                //  boolean convert_jmeterstatements = false;
                // String webdriver_script = ConvertJmeterTestcase.convertjmetertestcase();
                while ((str = br.readLine()) != null) {

                    if (str.contains("junitSampler.classname")) {
                        System.out.println("****" + str);
                        String[] temp_ar = jmetertests.get(i).split("\\.");
                        str = str.replace("ISTF_JAVA_CLASS", temp_ar[0]);
                        bw.write(str + "\n");
                        continue;
                    }
                    bw.write(str);
                    bw.write("\n");
                }
                bw.close();
                br.close();
            }
        } catch (Exception e) {
            System.out.println("Exception in Generating Jmx Files" + e.getMessage());
        }
    }

//    public static void main(String ar[]) {
//        CreateJmxFilesforUI dd = new CreateJmxFilesforUI();
//        dd.createJmxFilesforui();
//    }
}
