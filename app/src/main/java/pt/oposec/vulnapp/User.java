package pt.oposec.vulnapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @Expose
    @SerializedName("username")
    public String username="DefaultUser";
    @Expose
    @SerializedName("password")
    public String password="";
    @Expose
    @SerializedName("role")
    public String role="User";
    @Expose
    @SerializedName("points")
    public String points="0";


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String points, String role) {
        this.username = username;
        this.points = points;
        this.role = role;
    }

    public User() {

    }
}
