package gui;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import java.net.URL;

import java.awt.HeadlessException;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Desktop;

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
        this.setSize(620, 760);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        /**
         * Set up the tree structure
         */
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("All categories");

        /** Make tree from top node */
        tree = new JTree(top);
        tree.addTreeSelectionListener(this);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        JScrollPane treeDisplay = new JScrollPane(tree);
        treeDisplay.setPreferredSize(new Dimension(600, 100));

        /** Create tree nodes */
        DefaultMutableTreeNode category = null;
        DefaultMutableTreeNode article = null;

        /** Set up articles in category: How to */
        category = new DefaultMutableTreeNode("How to");
        top.add(category);

        article = new DefaultMutableTreeNode(new HelpArticle("Step by step", "docs/step-by-step.html"));
        category.add(article);
        article = new DefaultMutableTreeNode(new HelpArticle("Range options", "docs/range-options.html"));
        category.add(article);
        article = new DefaultMutableTreeNode(new HelpArticle("Accuracy options", "docs/accuracy-options.html"));
        category.add(article);
        article = new DefaultMutableTreeNode(new HelpArticle("Saving results", "docs/saving-results.html"));
        category.add(article);

        /** Set up articles in category: Troubleshooting */
        category = new DefaultMutableTreeNode("Troubleshooting");
        top.add(category);

        // TODO: replace placeholder html document with real one
        article = new DefaultMutableTreeNode(new HelpArticle("Some roots were not found", "docs/roots-not-found.html"));
        category.add(article);
        // TODO: replace placeholder html document with real one
        article = new DefaultMutableTreeNode(new HelpArticle("Graph looks wrong", "docs/test.html"));
        category.add(article);
        // TODO: replace placeholder html document with real one
        article = new DefaultMutableTreeNode(new HelpArticle("Graph takes a long time to load", "docs/test.html"));
        category.add(article);
        // TODO: replace placeholder html document with real one
        article = new DefaultMutableTreeNode(new HelpArticle("Solutions stuck at \"Solving...\"", "docs/test.html"));
        category.add(article);

        /** Set up articles in category: Math */
        category = new DefaultMutableTreeNode("Math");
        top.add(category);

        // TODO: replace placeholder html document with real one
        article = new DefaultMutableTreeNode(new HelpArticle("Complex functions", "docs/test.html"));
        category.add(article);
        // TODO: replace placeholder html document with real one
        article = new DefaultMutableTreeNode(new HelpArticle("Roots of complex functions", "docs/test.html"));
        category.add(article);
        // TODO: replace placeholder html document with real one
        article = new DefaultMutableTreeNode(new HelpArticle("Poles of complex functions", "docs/test.html"));
        category.add(article);
        // TODO: replace placeholder html document with real one
        article = new DefaultMutableTreeNode(new HelpArticle("Winding number algorithm", "docs/test.html"));
        category.add(article);
        // TODO: replace placeholder html document with real one
        article = new DefaultMutableTreeNode(new HelpArticle("The Riemann Sphere", "docs/test.html"));
        category.add(article);

        /**
         * Set up the html viewer
         */
        htmlPane = new JEditorPane();
        htmlPane.setEditable(false);
        htmlPane.setEditorKit(new HTMLEditorKit());
        /**
         * Make links clickable - taken from
         * https://docs.oracle.com/javase/8/docs/api/javax/swing/JEditorPane.html
         */
        htmlPane.addHyperlinkListener(new HyperlinkListener() {

            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    JEditorPane pane = (JEditorPane) e.getSource();
                    if (e instanceof HTMLFrameHyperlinkEvent) {
                        HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent) e;
                        HTMLDocument doc = (HTMLDocument) pane.getDocument();
                        doc.processHTMLFrameHyperlinkEvent(evt);
                    } else {
                        try {
                            /** If possible to open link in browser -> open */
                            if (Desktop.isDesktopSupported()) {
                                Desktop.getDesktop().browse(e.getURL().toURI());
                            } else {
                                /** Provide the user with link to open themselves */
                                JOptionPane.showMessageDialog(null,
                                        "Opening links in browser nor supported.\nLink: " + e.getURL(), "ERROR",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }
            }
        });
        try {
            htmlPane.setPage(getClass().getResource("docs/README.html"));
        } catch (IOException e) {
            htmlPane.setText("Could not load page: docs/README.html");
        }
        JScrollPane htmlDisplay = new JScrollPane(htmlPane);
        htmlDisplay.setPreferredSize(new Dimension(600, 600));
        htmlDisplay.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        htmlDisplay.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

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
                htmlPane.setPage(getClass().getResource("docs/README.html"));
            } catch (IOException ex) {
                htmlPane.setText("Could not load page: docs/README.html");
            }
        }
    }
}