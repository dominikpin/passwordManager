package GUI_components;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPasswordField;

public class PasswordPlaceholderField extends JPasswordField{
    private String placeholder;
    
    public PasswordPlaceholderField(String placeholder) {
        super(placeholder);
        this.placeholder = placeholder;
        setEchoChar((char)0);
        setForeground(Color.GRAY);
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText("");
                    setForeground(Color.BLACK);
                    setEchoChar('\u2022');
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setForeground(Color.GRAY);
                    setEchoChar((char)0);
                    setText(placeholder);
                }
            }
        });
    }

    @Override
    public String getText() {
        String text = super.getText();
        if (text.equals(placeholder)) {
            return "";
        }
        return text;
    }

    // TODO add button on the far right. if you press it it reveals password
}
