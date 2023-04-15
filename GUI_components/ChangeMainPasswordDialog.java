package GUI_components;

import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import logic.Encryption;
import logic.LoginInfo;

public class ChangeMainPasswordDialog extends JDialog {
    private static MainFrame frame;

    public ChangeMainPasswordDialog(String filePath, MainFrame frame) {
        ChangeMainPasswordDialog.frame = frame;
        setTitle("Change main password");
        setSize(300, 150);
        setLocationRelativeTo(frame);
        setLayout(new FlowLayout());

        PasswordPlaceholderField oldPassword = new PasswordPlaceholderField("Old password");
        PasswordPlaceholderField newPassword = new PasswordPlaceholderField("New password");
        PasswordPlaceholderField newPasswordRepeated = new PasswordPlaceholderField("New password repeated");
        JButton changePassword = new JButton("Change password");
        changePassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!checkPasswordChange(filePath, oldPassword.getPassword(), newPassword.getPassword(), newPasswordRepeated.getPassword())) {
                        return;
                    }
                } catch (HeadlessException | NoSuchAlgorithmException | IOException e1) {
                    System.out.println("SOMETHING IS WRONG");
                }
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
        });

        add(oldPassword);
        add(newPassword);
        add(newPasswordRepeated);
        add(changePassword);

        setVisible(true);
    }

    public static boolean checkPasswordChange(String filePath, char[] oldPassword, char[] newPassword, char[] newPasswordRepeated) throws HeadlessException, NoSuchAlgorithmException, IOException {
        if (!LoginFrame.checkPassword(filePath, Encryption.mainPasswordEncryption(oldPassword))) {
            JOptionPane.showMessageDialog(
                frame, 
                "Old password is incorrect", 
                "Could not change password", 
                JOptionPane.CLOSED_OPTION);
            return false;
        }
        if (oldPassword.length == 0 || newPassword.length == 0 || newPasswordRepeated.length == 0) {
            JOptionPane.showMessageDialog(
                frame, 
                "Can't have any inputs empty", 
                "Could not change password", 
                JOptionPane.CLOSED_OPTION);
            return false;
        }
        if (!Arrays.equals(newPassword, newPasswordRepeated)) {
            JOptionPane.showMessageDialog(
                frame, 
                "First and second new passwords don't match", 
                "Could not change password", 
                JOptionPane.CLOSED_OPTION);
            return false;
        }
        if (Arrays.equals(newPassword, oldPassword)) {
            JOptionPane.showMessageDialog(
                frame, 
                "Old and new password cannot be the same", 
                "Could not change password", 
                JOptionPane.CLOSED_OPTION);
            return false;
        }
        return true;
    }

    public static void changeMainPassword(String filePath, char[] oldPassword, char[] newPassword) throws Exception {
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
}
