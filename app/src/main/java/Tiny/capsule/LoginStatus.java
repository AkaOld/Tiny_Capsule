package Tiny.capsule;

import io.realm.RealmObject;

public class LoginStatus extends RealmObject {
    private String username;
    public LoginStatus(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
