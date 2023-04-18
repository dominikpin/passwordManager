package GUI_components;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import logic.Encryption;

public class RegisterDialog extends JDialog {
    private static final int WINDOW_WIDTH = 300;
    private static final int WINDOW_HEIGHT = 150;
    private static final String LOCK_ICON_PATH = "assets/lock-icon.png";

    public RegisterDialog(JFrame frame) {
        super(frame, true);
        setTitle("Register");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(frame);
        setIconImage(new ImageIcon(LOCK_ICON_PATH).getImage());

        PlaceholderField filePathOrUsername = new PlaceholderField("File path / Username");
        PasswordPlaceholderField password = new PasswordPlaceholderField("Password");
        PasswordPlaceholderField repeatPassword = new PasswordPlaceholderField("Repeat password");
        filePathOrUsername.addKeyListener(createKeyListener(password));
        password.addKeyListener(createKeyListener(repeatPassword));
        repeatPassword.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) handleRegistration(filePathOrUsername, password, repeatPassword);
            }
        });
        add(filePathOrUsername);
        add(password);
        add(repeatPassword);

        JButton register = new JButton("Register");
        register.addActionListener(e -> handleRegistration(filePathOrUsername, password, repeatPassword));
        JButton backButton = new JButton("Go Back");
        backButton.addActionListener(e -> {
            frame.requestFocus();
            dispose();
        });
        add(register);
        add(backButton);

        setVisible(true);
    }

    private void handleRegistration(PlaceholderField filePathOrUsername, PasswordPlaceholderField password, PasswordPlaceholderField repeatPassword) {
        // TODO add some password restrictions
        if (RegisterDialog.this.checkRegistration(filePathOrUsername.getText(), password.getPassword(), repeatPassword.getPassword())) {
            try {
                File file = new File("./" + filePathOrUsername.getText() + ".txt");
                file.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(Encryption.mainPasswordEncryption(password.getPassword()));
                writer.newLine();
                writer.close();
                JOptionPane.showMessageDialog(
                    RegisterDialog.this, 
                    "Successfully added a new user.\nMake sure to remember your password.\nYour data will be saved in <username>.txt file so have this file in same folder as this program when you want to access your passwords", 
                    "New user added", 
                    JOptionPane.CLOSED_OPTION);
                dispose();
            } catch (IOException | NoSuchAlgorithmException e1) {
                System.out.println("SOMETHING IS WRONG");
            }
        }
    }

    private boolean checkRegistration(String filePath, char[] password1, char[] password2) {
        if (filePath.isEmpty() || password1.length == 0 || password2.length == 0) {
            showMessage("Can't have any inputs empty");
            return false;
        }
        if (!Arrays.equals(password1, password2)) {
            showMessage("First and second passwords don't match");
            return false;
        }
        if (new File("./" + filePath + ".txt").exists()) {
            showMessage("Username already in use");
            return false;
        }
        return true;
    }
    
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(
            this, message, "Could not add new password", JOptionPane.CLOSED_OPTION);
    }    

    private KeyAdapter createKeyListener(Component nextComponent) {
        return new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) nextComponent.requestFocus();
            }
        };
    }
}
