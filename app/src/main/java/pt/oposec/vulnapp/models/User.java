package pt.oposec.vulnapp.models;

import android.os.Bundle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @Expose
    @SerializedName("username")
    public String username="username";
    @Expose
    @SerializedName("password")
    public String password="password";
    @Expose
    @SerializedName("role")
    public String role="role";
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

    public User(Bundle bundle) {
        if (bundle == null) return;
        this.username = bundle.getString("username");
        this.points = bundle.getString("points");
        this.role = bundle.getString("role");
    }

    public User(String aux){
        this(aux.split(":"));
    }

    public User(String[] splitArray) {
        this.username = splitArray[0];
        this.password = splitArray[1];
    }

    public User() {
    }

    public boolean isAdmin(){
        return this.role.compareToIgnoreCase("admin") == 0;
    }
    public boolean isLeet(){return this.points.equalsIgnoreCase("1337");}
}
