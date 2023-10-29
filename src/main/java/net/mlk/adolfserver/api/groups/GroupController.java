package net.mlk.adolfserver.api.groups;

import net.mlk.adolfserver.data.group.Group;
import net.mlk.adolfserver.data.group.GroupService;
import net.mlk.adolfserver.data.user.session.Session;
import net.mlk.adolfserver.errors.ResponseError;
import net.mlk.adolfserver.utils.AdolfUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class GroupController {

    @PostMapping(path = {"/groups", "/groups/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseError> createGroup(@RequestParam String group,
                                         @RequestAttribute Session session) {
        int userId = session.getUserId();

        if (group.isBlank()) {
            return new ResponseEntity<>(new ResponseError("Group name can't be empty."), HttpStatus.BAD_REQUEST);
        } else if (group.length() > 32) {
            return new ResponseEntity<>(new ResponseError("Group name size must be < 32"), HttpStatus.BAD_REQUEST);
        } else if (GroupService.findByUserIdAndGroupNameIgnoreCase(userId, group) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Group createdGroup = new Group(userId, group);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/groups/" + createdGroup.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(path = {"/groups", "/groups/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getGroups(@RequestAttribute Session session) {
        int userId = session.getUserId();
        return new ResponseEntity<>(GroupService.findByUserId(userId).toString(), HttpStatus.OK);
    }

    @PatchMapping(path = {"/groups/gId", "/groups/gId"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(path = {"/groups/{gId}", "/groups/{gId}/"}, method = RequestMethod.POST, headers = {"X-HTTP-Method-Override=PATCH"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseError> updateGroup(@PathVariable String gId,
                                         @RequestParam(value = "group") String groupName,
                                         @RequestAttribute Session session) {
        int userId = session.getUserId();
        int groupId = AdolfUtils.tryParseInteger(gId);
        Group group;

        if ((group = GroupService.findById(groupId)) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (groupName.isBlank()) {
            return new ResponseEntity<>(new ResponseError("Group name can't be empty."), HttpStatus.BAD_REQUEST);
        } else if (group.getGroupName().equalsIgnoreCase(groupName)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else if (GroupService.findByUserIdAndGroupNameIgnoreCase(userId, groupName) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        group.setName(groupName);
        GroupService.save(group);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = {"/groups/{gId}", "/groups/{gId}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseError> deleteGroup(@PathVariable String gId,
                                         @RequestAttribute Session session) {
        int userId = session.getUserId();
        int groupId = AdolfUtils.tryParseInteger(gId);
        Group group;

        if ((group = GroupService.findById(groupId)) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        GroupService.delete(group);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
