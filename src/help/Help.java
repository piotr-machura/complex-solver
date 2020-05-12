package help;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.net.URL;

import java.awt.HeadlessException;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 * The singleton class HelpFrame.
 */
public class Help extends JFrame implements TreeSelectionListener {
    private static final long serialVersionUID = 1L;

    /** The singleton instance */
    static Help instance = new Help();

    /**
     * getInstance.
     *
     * HelpFrame is a singleton class, instance is accessible through a static
     * getInstance() method.
     *
     * @return the instance of HelpFrame
     */
    public static Help getInstance() {
        return instance;
    }

    /** GUI Eleemnts */
    private JEditorPane htmlPane;
    private JTree tree;

    private Help() throws HeadlessException {
        super("Help");
        this.setSize(620, 720);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        /**
         * Set up the tree structure
         */
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("All categories");

        /** Create tree nodes */
        DefaultMutableTreeNode category = null;
        DefaultMutableTreeNode article = null;

        /** Set up articles in category: How to */
        category = new DefaultMutableTreeNode("How to");
        top.add(category);

        // TODO: replace placeholder html document with real one
        article = new DefaultMutableTreeNode(new HelpArticle("Step by step", "documents/test.html"));
        category.add(article);
        // TODO: replace placeholder html document with real one
        article = new DefaultMutableTreeNode(new HelpArticle("Accuracy options", "documents/test.html"));
        category.add(article);
        // TODO: replace placeholder html document with real one
        article = new DefaultMutableTreeNode(new HelpArticle("Saving results", "documents/test.html"));
        category.add(article);
        // TODO: replace placeholder html document with real one
        article = new DefaultMutableTreeNode(new HelpArticle("GUI Elements", "documents/test.html"));
        category.add(article);

        /** Set up articles in category: Math */
        category = new DefaultMutableTreeNode("Math");
        top.add(category);

        // TODO: replace placeholder html document with real one
        article = new DefaultMutableTreeNode(new HelpArticle("Complex functions", "documents/test.html"));
        category.add(article);
        // TODO: replace placeholder html document with real one
        article = new DefaultMutableTreeNode(new HelpArticle("Roots of complex functions", "documents/test.html"));
        category.add(article);
        // TODO: replace placeholder html document with real one
        article = new DefaultMutableTreeNode(new HelpArticle("Poles of complex functions", "documents/test.html"));
        category.add(article);
        // TODO: replace placeholder html document with real one
        article = new DefaultMutableTreeNode(new HelpArticle("Winding number algorithm", "documents/test.html"));
        category.add(article);
        // TODO: replace placeholder html document with real one
        article = new DefaultMutableTreeNode(new HelpArticle("The Riemann Sphere", "documents/test.html"));
        category.add(article);

        /** Set up articles in category: Troubleshooting */
        category = new DefaultMutableTreeNode("Troubleshooting");
        top.add(category);

        // TODO: replace placeholder html document with real one
        article = new DefaultMutableTreeNode(new HelpArticle("Some roots were not found", "documents/test.html"));
        category.add(article);
        // TODO: replace placeholder html document with real one
        article = new DefaultMutableTreeNode(new HelpArticle("Graph takes a long time to load", "documents/test.html"));
        category.add(article);
        // TODO: replace placeholder html document with real one
        article = new DefaultMutableTreeNode(
                new HelpArticle("Solutions stuck at \"Solving...\"", "documents/test.html"));
        category.add(article);
        // TODO: replace placeholder html document with real one
        article = new DefaultMutableTreeNode(new HelpArticle("Graph looks wrong", "documents/test.html"));
        category.add(article);

        /** Make tree from top node */
        tree = new JTree(top);
        tree.addTreeSelectionListener(this);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        JScrollPane treeDisplay = new JScrollPane(tree);
        treeDisplay.setPreferredSize(new Dimension(600, 100));

        /**
         * Set up the html viewer
         */
        htmlPane = new JEditorPane();
        htmlPane.setEditable(false);
        try {
            // TODO: replace placeholder html document with real one
            htmlPane.setPage(getClass().getResource("documents/about.html"));
        } catch (IOException e) {
            htmlPane.setText("Could not load page: documents/about.html");
        }
        JScrollPane htmlDisplay = new JScrollPane(htmlPane);
        htmlDisplay.setPreferredSize(new Dimension(600, 600));

        this.add(treeDisplay);
        this.add(htmlDisplay);

    }

    /**
     * The class HelpArticle.
     *
     * Contains article's name and URL to a HTML document.
     */
    private class HelpArticle {
        public String articleName;
        public URL articleURL;

        public HelpArticle(String articleName, String filename) {
            this.articleName = articleName;
            this.articleURL = getClass().getResource(filename);
        }

        public String toString() {
            return this.articleName;
        }
    }

    /**
     * TreeSelectionListener implementation.
     */
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null)
            return;
        if (node.isLeaf()) {
            /** If leaf was selected -> display article document */
            HelpArticle selectedArticle = (HelpArticle) node.getUserObject();
            try {
                htmlPane.setPage(selectedArticle.articleURL);
            } catch (IOException ex) {
                htmlPane.setText("Could not load page: " + selectedArticle.articleURL);
            }
        } else if (node.getLevel() == 0) {
            /** If the root was selected -> display welcome page */
            try {
                htmlPane.setPage(getClass().getResource("documents/about.html"));
            } catch (IOException ex) {
                htmlPane.setText("Could not load page: documents/about.html");
            }
        }
    }
}