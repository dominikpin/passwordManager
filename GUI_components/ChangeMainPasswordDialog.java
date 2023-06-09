package GUI_components;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import logic.Encryption;
import logic.LoginInfo;

public class ChangeMainPasswordDialog extends JDialog {
    private static final int WINDOW_WIDTH = 300;
    private static final int WINDOW_HEIGHT = 150;
    private static final String CHANGE_PASSWORD_ICON_PATH = "assets/password-change-icon.png";

    private static MainFrame frame;

    public ChangeMainPasswordDialog(String filePath, MainFrame frame) {
        ChangeMainPasswordDialog.frame = frame;
        setTitle("Change main password");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(frame);
        setLayout(new FlowLayout());
        setIconImage(new ImageIcon(CHANGE_PASSWORD_ICON_PATH).getImage());

        PasswordPlaceholderField oldPassword = new PasswordPlaceholderField("Old password");
        PasswordPlaceholderField newPassword = new PasswordPlaceholderField("New password");
        PasswordPlaceholderField newPasswordRepeated = new PasswordPlaceholderField("New password repeated");
        oldPassword.addKeyListener(createKeyListener(newPassword));
        newPassword.addKeyListener(createKeyListener(newPasswordRepeated));
        newPasswordRepeated.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) handlePasswordChange(filePath, oldPassword, newPassword, newPasswordRepeated);
            }
        });
        add(oldPassword);
        add(newPassword);
        add(newPasswordRepeated);

        JButton changePassword = new JButton("Change password");
        changePassword.addActionListener(e -> handlePasswordChange(filePath, oldPassword, newPassword, newPasswordRepeated));
        add(changePassword);

        setVisible(true);
    }

    private void handlePasswordChange(String filePath, PasswordPlaceholderField oldPassword, PasswordPlaceholderField newPassword, PasswordPlaceholderField newPasswordRepeated) {
        try {
            if (!checkPasswordChange(filePath, oldPassword.getPassword(), newPassword.getPassword(), newPasswordRepeated.getPassword())) return;
        } catch (HeadlessException | NoSuchAlgorithmException | IOException e1) {
            System.out.println("SOMETHING IS WRONG");
        }

        // TODO add some restriction for password
        int option = JOptionPane.showConfirmDialog(
            ChangeMainPasswordDialog.this, 
            "Are you sure you want to change the main password?\nThis may take a second.", 
            "Change password", 
            JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                changeMainPassword(filePath, oldPassword.getPassword(), newPassword.getPassword());
            } catch (Exception e1) {
                System.out.println("SOMETHING IS WRONG");
            }
            dispose();
        }
    }

    private boolean checkPasswordChange(String filePath, char[] oldPassword, char[] newPassword, char[] newPasswordRepeated) throws HeadlessException, NoSuchAlgorithmException, IOException {
        if (!LoginFrame.checkPassword(filePath, Encryption.mainPasswordEncryption(oldPassword))) {
            showMessage("Old password is incorrect");
            return false;
        }
        if (oldPassword.length == 0 || newPassword.length == 0 || newPasswordRepeated.length == 0) {
            showMessage("Can't have any inputs empty");
            return false;
        }
        if (!Arrays.equals(newPassword, newPasswordRepeated)) {
            showMessage("First and second new passwords don't match");
            return false;
        }
        if (Arrays.equals(newPassword, oldPassword)) {
            showMessage("Old and new password cannot be the same");
            return false;
        }
        return true;
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(
            this, message, "Could not change password", JOptionPane.CLOSED_OPTION);
    }   

    private static void changeMainPassword(String filePath, char[] oldPassword, char[] newPassword) throws Exception {
        File file = new File(filePath);
        FileReader fileReader = new FileReader(file);

        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder editedContent = new StringBuilder();
        String line;
        boolean isFirstElement = true;
        while ((line = bufferedReader.readLine()) != null) {
            if (isFirstElement) {
                line = Encryption.mainPasswordEncryption(newPassword);
                isFirstElement = false;
            } else {
                String[] args = line.split(",");
                args[2] = Encryption.encryptPassword(Encryption.decryptPassword(args[2], oldPassword), newPassword);
                LoginInfo loginInfo = new LoginInfo(args[0], args[1], args[2], args[3], args[4]);
                line = loginInfo.toString();
            }
            editedContent.append(line);
            editedContent.append(System.lineSeparator());
        }
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(editedContent.toString());
        frame.setNewMainPassword(newPassword);

        bufferedReader.close();
        fileReader.close();
        fileWriter.close();
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
