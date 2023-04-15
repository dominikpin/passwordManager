package GUI_components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import logic.Encryption;
import logic.LoginInfo;

public class ShowInfoDialog extends JDialog {
    private static boolean edit = false;
    private static MainFrame frame;
    private static JLabel iconLabel;

    public ShowInfoDialog(LoginInfo login, char[] mainPassword, String filePath, JFrame frame) throws Exception {
        ShowInfoDialog.frame = (MainFrame) frame;
        setTitle("Info display");
        setSize(400, 300);
        setLocationRelativeTo(frame);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        File imageFile = new File(login.getIconPath());
        if (!imageFile.exists()) {
            LoginInfo.getIconAndSaveIt(login.getDomain());
        }

        iconLabel = new JLabel(new ImageIcon(login.getIconPath()));

        DisplayInfoPanel domain = new DisplayInfoPanel("Domain", login.getDomain().isEmpty() ? "/" : login.getDomain());
        DisplayInfoPanel email = new DisplayInfoPanel("Email", login.getEmail().isEmpty() ? "/" : login.getEmail());
        DisplayInfoPanel username = new DisplayInfoPanel("Username", login.getUsername().isEmpty() ? "/" : login.getUsername());
        DisplayInfoPanel password = new DisplayInfoPanel("Password", Encryption.decryptPassword(login.getPassword(), mainPassword));

        JButton editButton = new JButton("Edit info");
        editButton.addActionListener((ActionListener) new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JTextField domainField = (JTextField) domain.getComponent(1);
                JTextField emailField = (JTextField) email.getComponent(1);
                JTextField usernameField = (JTextField) username.getComponent(1);
                JPasswordField passwordField = (JPasswordField) password.getComponent(1);
                if (edit) {
                    changeLogin(login, emailField.getText(), usernameField.getText(), passwordField.getPassword(), domainField.getText(), filePath, mainPassword);
                }
                edit = !edit;
                ((JTextField)domain.getComponent(1)).setEditable(edit);
                ((JTextField)email.getComponent(1)).setEditable(edit);
                ((JTextField)username.getComponent(1)).setEditable(edit);
                ((JPasswordField)password.getComponent(1)).setEditable(edit);
            }
        });

        JButton backButton = new JButton("Go Back");
        backButton.addActionListener((ActionListener) new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        add(iconLabel);
        add(domain);
        add(email);
        add(username);
        add(password);
        add(editButton);
        add(backButton);

        setVisible(true);
    }

    public void changeLogin(LoginInfo login, String email, String username, char[] password, String domain, String filePath, char[] mainPassword) {
        // TODO fix message creation
        String message = "Are you sure you want to change ";
        message += domain.equals(login.getDomain()) ? "" : "domain, ";
        message += email.equals(login.getEmail()) ? "" : "email, ";
        message += username.equals(login.getUsername()) ? "" : "username, ";
        try {
            message += Arrays.equals(password, Encryption.decryptPassword(login.getPassword(), mainPassword)) ? "" : "password";
        } catch (Exception e1) {
            System.out.println("SOMETHING IS WRONG");
        }
        if (!message.equals("Are you sure you want to change ")) {
            int option = JOptionPane.showConfirmDialog(
                ShowInfoDialog.this, 
                message, 
                "Change password", 
                JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    login.editLogin(email, username, password, domain, filePath, mainPassword);
                    frame.resetCardPanel();
                    iconLabel.setIcon(new ImageIcon(login.getIconPath()));
                } catch (Exception e1) {
                    System.out.println("SOMETHING IS WRONG");
                }
            }
        } else {
            JOptionPane.showMessageDialog(
                ShowInfoDialog.this, 
                "Nothing changed", 
                "No changes made", 
                JOptionPane.CLOSED_OPTION);
        }
    }
}
