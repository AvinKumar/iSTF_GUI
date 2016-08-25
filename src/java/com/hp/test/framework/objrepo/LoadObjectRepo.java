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
import java.util.Map;
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
public class LoadObjectRepo {
    
    public  JTree tree;
    
    public  DefaultMutableTreeNode root;
    
    public  void LoadXMLObjectRepo(String ModelXMlPATH) {
        // MappingObjectToModel.jTree1.removeAll();
       // tree.removeAll();
        root=null;
         root = new DefaultMutableTreeNode("Root");
        DefaultMutableTreeNode ChildNodes = new DefaultMutableTreeNode("Objects");
        
        
   Map<String, String> obj_list=  getObjectsfrmXML.GetLocators(ModelXMlPATH);
       // List<String> node_list = new ArrayList<String>();
   
   DefaultMutableTreeNode child[]=new DefaultMutableTreeNode[obj_list.size()];
DefaultMutableTreeNode grandChild[]= new DefaultMutableTreeNode[obj_list.size()];

int i=0;
        for (String Item : obj_list.keySet()) {
           // node_list.add(Item);
            
            child[i] = new DefaultMutableTreeNode(Item);
  grandChild[i]=new DefaultMutableTreeNode(obj_list.get(Item));
child[i].add(grandChild[i]);
 root.add(child[i]);
            
          //   ChildNodes.add(new DefaultMutableTreeNode(Item ));
          //  DefaultMutableTreeNode ChildNodes1 = new DefaultMutableTreeNode(obj_list.get(Item));
           // ChildNodes.add(ChildNodes1);
 i=i+1;
        }
       // root.add(ChildNodes);
        LoadObject();
      
    }
    
    public  void LoadObject() {
        tree=null;
        tree = new JTree(root);
       // ImageIcon imageIcon = new ImageIcon(LoadObjectRepo.class.getResource("/leaf.jpg"));
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();        
      //  renderer.setLeafIcon(imageIcon);
        
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

        MappingObjectToModel.jTree1.setModel(tree.getModel());
        
    }
    
}
