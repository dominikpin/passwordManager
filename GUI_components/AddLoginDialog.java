package GUI_components;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import logic.Encryption;
import logic.LoginInfo;

public class AddLoginDialog extends JDialog {
    private static final int WINDOW_WIDTH = 300;
    private static final int WINDOW_HEIGHT = 200;
    private static final String ADD_PASSWORD_ICON_PATH = "assets/password-add-icon.png";
    
    public AddLoginDialog(MainFrame frame, char[] mainPassword) {
        setTitle("Add new login");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(frame);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setIconImage(new ImageIcon(ADD_PASSWORD_ICON_PATH).getImage());

        PlaceholderField email = new PlaceholderField("Email");
        PlaceholderField username = new PlaceholderField("Username");
        PasswordPlaceholderField password = new PasswordPlaceholderField("Password");
        PlaceholderField domain = new PlaceholderField("Domain");

        email.addKeyListener(createKeyListener(username));
        username.addKeyListener(createKeyListener(password));
        password.addKeyListener(createKeyListener(domain));
        domain.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) handleAddPassword(email, username, password, domain, mainPassword, frame);
            }
        });
        add(email);
        add(username);
        add(password);
        add(domain);

        JButton submitButton = new JButton("Add");
        submitButton.addActionListener(e -> handleAddPassword(email, username, password, domain, mainPassword, frame));
        JButton backButton = new JButton("Go Back");
        backButton.addActionListener(e -> {
            frame.requestFocus();
            dispose();
        });
        add(submitButton);
        add(backButton);

        setVisible(true);
    }

    private void handleAddPassword(PlaceholderField email, PlaceholderField username, PasswordPlaceholderField password, PlaceholderField domain, char[] mainPassword, MainFrame frame) {
        if (checkNewPassword(email.getText(), username.getText(), password.getPassword(), domain.getText())) {
            try {
                LoginInfo newLoginInfo = new LoginInfo(
                    email.getText(), 
                    username.getText(), 
                    Encryption.encryptPassword(password.getPassword(), mainPassword), 
                    domain.getText(), 
                    LoginInfo.getIconAndSaveIt(domain.getText()));
                frame.addOrRemoveLoginInfo(newLoginInfo, true);
                JOptionPane.showMessageDialog(
                    AddLoginDialog.this, 
                    "Successfully added a new password.", 
                    "New password added", 
                    JOptionPane.CLOSED_OPTION);
                dispose();
            } catch (Exception e1) {
                System.out.println("SOMETHING IS WRONG");
            }
        }
    }

    private boolean checkNewPassword(String email, String username, char[] passwordChar, String domain) {
        if ((email.isEmpty() && username.isEmpty()) || passwordChar.length == 0) {
            JOptionPane.showMessageDialog(
                AddLoginDialog.this, 
                "One of email and username have to be filled in and password has to be filled in.", 
                "Password couldn't be added", 
                JOptionPane.CLOSED_OPTION);
            return false;
        }
        if (passwordChar.length < 8) {
            int option = JOptionPane.showConfirmDialog(
                AddLoginDialog.this, 
                "Your password should be at least 8 characters long. Are you sure you want to add this password?", 
                "Password length", 
                JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.NO_OPTION) {
                return false;
            }
        }
        String passwordString = new String(passwordChar);
        int counter = 0;
        if (passwordString.matches(".*\\d.*")) {
            counter++;
        }
        if (passwordString.matches(".*[a-z].*")) {
            counter++;
        }
        if (passwordString.matches(".*[A-Z].*")) {
            counter++;
        }
        if (passwordString.matches(".*\\p{P}.*")) {
            counter++;
        }
        if (counter < 3) {
            int option = JOptionPane.showConfirmDialog(
                AddLoginDialog.this, 
                "Your password should contain at least 3 out of these 4 requirements: lowercase letters, uppercase letters, numbers, and symbols. Are you sure you want to add this password?", 
                "Password length", 
                JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.NO_OPTION) {
                return false;
            }
        }
        // TODO check if new password is same as any of existing ones
        return true;
    }

    private KeyAdapter createKeyListener(Component nextComponent) {
        return new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    nextComponent.requestFocus();
                }
            }
        };
    }
}
