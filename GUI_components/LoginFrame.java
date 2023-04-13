package GUI_components;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import logic.Encryption;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setSize(300, 150);
        setLocationRelativeTo(null);
        ImageIcon icon = new ImageIcon("assets/lock-icon.jpg");
        setIconImage(icon.getImage());

        PlaceholderField filePathOrUsername = new PlaceholderField("File path / Username");
        PasswordPlaceholderField password = new PasswordPlaceholderField("Password");
        filePathOrUsername.addKeyListener(createKeyListener(password));
        password.addKeyListener(createKeyListener(filePathOrUsername));

        JButton login = new JButton("Login");
        login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (LoginFrame.this.checkLogin(filePathOrUsername.getText(), password.getPassword())) {
                        String folderName = "Icons";
                        File folder = new File(folderName);
                        if (!folder.exists()) {
                            folder.mkdir();
                        }
                        new MainFrame(filePathOrUsername.getText() + ".txt", password.getPassword());
                        dispose();
                    }
                } catch (NoSuchAlgorithmException | IOException e1) {
                    System.out.println("SOMETHING IS WRONG");
                }
            }
        });

        JButton register = new JButton("Register");
        register.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new RegisterDialog(LoginFrame.this);
            }
        });

        add(filePathOrUsername);
        add(password);
        add(login);
        add(register);

        setVisible(true);
    }

    public static boolean checkPassword(String filePath, String password) throws IOException {
        File file = new File(filePath);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        line = bufferedReader.readLine();
        bufferedReader.close(); 
        return line.equals(password);
    }

    public boolean checkLogin(String filePath, char[] password) throws NoSuchAlgorithmException, IOException {
        File file = new File("./" + filePath + ".txt");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "Wrong username/filePath", "Wrong username", JOptionPane.CLOSED_OPTION);
            return false;
        }
        if (!checkPassword(filePath + ".txt", Encryption.mainPasswordEncryption(password))) {
            JOptionPane.showMessageDialog(this, "Wrong password", "Wrong password", JOptionPane.CLOSED_OPTION);
            return false;
        }
        return true;
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