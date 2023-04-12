package logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

public class LoginInfo {
    private String email;
    private String username;
    private String password;
    private String domain;
    private String iconPath;

    public LoginInfo(String email, String username, String password, String domain, String iconPath) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.domain = domain;
        this.iconPath = iconPath;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDomain() {
        return domain;
    }

    public String getIconPath() {
        return iconPath;
    }

    public String toString() {
        return String.format("%s,%s,%s,%s,%s", this.email, this.username, this.password, this.domain, this.iconPath);
    }

    public void saveLogin(String filePath) throws IOException {
        FileWriter fileWriter = new FileWriter(filePath, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(this.toString());
        bufferedWriter.newLine();
        
        bufferedWriter.close();
        fileWriter.close();
    }

    public void editLogin(String email, String username, String password, String domain, String iconPath, String filePath) throws IOException {

        File file = new File(filePath);
        FileReader fileReader = new FileReader(file);

        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder editedContent = new StringBuilder();
        String line;
        String editLine = this.toString();
        while ((line = bufferedReader.readLine()) != null) {
            if (line.equals(editLine)) {
                this.email = email;
                this.username = username;
                this.password = password;
                this.domain = domain;
                this.iconPath = iconPath;
                line = this.toString();
            }
            editedContent.append(line);
            editedContent.append(System.lineSeparator());
        }
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(editedContent.toString());

        bufferedReader.close();
        fileReader.close();
        fileWriter.close();
    }

    public void removeLogin(String filePath) throws IOException {

        File file = new File(filePath);
        FileReader fileReader = new FileReader(file);

        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder editedContent = new StringBuilder();
        String line;
        String editLine = this.toString();
        while ((line = bufferedReader.readLine()) != null) {
            if (line.equals(editLine)) {
                continue;
            }
            editedContent.append(line);
            editedContent.append(System.lineSeparator());
        }
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(editedContent.toString());

        bufferedReader.close();
        fileReader.close();
        fileWriter.close();
    }

    public static String getIconAndSaveIt(String domain) {
        String imgPath = String.format("Icons/%s.png", domain);
        if (new File(imgPath).exists()) {
            return imgPath;
        }
        try {
            int size = 48;
            URL url = new URL("https://www.google.com/s2/favicons?domain=" + domain + "&sz=" + size);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            BufferedImage image = ImageIO.read(inputStream);
            File outputFile = new File(imgPath);
            ImageIO.write(image, "png", outputFile);
            return String.format("Icons/%s.png", domain);
        } catch (Exception e) {
            return "assets/favicon-standard.png";
        }
    }
}
