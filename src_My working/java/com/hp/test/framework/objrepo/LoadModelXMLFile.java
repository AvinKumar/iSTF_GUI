/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.objrepo;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author yanamalp
 */
public class LoadModelXMLFile {
    
    public static JTree tree;
    
    public static DefaultMutableTreeNode root;
    
    public static void validate(String ModelXMlPATH) {
       
        tree=null;
        root=null;
         root = new DefaultMutableTreeNode("Search");
        DefaultMutableTreeNode ChildNodes = new DefaultMutableTreeNode("Elements");
        
        ReadXMLFileUsingDom r = new ReadXMLFileUsingDom();
        r.ReadXMLModel(ModelXMlPATH);
        List<String> node_list = new ArrayList<String>();
        for (String Item : ReadXMLFileUsingDom.mapmainnode.keySet()) {
            node_list.add(Item);
             ChildNodes.add(new DefaultMutableTreeNode(Item));
  
        }
        root.add(ChildNodes);
        LoadObject();
      //  System.out.println("fuclk man");
        // NewJFrame1.Tree_ModelXml.setModel(new top);
    }
    
    public static void LoadObject() {
        tree = new JTree(root);
//       ImageIcon imageIcon = new ImageIcon(LoadModelXMLFile.class.getResource("/leaf.jpg"));
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();        
     //   renderer.setLeafIcon(imageIcon);
        
        tree.setCellRenderer(renderer);
        tree.setShowsRootHandles(true);
        tree.setRootVisible(true);
        tree.setAutoscrolls(true);
        tree.setScrollsOnExpand(true);
        
        tree.setBounds(20, 20, 250, 400);
        tree.setAutoscrolls(true);
        tree.setVisibleRowCount(10);
        tree.setVisible(true);

    //    selectedLabel = new JLabel();
        //  add(selectedLabel, BorderLayout.SOUTH);
        tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                  System.out.println(selectedNode.getUserObject().toString());
            }
        });

        JScrollPane scrollPane = new JScrollPane();
      
        scrollPane.getViewport().add(tree);

        MappingObjectToModel.jTree2.setModel(tree.getModel());
        
    }
    
}
