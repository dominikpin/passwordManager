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
    
    public ShowInfoDialog(LoginInfo login, char[] mainPassword) throws Exception {
        setTitle("Info display");
        setSize(400, 300);
        setLocationRelativeTo(null);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        File imageFile = new File(login.getIconPath());
        if (!imageFile.exists()) {
            LoginInfo.getIconAndSaveIt(login.getDomain());
        }

        ImageIcon Icon = new ImageIcon(login.getIconPath());
        JLabel iconLabel = new JLabel(Icon);

        DisplayInfoPanel domain = new DisplayInfoPanel("Domain", login.getDomain().isEmpty() ? "/" : login.getDomain());
        DisplayInfoPanel email = new DisplayInfoPanel("Email", login.getEmail().isEmpty() ? "/" : login.getEmail());
        DisplayInfoPanel username = new DisplayInfoPanel("Username", login.getUsername().isEmpty() ? "/" : login.getUsername());
        DisplayInfoPanel password = new DisplayInfoPanel("Password", Encryption.decryptPassword(login.getPassword(), mainPassword));

        // TODO add functionality to edit button
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
