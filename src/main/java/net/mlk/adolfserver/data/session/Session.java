package net.mlk.adolfserver.data.session;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sessions")
public class Session {
    @Id
    private int id;
    private String name;
    private String token;
    private String mask;

    protected Session() {

    }

    public Session(String userName, String token, String mask) {
        this.setName(userName);
        this.setToken(token);
        this.setMask(mask);
        SessionService.saveSession(this);
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getMask() {
        return this.mask;
    }

    public void setName(String userName) {
        this.name = userName;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return this.name;
    }

    public String getToken() {
        return this.token;
    }
}
