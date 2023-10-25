package net.mlk.adolfserver.data.session;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import net.mlk.adolfserver.security.Token;

import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
public class Session {
    @Id
    private int id;
    private String name;
    private String token;
    private String mask;
    private LocalDateTime creation_time;
    private LocalDateTime expiration_time;

    protected Session() {

    }

    public Session(Token token) {
        this.setName(token.getName());
        this.setToken(token.getToken());
        this.setMask(token.getMask());
        this.setCreationTime(token.getCreationTime());
        this.setExpirationTime(token.getExpirationTime());
        SessionService.saveSession(this);
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creation_time = creationTime;
    }

    public LocalDateTime getCreationTime() {
        return this.creation_time;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expiration_time = expirationTime;
    }

    public LocalDateTime getExpirationTime() {
        return this.expiration_time;
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
