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
    @Column(name = "group_name")
    @JsonField(key = "group_name")
    private String groupName;

    protected Group() {

    }

    public Group(int userId, String groupName) {
        this.userId = userId;
        this.groupName = groupName;
        GroupService.save(this);
    }

    public int getId() {
        return this.id;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getGroupName() {
        return this.groupName;
    }
}
