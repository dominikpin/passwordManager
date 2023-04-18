package GUI_components;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 300;
    private static final String SHOW_INFO_ICON_PATH = "assets/info-icon.png";
    private static MainFrame frame;
    private static JLabel iconLabel;

    public ShowInfoDialog(LoginInfo login, char[] mainPassword, String filePath, JFrame frame) throws Exception {
        ShowInfoDialog.frame = (MainFrame) frame;
        setTitle("Info display");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(frame);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setIconImage(new ImageIcon(SHOW_INFO_ICON_PATH).getImage());

        File imageFile = new File(login.getIconPath());
        if (!imageFile.exists()) LoginInfo.getIconAndSaveIt(login.getDomain());
        iconLabel = new JLabel(new ImageIcon(login.getIconPath()));
        add(iconLabel);

        DisplayInfoPanel domain = new DisplayInfoPanel("Domain", login.getDomain().isEmpty() ? "/" : login.getDomain());
        DisplayInfoPanel email = new DisplayInfoPanel("Email", login.getEmail().isEmpty() ? "/" : login.getEmail());
        DisplayInfoPanel username = new DisplayInfoPanel("Username", login.getUsername().isEmpty() ? "/" : login.getUsername());
        DisplayInfoPanel password = new DisplayInfoPanel("Password", Encryption.decryptPassword(login.getPassword(), mainPassword));
        add(domain);
        add(email);
        add(username);
        add(password);

        JButton editButton = new JButton("Edit info");
        editButton.addActionListener(e -> editLogin(domain, email, username, password, editButton, login, filePath, mainPassword));
        JButton backButton = new JButton("Go Back");
        backButton.addActionListener(e -> {
            frame.requestFocus();
            dispose();
        });
        add(editButton);
        add(backButton);

        setVisible(true);
    }

    private void editLogin(DisplayInfoPanel domain, DisplayInfoPanel email, DisplayInfoPanel username, DisplayInfoPanel password, JButton editButton, LoginInfo login, String filePath, char[] mainPassword) {
        JTextField domainField = (JTextField) domain.getComponent(1);
        JTextField emailField = (JTextField) email.getComponent(1);
        JTextField usernameField = (JTextField) username.getComponent(1);
        JPasswordField passwordField = (JPasswordField) password.getComponent(1);
        boolean edit = editButton.getText().equals("Edit info");
        if (!edit) changeLogin(login, emailField.getText(), usernameField.getText(), passwordField.getPassword(), domainField.getText(), filePath, mainPassword);

        editButton.setText(edit ? "Save" : "Edit info");
        domainField.setEditable(edit);
        emailField.setEditable(edit);
        usernameField.setEditable(edit);
        passwordField.setEditable(edit);
    }

    private void changeLogin(LoginInfo login, String email, String username, char[] password, String domain, String filePath, char[] mainPassword) {
        String message = "Are you sure you want to change ";
        List<String> editedAttribute = new ArrayList<String>();
        if (!domain.equals(login.getDomain())) editedAttribute.add("domain");
        if (!email.equals(login.getEmail())) editedAttribute.add("email");
        if (!username.equals(login.getUsername())) editedAttribute.add("username");
        try {
            if (!Arrays.equals(password, Encryption.decryptPassword(login.getPassword(), mainPassword))) editedAttribute.add("password");
        } catch (Exception e1) {
            System.out.println("SOMETHING IS WRONG");
        }
        if (editedAttribute.size() == 1) message += editedAttribute.iterator().next() + ".";
        else {
            for (String attribute: editedAttribute) {
                if (attribute == editedAttribute.get(editedAttribute.size() - 1)) {
                    message = message.substring(0, message.length() - 2);
                    message += String.format(" and %s.", attribute);
                    break;
                }
                message += attribute + ", ";
            }
        }
        
        if (editedAttribute.size() <= 0) {
            JOptionPane.showMessageDialog(
                ShowInfoDialog.this, 
                "Nothing changed", 
                "No changes made", 
                JOptionPane.CLOSED_OPTION);
            return;
        }
        int option = JOptionPane.showConfirmDialog(
            ShowInfoDialog.this, 
            message, 
            "Change password", 
            JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                login.editLogin(email, username, password, domain, filePath, mainPassword);
            } catch (Exception e1) {
                System.out.println("SOMETHING IS WRONG");
            }
            frame.resetCardPanel();
            iconLabel.setIcon(new ImageIcon(login.getIconPath()));
        } else {
            // TODO change info panels back
        }
    }
}
