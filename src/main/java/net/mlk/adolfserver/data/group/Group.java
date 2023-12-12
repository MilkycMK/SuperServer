package net.mlk.adolfserver.data.group;

import jakarta.persistence.*;
import net.mlk.jmson.annotations.JsonField;
import net.mlk.jmson.annotations.JsonIgnore;
import net.mlk.jmson.utils.JsonConvertible;

@Table(name = "groups")
@Entity
public class Group implements JsonConvertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "user_id")
    @JsonIgnore
    private int userId;
    private String name;

    protected Group() {

    }

    public Group(int userId, String name) {
        this.userId = userId;
        this.name = name;
        GroupService.save(this);
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getName() {
        return this.name;
    }
}
