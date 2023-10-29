package net.mlk.adolfserver.data.group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
    Group findByUserIdAndNameIgnoreCase(int userId, String group);
    Group findByIdAndUserId(int id, int userId);
    List<Group> findAllByUserId(int id);
}
