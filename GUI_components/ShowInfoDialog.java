package GUI_components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import logic.Encryption;
import logic.LoginInfo;

public class ShowInfoDialog extends JDialog {
    public ShowInfoDialog(LoginInfo login, String mainPassword) throws Exception {
        setTitle("Add new password");
        setSize(300, 200);
        setLocationRelativeTo(null);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        File imageFile = new File(login.getIconPath());
        if (!imageFile.exists()) {
            LoginInfo.getIconAndSaveIt(login.getDomain());
        }

        ImageIcon Icon = new ImageIcon(login.getIconPath());
        JLabel iconLabel = new JLabel(Icon);

        // TODO add buttun at each info to copy info to clipboard
        // TODO hide password info with dots and add button when pressed to show password

        JLabel domain = new JLabel(String.format("Domain: %s", login.getDomain().isEmpty() ? "/" : login.getDomain()));
        JLabel email = new JLabel(String.format("Email: %s", login.getEmail().isEmpty() ? "/" : login.getEmail()));
        JLabel username = new JLabel(String.format("Username: %s", login.getUsername().isEmpty() ? "/" : login.getUsername()));
        JLabel password = new JLabel(String.format("Password: %s", Encryption.decryptPassword(login.getPassword(), mainPassword)));

        JButton editButton = new JButton("Edit info");
        editButton.addActionListener((ActionListener) new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
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
}
