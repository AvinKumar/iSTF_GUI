/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.objrepo;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author yanamalp
 */
public class FindNode {
    
    public final DefaultMutableTreeNode findNode(String searchString) {

            List<DefaultMutableTreeNode> searchNodes = getSearchNodes((DefaultMutableTreeNode)MappingObjectToModel.jTree1.getModel().getRoot());
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)MappingObjectToModel.jTree1.getLastSelectedPathComponent();

            DefaultMutableTreeNode foundNode = null;
            int bookmark = -1;

            if( currentNode != null ) {
                for(int index = 0; index < searchNodes.size(); index++) {
                    if( searchNodes.get(index) == currentNode ) {
                        bookmark = index;
                        break;
                    }
                }
            }

            for(int index = bookmark + 1; index < searchNodes.size(); index++) {    
                if(searchNodes.get(index).toString().toLowerCase().contains(searchString.toLowerCase())) {
                    foundNode = searchNodes.get(index);
                    break;
                }
            }

            if( foundNode == null ) {
                for(int index = 0; index <= bookmark; index++) {    
                    if(searchNodes.get(index).toString().toLowerCase().contains(searchString.toLowerCase())) {
                        foundNode = searchNodes.get(index);
                        break;
                    }
                }
            }
            return foundNode;
        }   

        private final List<DefaultMutableTreeNode> getSearchNodes(DefaultMutableTreeNode root) {
            List<DefaultMutableTreeNode> searchNodes = new ArrayList<DefaultMutableTreeNode>();

            Enumeration<?> e = root.preorderEnumeration();
            while(e.hasMoreElements()) {
                searchNodes.add((DefaultMutableTreeNode)e.nextElement());
            }
            return searchNodes;
        }

       


//        public void actionPerformed(ActionEvent e) {
//            String search = text.getText();
//                if(search.trim().length() > 0 ) {
//
//                DefaultMutableTreeNode node = findNode(search);                
//                if( node != null ) {
//                    TreePath path = new TreePath(node.getPath());
//                    MappingObjectToModel.jTree1.setSelectionPath(path);
//                    MappingObjectToModel.jTree1.scrollPathToVisible(path);
//                }  
//            }
//        }

    
}
