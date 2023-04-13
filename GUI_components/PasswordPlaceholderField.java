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
    private String placeholder;

    public PasswordPlaceholderField(String placeholder) {
        super(placeholder);
        this.placeholder = placeholder;
        setEchoChar((char)0);
        setForeground(Color.GRAY);
        setLayout(new BorderLayout());

        ImageIcon eyeOpened = new ImageIcon("assets/eye-icon-opened.png");
        ImageIcon eyeClosed = new ImageIcon("assets/eye-icon-closed.png");
        JButton showPasswordButton = new JButton(eyeClosed);
        showPasswordButton.addActionListener(e -> {
            if (getPassword().length == 0) {
                return;
            }
            if (getEchoChar() == '\u2022') {
                setEchoChar((char)0);
                showPasswordButton.setIcon(eyeOpened);
            } else {
                setEchoChar('\u2022');
                showPasswordButton.setIcon(eyeClosed);
            }
        });
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

    @Override
    public char[] getPassword() {
        char[] text = super.getPassword();
        if (Arrays.equals(text, placeholder.toCharArray())) {
            return new char[0];
        }
        return text;
    }    
}
