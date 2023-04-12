package GUI_components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import logic.LoginInfo;

public class Card extends JButton {
    public Card(LoginInfo login, Frame frame, String mainPassword) {
        setBackground(Color.white);
        setLayout(new BorderLayout());

        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    ShowInfoDialog showInfo = new ShowInfoDialog(login, mainPassword);
                } catch (Exception e1) {
                    System.out.println("SOMETHING IS WRONG");
                }
            }
        });

        File imageFile = new File(login.getIconPath());
        if (!imageFile.exists()) {
            LoginInfo.getIconAndSaveIt(login.getDomain());
        }
        
        ImageIcon Icon = new ImageIcon(login.getIconPath());

        JLabel iconLabel = new JLabel(Icon);
        JLabel textLabel = new JLabel(login.getEmail().isEmpty() ? login.getUsername() : login.getEmail());

        add(iconLabel, BorderLayout.WEST);
        // TODO add spacer
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