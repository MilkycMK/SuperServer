package net.mlk.adolfserver.data.session;

import jakarta.persistence.*;
import net.mlk.adolfserver.security.Token;

import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String token;
    private String mask;
    @Column(name = "creation_time")
    private LocalDateTime creationTime;
    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;

    protected Session() {

    }

    public Session(Token token) {
        this.setName(token.getName().toLowerCase());
        this.setToken(token.getToken());
        this.setMask(token.getMask());
        this.setCreationTime(token.getCreationTime());
        this.setExpirationTime(token.getExpirationTime());
        SessionService.saveSession(this);
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public LocalDateTime getCreationTime() {
        return this.creationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public LocalDateTime getExpirationTime() {
        return this.expirationTime;
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
