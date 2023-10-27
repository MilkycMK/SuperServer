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
    @Column(name = "user_name")
    private String userName;
    private String token;
    private String mask;
    @Column(name = "creation_time")
    private LocalDateTime creationTime;
    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;

    protected Session() {

    }

    public Session(Token token) {
        this.userName = token.getUserName().toLowerCase();
        this.token = token.getToken();
        this.mask = token.getMask();
        this.creationTime = token.getCreationTime();
        this.expirationTime = token.getExpirationTime();
        SessionService.saveSession(this);
    }

    public int getId() {
        return this.id;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getToken() {
        return this.token;
    }

    public String getMask() {
        return this.mask;
    }

    public LocalDateTime getCreationTime() {
        return this.creationTime;
    }

    public LocalDateTime getExpirationTime() {
        return this.expirationTime;
    }

}
