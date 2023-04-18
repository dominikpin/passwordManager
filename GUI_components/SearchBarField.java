package GUI_components;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.SwingUtilities;

public class SearchBarField extends PlaceholderField {
    private static final int SEARCH_BAR_WIDTH = 20;
    public MainFrame frame;

    public SearchBarField(MainFrame frame) {
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