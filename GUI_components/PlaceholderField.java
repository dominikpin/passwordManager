package GUI_components;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;

public class PlaceholderField extends JTextField {
    private static final int BAR_WIDTH = 20;
    private String placeholder;

    public PlaceholderField(String placeholder) {
        super(placeholder, BAR_WIDTH);
        this.placeholder = placeholder;
        setForeground(Color.GRAY);
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText("");
                    setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setForeground(Color.GRAY);
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
}