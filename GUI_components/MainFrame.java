package GUI_components;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import logic.LoginInfo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private static final int WINDOW_WIDTH = 450;
    private static final int WINDOW_HEIGHT = 300;
    private static final int SCROLLING_SPEED = 10;
    private static final String LOCK_ICON_PATH = "assets/lock-icon.png";
    private static List<LoginInfo> loginList;
    private char[] mainPassword;
    private static String filePath;
    private String searchBarContent = "";
    private static JPanel cardPanel;

    public MainFrame(String filePath, char[] mainPassword) throws IOException{
        this.mainPassword = mainPassword;
        MainFrame.filePath = filePath;
        setTitle("Password manager");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitOrLogoutApplication(false);
            }
        });
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setIconImage(new ImageIcon(LOCK_ICON_PATH).getImage());
        
        JMenuBar menuBar = new JMenuBar();

        JMenu loginMenu = new JMenu("Login");
        JMenuItem newLoginMenuItem = new JMenuItem("New login");
        JMenuItem newPassMenuItem = new JMenuItem("Generate strong password");

        // TODO unsecure passwords (same passwords)

        JMenuItem changeMainPassword = new JMenuItem("Change main password");
        JMenuItem logoutMenuItem = new JMenuItem("Logout");
        JMenuItem exitMenuItem = new JMenuItem("Exit");

        newLoginMenuItem.addActionListener(e -> new AddLoginDialog(MainFrame.this, mainPassword));
        newPassMenuItem.addActionListener(e -> new StrongPasswordGenDialog(this));
        changeMainPassword.addActionListener(e -> new ChangeMainPasswordDialog(filePath, this));
        logoutMenuItem.addActionListener(e -> exitOrLogoutApplication(true));
        exitMenuItem.addActionListener(e -> exitOrLogoutApplication(false));
        loginMenu.add(newLoginMenuItem);
        loginMenu.add(newPassMenuItem);
        loginMenu.addSeparator();
        loginMenu.add(changeMainPassword);
        loginMenu.add(logoutMenuItem);
        loginMenu.add(exitMenuItem);
        menuBar.add(loginMenu);
        setJMenuBar(menuBar);

        loginList = loadLoginInfo();

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        SearchBarField searchBar = new SearchBarField(this);
        JButton addButton = new JButton("ADD NEW LOGIN");
        addButton.addActionListener(e -> new AddLoginDialog(MainFrame.this, mainPassword));
        panel.add(searchBar, BorderLayout.WEST);
        panel.add(addButton, BorderLayout.EAST);
        add(panel, BorderLayout.NORTH);

        cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        addAllCardLogins();
        JScrollPane scrollPane = new JScrollPane(cardPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLLING_SPEED);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void exitOrLogoutApplication(boolean makeLogin) {
        String message;
        if (makeLogin) message = "Are you sure you want to logout?";
        else message = "Are you sure you want to exit?";
        int option = JOptionPane.showConfirmDialog(
                MainFrame.this, 
                message, 
                "Exit", 
                JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                Path folderToDelete = Paths.get("Icons");
                Files.walk(folderToDelete)
                        .map(Path::toFile)
                        .sorted((o1, o2) -> -o1.compareTo(o2))
                        .forEach(File::delete);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            dispose();
            if (makeLogin) new LoginFrame(this);
            else System.exit(0);
        }
    }

    private static boolean loginContains(String searchBar, LoginInfo login) {
        searchBar = searchBar.toLowerCase();
        if (login.getDomain().toLowerCase().contains(searchBar) || login.getEmail().toLowerCase().contains(searchBar) || login.getUsername().toLowerCase().contains(searchBar)) 
            return true;
        return false;
    }

    private static List<LoginInfo> loadLoginInfo() throws IOException {
        List<LoginInfo> loginInfo = new ArrayList<LoginInfo>();
        File file = new File(filePath);
        FileReader fileReader = new FileReader(file);

        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        boolean isFirstElement = true;
        while ((line = bufferedReader.readLine()) != null) {
            if (isFirstElement) {
                isFirstElement = false;
                continue;
            }
            String[] args = line.split(",");
            loginInfo.add(new LoginInfo(args[0], args[1], args[2], args[3], args[4]));
        }

        bufferedReader.close();
        return loginInfo;
    }

    private void addAllCardLogins() {
        for (LoginInfo login : loginList) {
            if (searchBarContent.equals("") || loginContains(searchBarContent, login)) {
                CardButton card = new CardButton(login, this, mainPassword, filePath);
                JPanel cardWrapperPanel = new JPanel();
                cardWrapperPanel.setLayout(new BoxLayout(cardWrapperPanel, BoxLayout.Y_AXIS));
                cardWrapperPanel.add(card);
                Dimension preferredSize = card.getPreferredSize();
                cardWrapperPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) preferredSize.getHeight()));
                cardPanel.add(cardWrapperPanel);
            }
        }
    }

    public void addOrRemoveLoginInfo(LoginInfo loginInfo, boolean add) throws IOException {
        if (add) {
            loginList.add(loginInfo);
            loginInfo.saveLogin(filePath);
        } else {
            loginList.remove(loginInfo);
            loginInfo.removeLogin(filePath);
        }
        resetCardPanel();
    }

    public void resetCardPanel() {
        cardPanel.removeAll();
        addAllCardLogins();
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    public void setSearchBar(String newValue) {
        this.searchBarContent = newValue;
        resetCardPanel();
    }

    public void setNewMainPassword(char[] newPassword) {
        this.mainPassword = newPassword;
    }
}