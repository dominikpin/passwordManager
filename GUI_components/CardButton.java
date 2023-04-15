package GUI_components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

import logic.LoginInfo;

public class CardButton extends JButton {

    public CardButton(LoginInfo login, MainFrame frame, char[] mainPassword, String filePath) {
        setBackground(Color.white);
        setLayout(new BorderLayout());

        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    new ShowInfoDialog(login, mainPassword, filePath, frame);
                } catch (Exception e1) {
                    System.out.println("SOMETHING IS WRONG");
                }
            }
        });

        File imageFile = new File(login.getIconPath());
        if (!imageFile.exists()) {
            LoginInfo.getIconAndSaveIt(login.getDomain());
        }
        ImageIcon icon = new ImageIcon(login.getIconPath());
        JLabel iconLabel = new JLabel(icon);
        JLabel textLabel = new JLabel(login.getEmail().isEmpty() ? login.getUsername() : login.getEmail());

        add(iconLabel, BorderLayout.WEST);
        Border emptyBorder = BorderFactory.createEmptyBorder(0, 0, 0, 5);
        iconLabel.setBorder(emptyBorder);
        add(textLabel, BorderLayout.CENTER);


        JButton deleteButton = new JButton("X");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
        });
        add(deleteButton, BorderLayout.EAST);
    }
}