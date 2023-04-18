package GUI_components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPasswordField;

public class PasswordPlaceholderField extends JPasswordField {
    private static final int BAR_WIDTH = 20;
    private static final String EYE_OPENED_ICON_PATH = "assets/eye-icon-opened.png";
    private static final String EYE_CLOSED_ICON_PATH = "assets/eye-icon-closed.png";
    private String placeholder;

    public PasswordPlaceholderField(String placeholder) {
        super(placeholder, BAR_WIDTH);
        this.placeholder = placeholder;
        setEchoChar((char)0);
        setForeground(Color.GRAY);
        setLayout(new BorderLayout());

        JButton showPasswordButton = new JButton(new ImageIcon(EYE_CLOSED_ICON_PATH));
        showPasswordButton.addActionListener(e -> showHidePassword(showPasswordButton));
        add(showPasswordButton, BorderLayout.EAST);

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getPassword().length == 0) {
                    setText("");
                    setForeground(Color.BLACK);
                    setEchoChar('\u2022');
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (getPassword().length == 0) {
                    setForeground(Color.GRAY);
                    setEchoChar((char)0);
                    setText(placeholder);
                }
            }
        });
    }

    private void showHidePassword(JButton showPasswordButton) {
        if (getPassword().length == 0) return;
        if (getEchoChar() == '\u2022') {
            setEchoChar((char)0);
            showPasswordButton.setIcon(new ImageIcon(EYE_OPENED_ICON_PATH));
        } else {
            setEchoChar('\u2022');
            showPasswordButton.setIcon(new ImageIcon(EYE_CLOSED_ICON_PATH));
        }
    }

    @Override
    public char[] getPassword() {
        char[] text = super.getPassword();
        if (Arrays.equals(text, placeholder.toCharArray())) return new char[0];
        return text;
    }    
}
