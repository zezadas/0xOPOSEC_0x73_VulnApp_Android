package pt.oposec.vulnapp.helpers;

import android.content.Context;

import pt.oposec.vulnapp.models.User;

public class SaveData {
    String CONF_FILE = "config.txt";
    String USER_FILE = "user.txt";
    private int MAX_ATTEMPTS = 10;
    private final String SECRET = "HAXOR";
    private FileOperations fileOperations;

    public SaveData(Context context, String appTag) {
        this.fileOperations=new FileOperations(context,appTag);
    }

    private void writeAttempts(int amount) {
        fileOperations.writeToFile(String.valueOf(amount), CONF_FILE);
    }

    public int readAttempts() {
        String filePath = CONF_FILE;
        if (fileOperations.isFileNotFound(filePath)) resetAttempts();
        return Integer.parseInt(fileOperations.readFromFile(filePath));
    }

    public void resetAttempts() {
        writeAttempts(MAX_ATTEMPTS);
    }

    public void decreaseAttemptsLeft() {
        int num = readAttempts();
        num--;
        writeAttempts(num);
    }

    public void storeDefaultCreds(){
        String username = "john"; // 123456
        String encryptedPassword ="ysk{g~";
        saveUserRaw(username, encryptedPassword);
    }

    public User readUserFromFile(){
        if (fileOperations.isFileNotFound(USER_FILE)) storeDefaultCreds();
        String aux = fileOperations.readFromFile(USER_FILE);
        String[] splitedArray = aux.split(":");
        String username = splitedArray[0];
        String password = splitedArray[1];
        return new User(username,password);
    }
    public User loadUser() {
        User user = readUserFromFile();
        String decryptedPassword = Encryption.xor(user.password, SECRET);
        return new User(user.username, decryptedPassword);
    }

    public void saveUser(String user, String password) {
        String encryptedPassword = Encryption.xor(password, SECRET);
        saveUserRaw(user,encryptedPassword);
    }

    public void saveUserRaw(String user, String password) {
        fileOperations.writeToFile(user + ":" + password, USER_FILE);
    }
}
