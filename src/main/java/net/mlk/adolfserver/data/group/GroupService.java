package net.mlk.adolfserver.data.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public static GroupRepository getGroupRepository() {
        return groupRepository;
    }
}
