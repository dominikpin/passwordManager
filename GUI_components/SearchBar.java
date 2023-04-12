package GUI_components;

import javax.swing.*;
import java.awt.event.*;

public class SearchBar extends PlaceholderField {
    private static final int SEARCH_BAR_WIDTH = 20;
    public Frame frame;

    public SearchBar(Frame frame) {
        super("Search");
        this.frame = frame;
        setColumns(SEARCH_BAR_WIDTH);

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                performSearch();
            }
        });
    }

    private void performSearch() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                String query = getText();
                frame.setSearchBar(query);
            }
        });
    }
}