package GUI_components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import logic.LoginInfo;

public class CardButton extends JButton {

    public CardButton(LoginInfo login, MainFrame frame, char[] mainPassword, String filePath) {
        setBackground(Color.white);
        setLayout(new BorderLayout());
        addActionListener(e -> {
            try {
                new ShowInfoDialog(login, mainPassword, filePath, frame);
            } catch (Exception e1) {
                System.out.println("SOMETHING IS WRONG");
            }
        });

        File imageFile = new File(login.getIconPath());
        if (!imageFile.exists()) LoginInfo.getIconAndSaveIt(login.getDomain());
        ImageIcon icon = new ImageIcon(login.getIconPath());
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        JLabel textLabel = new JLabel(login.getEmail().isEmpty() ? login.getUsername() : login.getEmail());
        add(iconLabel, BorderLayout.WEST);
        add(textLabel, BorderLayout.CENTER);

        JButton deleteButton = new JButton("X");
        deleteButton.addActionListener(e -> handleDeleteLogin(frame, login));
        add(deleteButton, BorderLayout.EAST);
    }

    private void handleDeleteLogin(MainFrame frame, LoginInfo login) {
        int option = JOptionPane.showConfirmDialog(
            frame, 
            "Are you sure you want to delete this login?", 
            "Delete login", 
            JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                frame.addOrRemoveLoginInfo(login, false);
            } catch (IOException e1) {
                System.out.println("SOMETHING IS WRONG");
            }
        }
    }
}