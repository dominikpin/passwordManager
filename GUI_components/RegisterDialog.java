package GUI_components;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class RegisterDialog extends JDialog{
    
    public RegisterDialog(JFrame owner) {
        super(owner, true);
        setTitle("Register");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setSize(300, 150);
        setLocationRelativeTo(null);
        ImageIcon icon = new ImageIcon("assets/lock-icon.jpg");
        setIconImage(icon.getImage());

        PlaceholderField filePathOrUsername = new PlaceholderField("File path / Username");
        PasswordPlaceholderField password = new PasswordPlaceholderField("Password");
        PasswordPlaceholderField repeatPassword = new PasswordPlaceholderField("Repeat password");
        filePathOrUsername.addKeyListener(createKeyListener(password));
        password.addKeyListener(createKeyListener(repeatPassword));
        repeatPassword.addKeyListener(createKeyListener(filePathOrUsername));

        JButton register = new JButton("Register");
        register.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (RegisterDialog.this.checkRegistration(filePathOrUsername.getText(), password.getPassword(), repeatPassword.getPassword())) {
                    try {
                        File file = new File("./" + filePathOrUsername.getText() + ".txt");
                        file.createNewFile();
                        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                        writer.write(Encryption.mainPasswordEncryption(password.getPassword()));
                        writer.newLine();
                        writer.close();
                        JOptionPane.showMessageDialog(RegisterDialog.this, "Successfully added a new user.\nMake sure to remember your password.\nYour data will be saved in <username>.txt file so have this file in same folder as this program when you want to access your passwords", "New user added", JOptionPane.CLOSED_OPTION);
                        dispose();
                    } catch (IOException | NoSuchAlgorithmException e1) {
                        System.out.println("SOMETHING IS WRONG");
                    }
                }
            }
        });

        add(filePathOrUsername);
        add(password);
        add(repeatPassword);
        add(register);
        setVisible(true);
    }

    public boolean checkRegistration(String filePath, char[] password1, char[] password2) {
        if (filePath.isEmpty() || password1.length == 0 || password2.length == 0) {
            JOptionPane.showMessageDialog(this, "Can't have any inputs empty", "Could not add new password", JOptionPane.CLOSED_OPTION);
            return false;
        }
        if (!Arrays.equals(password1, password2)) {
            JOptionPane.showMessageDialog(this, "First and second passwords don't match", "Could not add new password", JOptionPane.CLOSED_OPTION);
            return false;
        }
        File file = new File("./" + filePath + ".txt");
        if (file.exists()) {
            JOptionPane.showMessageDialog(this, "Username already in use", "Could not add new password", JOptionPane.CLOSED_OPTION);
            return false;
        }
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
