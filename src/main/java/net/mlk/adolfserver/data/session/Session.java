package net.mlk.adolfserver.data.session;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sessions")
public class Session {
    @Id
    private String name;
    private String token;

    protected Session() {

    }

    public Session(String userName, String token) {
        this.setUserName(userName);
        this.setToken(token);
        SessionService.saveSession(this);
    }

    public void setUserName(String userName) {
        this.name = userName;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return this.name;
    }

    public String getToken() {
        return this.token;
    }
}
