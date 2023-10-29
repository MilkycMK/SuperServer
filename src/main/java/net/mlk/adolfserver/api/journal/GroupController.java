package net.mlk.adolfserver.api.journal;

import net.mlk.adolfserver.data.group.Group;
import net.mlk.adolfserver.data.group.GroupRepository;
import net.mlk.adolfserver.data.group.GroupService;
import net.mlk.adolfserver.data.user.session.Session;
import net.mlk.adolfserver.errors.ResponseError;
import net.mlk.adolfserver.utils.AdolfUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class GroupController {

    @PostMapping(path = {"/groups", "/groups/"})
    public HttpStatus createGroup(@RequestParam String group,
                                              @RequestAttribute Session session) {
        GroupRepository groupRepository = GroupService.getGroupRepository();
        int userId = session.getUserId();

        if (groupRepository.findByUserIdAndGroupNameIgnoreCase(userId, group) != null) {
            return new ResponseEntity<>(new ResponseError("Группа уже существует.").toString(), HttpStatus.CONFLICT);
        } else if (group.isBlank()) {
            return new ResponseEntity<>(new ResponseError("Название группы не может быть пустым.").toString(), HttpStatus.BAD_REQUEST);
        }

        Group createdGroup = new Group(userId, group);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(path = {"/groups/{gId}", "/groups/{gId}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteGroup(@PathVariable String gId,
                                              @RequestAttribute Session session) {
        GroupRepository groupRepository = GroupService.getGroupRepository();
        int userId = session.getUserId();
        int groupId = AdolfUtils.tryParseInteger(gId);
        Group group;

        if ((group = groupRepository.findById(groupId)) == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
