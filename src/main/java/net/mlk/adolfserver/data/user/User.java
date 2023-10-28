package net.mlk.adolfserver.data.user;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "user_name")
    private String name;
    private String password;

    protected User() {

    }

    public User(String name, String password) {
        this.name = name.toLowerCase();
        this.password = password;
        UserService.saveUser(this);
        UserService.getUserRepository().flush();
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    public int getId() {
        return this.id;
    }
}
