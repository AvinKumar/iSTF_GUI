/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.objrepo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static com.hp.test.framework.objrepo.MappingObjectToModel.obj_repo_path;

/**
 *
 * @author yanamalp
 */
public class EditingRepository {

    public static void addLocator(String Locator, String ModelxmlElement) {
        String Obj_repo_path = MappingObjectToModel.obj_repo_path;

        BufferedReader reader = null;
        BufferedWriter writer = null;
        ArrayList list = new ArrayList();
        String fileName = Obj_repo_path;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String tmp;
            int cou = 0;
            while ((tmp = reader.readLine()) != null) {
                if (cou == 0) {
                    list.add("<Object>");
                    list.add("<" + ModelxmlElement + ">" + Locator + "</" + ModelxmlElement + ">");
                    cou = 1;
                    continue;

                }
                list.add(tmp);
            }
            reader.close();

            writer = new BufferedWriter(new FileWriter(fileName));
            for (int i = 0; i < list.size(); i++) {
                writer.write(list.get(i) + "\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(EditingRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void deleteLocator(String ModelxmlElement) {
        
         String Obj_repo_path = MappingObjectToModel.obj_repo_path;

        BufferedReader reader = null;
        BufferedWriter writer = null;
        ArrayList list = new ArrayList();
        String fileName = Obj_repo_path;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String tmp;
         
            while ((tmp = reader.readLine()) != null) {
                
                if (tmp.contains(ModelxmlElement)) {
                    System.out.println("Inside Skip"+tmp);
                   // list.add("<" + ModelxmlElement + ">" + Locator + "</" + ModelxmlElement + ">");
                     continue;

                }
                list.add(tmp);
            }
            reader.close();

            writer = new BufferedWriter(new FileWriter(fileName));
            for (int i = 0; i < list.size(); i++) {
                writer.write(list.get(i) + "\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(EditingRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }

    public static void updateLocator(String Locator, String ModelxmlElement) {
        
        String Obj_repo_path = MappingObjectToModel.obj_repo_path;

        BufferedReader reader = null;
        BufferedWriter writer = null;
        ArrayList list = new ArrayList();
        String fileName = Obj_repo_path;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String tmp;
         
            while ((tmp = reader.readLine()) != null) {
                if (tmp.contains(ModelxmlElement)) {
                  
                    list.add("<" + ModelxmlElement + ">" + Locator + "</" + ModelxmlElement + ">");
                     continue;

                }
                list.add(tmp);
            }
            reader.close();

            writer = new BufferedWriter(new FileWriter(fileName));
            for (int i = 0; i < list.size(); i++) {
                writer.write(list.get(i) + "\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(EditingRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static void main(String ar[]) {
        //  addLocator();
    }
}
