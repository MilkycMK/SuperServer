package net.mlk.adolfserver.data.group;

import net.mlk.jmson.Json;
import net.mlk.jmson.JsonList;
import net.mlk.jmson.utils.JsonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("GroupService")
public class GroupService {
    private static GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        GroupService.groupRepository = groupRepository;
    }

    public static void save(Group group) {
        groupRepository.save(group);
        groupRepository.flush();
    }

    public static Group findByUserIdAndNameIgnoreCase(int userId, String group) {
        return groupRepository.findByUserIdAndNameIgnoreCase(userId, group);
    }

    public static Group findById(int id) {
        return groupRepository.findById(id);
    }

    public static List<Json> findAllByUserId(int id) {
        List<Group> groups = groupRepository.findAllByUserId(id);
        return groups.stream()
                .map(JsonConverter::convertToJson)
                .collect(Collectors.toList());
    }

    public static void delete(Group group) {
        groupRepository.delete(group);
    }

    public static GroupRepository getGroupRepository() {
        return groupRepository;
    }
}
