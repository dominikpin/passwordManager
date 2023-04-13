package GUI_components;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import logic.StrongPasswordGen;

public class StrongPasswordGenDialog extends JDialog {
    
    public StrongPasswordGenDialog() {
        setTitle("Generate strong password");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Check every box you want in your password");
        JSlider numberOfChars = new JSlider(0, 4, 40, 16);
        JLabel numberOfCharsLabel = new JLabel(String.format("%s", numberOfChars.getValue()));
        
        JCheckBox letters = new JCheckBox("Letters");
        letters.setSelected(true);
        JCheckBox numbers = new JCheckBox("Numbers");
        numbers.setSelected(true);
        JCheckBox symbols = new JCheckBox("Symbols");
        numbers.setSelected(true);
        JLabel newPassword = new JLabel("Password: ");
        numberOfChars.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                newPassword.setText("New strong password: " + StrongPasswordGen.genStrongPassword(numberOfChars.getValue(), letters.isSelected(), numbers.isSelected(), symbols.isSelected()));
                numberOfCharsLabel.setText(String.format("%s", numberOfChars.getValue()));
            }
        });
        letters.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newPassword.setText("New strong password: " + StrongPasswordGen.genStrongPassword(numberOfChars.getValue(), letters.isSelected(), numbers.isSelected(), symbols.isSelected()));
                if (!letters.isSelected()) {
                    numbers.setEnabled(false);
                } else {
                    numbers.setEnabled(true);
                }
            }
        });
        numbers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newPassword.setText("New strong password: " + StrongPasswordGen.genStrongPassword(numberOfChars.getValue(), letters.isSelected(), numbers.isSelected(), symbols.isSelected()));
                if (!numbers.isSelected()) {
                    letters.setEnabled(false);
                } else {
                    letters.setEnabled(true);
                }
            }
        });
        symbols.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newPassword.setText("New strong password: " + StrongPasswordGen.genStrongPassword(numberOfChars.getValue(), letters.isSelected(), numbers.isSelected(), symbols.isSelected()));
            }
        });
        newPassword.setText("New strong password: " + StrongPasswordGen.genStrongPassword(numberOfChars.getValue(), letters.isSelected(), numbers.isSelected(), symbols.isSelected()));
        
        JButton copyToClipboard = new JButton(new ImageIcon("assets/copy-to-clipboard-icon.png"));
        copyToClipboard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StringSelection selection = new StringSelection(newPassword.getText().substring(21));
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, null);
                JOptionPane.showMessageDialog(null, "Password copied to the clipboard", "Copied to Clipboard", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        JButton refreshPassword = new JButton(new ImageIcon("assets/refresh-icon.png"));
        refreshPassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newPassword.setText("New strong password: " + StrongPasswordGen.genStrongPassword(numberOfChars.getValue(), letters.isSelected(), numbers.isSelected(), symbols.isSelected()));
            }
        });

        add(label);
        add(numberOfChars);
        add(numberOfCharsLabel);
        add(letters);
        add(numbers);
        add(symbols);
        add(newPassword);
        add(copyToClipboard);
        add(refreshPassword);

        setVisible(true);
    }
}
