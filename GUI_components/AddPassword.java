package GUI_components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import logic.Encryption;
import logic.LoginInfo;

public class AddPassword extends JDialog {
    
    public AddPassword(Frame frame, String mainPassword) {
        setTitle("Add new login");
        setSize(300, 200);
        setLocationRelativeTo(null);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        PlaceholderField email = new PlaceholderField("Email");
        PlaceholderField username = new PlaceholderField("Username");
        PlaceholderField password = new PlaceholderField("Password");
        PlaceholderField domain = new PlaceholderField("Domain");

        JButton submitButton = new JButton("Add");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (checkNewPassword(email.getText(), username.getText(), password.getText(), domain.getText())) {
                    try {
                        LoginInfo newLoginInfo = new LoginInfo(email.getText(), username.getText(), Encryption.encryptPassword(password.getText(), mainPassword), domain.getText(), LoginInfo.getIconAndSaveIt(domain.getText()));
                        frame.addOrRemoveLoginInfo(newLoginInfo, true);
                        JOptionPane.showMessageDialog(AddPassword.this, "Successfully added a new password.", "New password added", JOptionPane.CLOSED_OPTION);
                        dispose();
                    } catch (Exception e1) {
                        System.out.println("SOMETHING IS WRONG");
                    }
                } 
            }
        });
        
        JButton backButton = new JButton("Go Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        add(email);
        add(username);
        add(password);
        add(domain);
        add(submitButton);
        add(backButton);

        setVisible(true);
    }

    public boolean checkNewPassword(String email, String username, String password, String domain) {
        if ((email.isEmpty() && username.isEmpty()) || password.isEmpty()) {
            JOptionPane.showMessageDialog(AddPassword.this, "One of email and username have to be filled in and password has to be filled in.", "Password couldn't be added", JOptionPane.CLOSED_OPTION);
            return false;
        }
        // TODO add some restrictions to password made
        return true;
    }
}
