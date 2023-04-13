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
import javax.swing.text.JTextComponent;

public class DisplayInfoPanel extends JPanel {

    public DisplayInfoPanel(String key, String value, boolean isPassword) {
        setLayout(new FlowLayout());

        JLabel keyLabel = new JLabel(key + ": ");
        JTextComponent valueField;
        if (!isPassword) {
            valueField = new JTextField(value);
        } else {
            valueField = new JPasswordField(value);
            valueField.setLayout(new BorderLayout());
            ImageIcon eyeOpened = new ImageIcon("assets/eye-icon-opened.png");
            ImageIcon eyeClosed = new ImageIcon("assets/eye-icon-closed.png");
            JButton showPasswordButton = new JButton(eyeClosed);
            showPasswordButton.addActionListener(e -> {
                if (valueField.getText().isEmpty()) {
                    return;
                }
                if (((JPasswordField) valueField).getEchoChar() == '\u2022') {
                    ((JPasswordField) valueField).setEchoChar((char)0);
                    showPasswordButton.setIcon(eyeOpened);
                } else {
                    ((JPasswordField) valueField).setEchoChar('\u2022');
                    showPasswordButton.setIcon(eyeClosed);
                }
            });
            add(showPasswordButton, BorderLayout    .EAST);
        }
        valueField.setEditable(false);
        JButton copyToClipboard = new JButton(new ImageIcon("assets/copy-to-clipboard-icon.png"));
        copyToClipboard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text = valueField.getText();
                StringSelection selection = new StringSelection(text);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, null);
                JOptionPane.showMessageDialog(null, key + " copied to the clipboard", "Copied to Clipboard", JOptionPane.INFORMATION_MESSAGE);
            }
        });
               
        add(keyLabel, FlowLayout.LEFT);
        add(valueField, FlowLayout.CENTER);
        add(copyToClipboard, FlowLayout.RIGHT);
    }
}
