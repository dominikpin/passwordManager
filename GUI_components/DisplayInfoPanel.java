package GUI_components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class DisplayInfoPanel extends JPanel {
    private static final int BAR_WIDTH = 20;
    private static final String COPY_ICON_PATH = "assets/copy-to-clipboard-icon.png";
    private static final String EYE_OPENED_ICON_PATH = "assets/eye-icon-opened.png";
    private static final String EYE_CLOSED_ICON_PATH = "assets/eye-icon-closed.png";

    public DisplayInfoPanel(String key, String value) {
        setLayout(new FlowLayout());

        JLabel keyLabel = new JLabel(key + ": ");
        JTextField valueField = new JTextField(value, BAR_WIDTH);
        valueField.setEditable(false);
        JButton copyToClipboard = new JButton(new ImageIcon(COPY_ICON_PATH));
        copyToClipboard.addActionListener(e -> copyToClipboard(valueField, key));
        add(keyLabel, FlowLayout.LEFT);
        add(valueField, FlowLayout.CENTER);
        add(copyToClipboard, FlowLayout.RIGHT);
    }

    private void copyToClipboard(JTextField valueField, String key) {
        String text = valueField.getText();
        StringSelection selection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);
        JOptionPane.showMessageDialog(
            DisplayInfoPanel.this, 
            key + " copied to the clipboard", 
            "Copied to Clipboard", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void copyToClipboard(JPasswordField valueField, String key) {
        char[] text = valueField.getPassword();
        StringSelection selection = new StringSelection(new String(text));
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);
        JOptionPane.showMessageDialog(
            DisplayInfoPanel.this, key + 
            " copied to the clipboard", 
            "Copied to Clipboard", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void showHidePassword(JButton showPasswordButton, JPasswordField valueField) {
        if (valueField.getPassword().length == 0) return;
        if (valueField.getEchoChar() == '\u2022') {
            valueField.setEchoChar((char)0);
            showPasswordButton.setIcon(new ImageIcon(EYE_OPENED_ICON_PATH));
        } else {
            valueField.setEchoChar('\u2022');
            showPasswordButton.setIcon(new ImageIcon(EYE_CLOSED_ICON_PATH));
        }
    }

    public DisplayInfoPanel(String key, char[] value) {
        setLayout(new FlowLayout());
        
        JLabel keyLabel = new JLabel(key + ": ");
        JPasswordField valueField = new JPasswordField(new String(value), BAR_WIDTH);
        valueField.setEditable(false);
        valueField.setLayout(new BorderLayout());
        JButton showPasswordButton = new JButton(new ImageIcon(EYE_CLOSED_ICON_PATH));
        showPasswordButton.addActionListener(e -> showHidePassword(showPasswordButton, valueField));
        valueField.add(showPasswordButton, BorderLayout.EAST);

        JButton copyToClipboard = new JButton(new ImageIcon("assets/copy-to-clipboard-icon.png"));
        copyToClipboard.addActionListener(e -> copyToClipboard(valueField, key));
        
        add(keyLabel, FlowLayout.LEFT);
        add(valueField, FlowLayout.CENTER);
        add(copyToClipboard, FlowLayout.RIGHT);
    }
}
