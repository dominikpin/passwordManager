package GUI_components;

import java.awt.Component;
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
    private static final int WINDOW_WIDTH = 300;
    private static final int WINDOW_HEIGHT = 150;
    private static final String LOCK_ICON_PATH = "assets/lock-icon.png";

    public LoginFrame(JFrame frame) {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(frame);
        setIconImage(new ImageIcon(LOCK_ICON_PATH).getImage());

        PlaceholderField filePathOrUsername = new PlaceholderField("File path / Username");
        PasswordPlaceholderField password = new PasswordPlaceholderField("Password");
        filePathOrUsername.addKeyListener(createKeyListener(password));
        password.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) handleLogin(filePathOrUsername, password);
            }
        });
        add(filePathOrUsername);
        add(password);

        JButton login = new JButton("Login");
        login.addActionListener(e -> handleLogin(filePathOrUsername, password));
        JButton register = new JButton("Register");
        register.addActionListener(e -> new RegisterDialog(LoginFrame.this));
        add(login);
        add(register);

        setVisible(true);
    }

    private void handleLogin(PlaceholderField filePathOrUsername, PasswordPlaceholderField password) {
        try {
            if (checkLogin(filePathOrUsername.getText(), password.getPassword())) {
                String folderName = "Icons";
                File folder = new File(folderName);
                if (!folder.exists()) {
                    folder.mkdir();
                }
                MainFrame mainFrame = new MainFrame(filePathOrUsername.getText() + ".txt", password.getPassword());
                mainFrame.setLocationRelativeTo(LoginFrame.this);
                dispose();
            }
        } catch (NoSuchAlgorithmException | IOException e1) {
            System.out.println("SOMETHING IS WRONG");
        }
    }

    private boolean checkLogin(String filePath, char[] password) throws NoSuchAlgorithmException, IOException {
        File file = new File("./" + filePath + ".txt");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(
                this, 
                "Wrong username/filePath", 
                "Wrong username", 
                JOptionPane.CLOSED_OPTION);
            return false;
        }
        if (!checkPassword(filePath + ".txt", Encryption.mainPasswordEncryption(password))) {
            JOptionPane.showMessageDialog(
                this, 
                "Wrong password", 
                "Wrong password", 
                JOptionPane.CLOSED_OPTION);
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

    public static boolean checkPassword(String filePath, String password) throws IOException {
        File file = new File(filePath);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        line = bufferedReader.readLine();
        bufferedReader.close(); 
        return line.equals(password);
    }
}