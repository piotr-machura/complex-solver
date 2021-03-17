package gui;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * the class JTreeExpander.
 *
 * Expands the JTree and prevents it from collapsing.
 *
 * @Credit https://www.logicbig.com/tutorials/java-swing/tree-disallow-collapse.html
 */
public class JTreeExpander {
    public static void setTreeExpandedState(JTree tree, boolean expanded) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getModel().getRoot();
        setNodeExpandedState(tree, node, expanded);
    }

    public static void setNodeExpandedState(JTree tree, TreeNode node, boolean expanded) {
        ArrayList<? extends TreeNode> list = Collections.list(node.children());
        for (TreeNode treeNode : list) {
            setNodeExpandedState(tree, treeNode, expanded);
        }
        DefaultMutableTreeNode mutableTreeNode = (DefaultMutableTreeNode) node;
        if (!expanded && mutableTreeNode.isRoot()) {
            return;
        }
        TreePath path = new TreePath(mutableTreeNode.getPath());
        if (expanded) {
            tree.expandPath(path);
        } else {
            tree.collapsePath(path);
        }
    }

    public static void makeTreeUnCollapsible(JTree tree) {
        tree.addTreeWillExpandListener(new TreeWillExpandListener() {
            @Override
            public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {

            }

            @Override
            public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
                throw new ExpandVetoException(event, "Collapsing tree not allowed");
            }
        });
    }
}