package GUI_components;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;

import logic.StrongPasswordGen;

public class StrongPasswordGenDialog extends JDialog {
    private static final int WINDOW_WIDTH = 450;
    private static final int WINDOW_HEIGHT = 300;
    private static final String PASSWORD_PREFIX = "New strong password: ";
    private static final String COPY_ICON_PATH = "assets/copy-to-clipboard-icon.png";
    private static final String PASSWORD_GEN_ICON_PATH = "assets/refresh-icon.png";
    private static final String REFRESH_ICON_PATH = "assets/refresh-icon.png";

    JLabel passwordLabel;
    
    public StrongPasswordGenDialog(JFrame frame) {
        setTitle("Generate strong password");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(frame);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setIconImage(new ImageIcon(PASSWORD_GEN_ICON_PATH).getImage());

        JSlider numberOfChars = new JSlider(0, 4, 40, 16);
        JLabel numberOfCharsLabel = new JLabel(String.format("%s", numberOfChars.getValue()));
        add(numberOfChars);
        add(numberOfCharsLabel);
        
        JCheckBox letters = new JCheckBox("Letters", true);
        JCheckBox numbers = new JCheckBox("Numbers", true);
        JCheckBox symbols = new JCheckBox("Symbols", true);
        passwordLabel = new JLabel();
        numberOfChars.addChangeListener(e -> {
            updatePasswordLabel(numberOfChars, letters, numbers, symbols);
            numberOfCharsLabel.setText(String.format("%s", numberOfChars.getValue()));
        });
        letters.addActionListener(e -> {
            updatePasswordLabel(numberOfChars, letters, numbers, symbols);
            numbers.setEnabled(letters.isSelected());
        });
        numbers.addActionListener(e -> {
            updatePasswordLabel(numberOfChars, letters, numbers, symbols);
            letters.setEnabled(numbers.isSelected());
        });
        symbols.addActionListener(e -> updatePasswordLabel(numberOfChars, letters, numbers, symbols));
        updatePasswordLabel(numberOfChars, letters, numbers, symbols);
        add(letters);
        add(numbers);
        add(symbols);
        add(passwordLabel);
        
        JButton copyToClipboard = new JButton(new ImageIcon(COPY_ICON_PATH));
        copyToClipboard.addActionListener(e -> copyToClipboard());
        JButton refreshPassword = new JButton(new ImageIcon(REFRESH_ICON_PATH));
        refreshPassword.addActionListener(e -> updatePasswordLabel(numberOfChars, letters, numbers, symbols));
        add(copyToClipboard);
        add(refreshPassword);

        JButton backButton = new JButton("Go Back");
        backButton.addActionListener(e -> {
            frame.requestFocus();
            dispose();
        });
        add(backButton);

        setVisible(true);
    }

    private void updatePasswordLabel(JSlider numberOfChars, JCheckBox letters, JCheckBox numbers, JCheckBox symbols) {
        passwordLabel.setText(PASSWORD_PREFIX + StrongPasswordGen.genStrongPassword(numberOfChars.getValue(), letters.isSelected(), numbers.isSelected(), symbols.isSelected()));
    }

    private void copyToClipboard() {
        StringSelection selection = new StringSelection(passwordLabel.getText().substring(PASSWORD_PREFIX.length()));
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);
        JOptionPane.showMessageDialog(
                StrongPasswordGenDialog.this, 
                "Password copied to the clipboard", 
                "Copied to Clipboard", 
                JOptionPane.INFORMATION_MESSAGE);
    }
}
