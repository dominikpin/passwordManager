package GUI_components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class DisplayInfoPanel extends JPanel {
    private static final int BAR_WIDTH = 20;

    public DisplayInfoPanel(String key, String value) {
        setLayout(new FlowLayout());

        JLabel keyLabel = new JLabel(key + ": ");
        JTextField valueField = new JTextField(value, BAR_WIDTH);
        valueField.setEditable(false);
        JButton copyToClipboard = new JButton(new ImageIcon("assets/copy-to-clipboard-icon.png"));
        copyToClipboard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
        });
               
        add(keyLabel, FlowLayout.LEFT);
        add(valueField, FlowLayout.CENTER);
        add(copyToClipboard, FlowLayout.RIGHT);
    }

    public DisplayInfoPanel(String key, char[] value) {
        setLayout(new FlowLayout());
        
        JLabel keyLabel = new JLabel(key + ": ");
        JPasswordField valueField = new JPasswordField(new String(value), BAR_WIDTH);
        valueField.setEditable(false);
        valueField.setLayout(new BorderLayout());
        ImageIcon eyeOpened = new ImageIcon("assets/eye-icon-opened.png");
        ImageIcon eyeClosed = new ImageIcon("assets/eye-icon-closed.png");
        JButton showPasswordButton = new JButton(eyeClosed);
        showPasswordButton.addActionListener(e -> {
            if (valueField.getPassword().length == 0) {
                return;
            }
            if (valueField.getEchoChar() == '\u2022') {
                valueField.setEchoChar((char)0);
                showPasswordButton.setIcon(eyeOpened);
            } else {
                valueField.setEchoChar('\u2022');
                showPasswordButton.setIcon(eyeClosed);
            }
        });
        valueField.add(showPasswordButton, BorderLayout.EAST);

        JButton copyToClipboard = new JButton(new ImageIcon("assets/copy-to-clipboard-icon.png"));
        copyToClipboard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
        });
        
        add(keyLabel, FlowLayout.LEFT);
        add(valueField, FlowLayout.CENTER);
        add(copyToClipboard, FlowLayout.RIGHT);
    }
}
