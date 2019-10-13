/*
 *
 *  Aberrant - Authentication
 *  Free authentication service to use with MySQL.
 *
 */

package API;

import API.generators.hash.HashGenerator;
import API.generators.salt.SaltGenerator;
import API.generators.session.SessionGenerator;
import API.persistance.model.Group;
import API.persistance.model.Session;
import API.persistance.model.User;
import API.repository.GroupRepository;
import API.repository.SessionRepository;
import API.repository.UserRepository;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/auth/v1")
public class HookController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private GroupRepository groupRepository;

    Gson gson = new Gson();
    private SaltGenerator saltGenerator = new SaltGenerator();
    private HashGenerator hashGenerator = new HashGenerator();
    private SessionGenerator sessionGenerator = new SessionGenerator();

    public HookController() {
    }

    @RequestMapping(value = { "/users/select", "/users/select/{username}" }, method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<Iterable<User>> getAllUsers(@RequestHeader Map<String, String> headers, @PathVariable Optional<String> username) {
        String sessionToken = headers.get("sessiontoken");
        int requestNumber = Integer.valueOf(headers.get("requestnumber"));
        if(checkSession(sessionToken,requestNumber)) {
            if(username.isPresent()) {
                return new ResponseEntity<>(userRepository.findByUsername(username.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/users/insert", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> insertUser(@RequestBody String jsonString) {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jobject = jsonElement.getAsJsonObject();
        String sessionToken = jobject.get("sessionToken").getAsString();
        int requestNumber = jobject.get("requestNumber").getAsInt();

        if(checkSession(sessionToken,requestNumber)) {
            String username = jobject.get("username").getAsString();
            String email = jobject.get("email").getAsString();
            String passwordOriginal = jobject.get("password").getAsString();
            String salt_front = saltGenerator.generate();
            String salt_back = saltGenerator.generate();
            Set<Group> groups = new HashSet<Group>();

            String password = hashGenerator.generateHash(salt_front, passwordOriginal, salt_back);

            User user = new User(username, email, password, groups, salt_front, salt_back);
            List<User> userFound = userRepository.findByUsername(username);

            if (userFound.size() == 0) {
                userRepository.save(user);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/users/delete", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> deleteUser(@RequestBody String jsonString) {

        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jobject = jsonElement.getAsJsonObject();
        String sessionToken = jobject.get("sessionToken").getAsString();
        int requestNumber = jobject.get("requestNumber").getAsInt();

        if(checkSession(sessionToken,requestNumber)) {

            String username = jobject.get("username").getAsString();
            userRepository.deleteByUsername(username);
            return new ResponseEntity<>(HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/users/update", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> changeUser(@RequestBody String jsonString) {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jobject = jsonElement.getAsJsonObject();
        String sessionToken = jobject.get("sessionToken").getAsString();
        int requestNumber = jobject.get("requestNumber").getAsInt();

        if(checkSession(sessionToken,requestNumber)) {

            int id = jobject.get("user_id").getAsInt();
            String username = jobject.get("username").getAsString();
            String email = jobject.get("email").getAsString();
            String password = jobject.get("password").getAsString();

            User user = userRepository.findById(id).get(0);

            if (!username.isEmpty()) {
                user.setUsername(username);
            }
            if (!email.isEmpty()) {
                user.setEmail(email);
            }
            if (!password.isEmpty()) {
                String salt_front = saltGenerator.generate();
                String salt_back = saltGenerator.generate();
                String newPassword = hashGenerator.generateHash(salt_front, password, salt_back);
                user.setPassword(newPassword);
            }

            userRepository.save(user);

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/groups/insert", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> addGroup(@RequestBody String jsonString) {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jobject = jsonElement.getAsJsonObject();
        String sessionToken = jobject.get("sessionToken").getAsString();
        int requestNumber = jobject.get("requestNumber").getAsInt();
        String groupName = jobject.get("groupName").getAsString();

        if(checkSession(sessionToken,requestNumber)) {
            Group group = new Group();
            group.setGroupName(groupName);
            groupRepository.save(group);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/groups/delete", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> deleteGroup(@RequestBody String jsonString) {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jobject = jsonElement.getAsJsonObject();
        String sessionToken = jobject.get("sessionToken").getAsString();
        int requestNumber = jobject.get("requestNumber").getAsInt();
        String groupName = jobject.get("groupName").getAsString();

        if(checkSession(sessionToken,requestNumber)) {
            groupRepository.deleteByGroupName(groupName);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = {"/groups/select", "/groups/select/{groupName}"}, method = RequestMethod.GET)
    public ResponseEntity getGroups(@RequestHeader Map<String,String> headers, @PathVariable Optional<String> groupName) {
        String sessionToken = headers.get("sessiontoken");
        int requestNumber = Integer.valueOf(headers.get("requestnumber"));
        if(checkSession(sessionToken,requestNumber)) {
            if(groupName.isPresent()){
                return new ResponseEntity(groupRepository.findByGroupName(groupName.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(groupRepository.findAll(), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<Session> authenticateUser(@RequestBody String jsonString, @RequestHeader("host") String host) {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jobject = jsonElement.getAsJsonObject();
        String username = jobject.get("username").getAsString();
        String password = jobject.get("password").getAsString();

        User user = userRepository.findByUsername(username).get(0);

        String realPass =  hashGenerator.generateHash(user.getSaltFront(),password,user.getSalt_back());

        if(user.getPassword().equals(realPass)) {
            Session session = sessionGenerator.generateSession(user.getid(), username, host);
            sessionRepository.save(session);
            return new ResponseEntity<>(session,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/sessions/select/all", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Session>> getAllSessions(@RequestHeader Map<String,String> headers) {
        String sessionToken = headers.get("sessiontoken");
        int requestNumber = Integer.valueOf(headers.get("requestnumber"));
        if(checkSession(sessionToken,requestNumber)) {
            return new ResponseEntity<>(sessionRepository.findAll(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/checkSession", method = RequestMethod.POST)
    public boolean checkSession(@RequestParam(value = "sessionToken", required = true) String sessionToken,
                                @RequestParam(value = "requestNumber", required = true) int requestNumber){
        //Check if session token exists.
        if(sessionRepository.existsBySessionToken(sessionToken)) {
            Session session = sessionRepository.findBySessionToken(sessionToken);
            int realRequestNumber = session.getRequestNumber();
            // Check that the request number is somewhere near where the database thinks it should be.
            if(requestNumber >= (realRequestNumber - 10) && requestNumber <= (realRequestNumber + 10)) {
                LocalDateTime currentTime = LocalDateTime.now();
                session.setSessionLastUsed(currentTime);
                session.setRequestNumber(session.getRequestNumber() + 1);
                sessionRepository.save(session);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public ResponseEntity logout (@RequestBody String jsonString){
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jobject = jsonElement.getAsJsonObject();
        String sessionToken = jobject.get("sessionToken").getAsString();
        int requestNumber = jobject.get("requestNumber").getAsInt();

        if(checkSession(sessionToken,requestNumber)) {
            sessionRepository.deleteBySessionToken(sessionToken);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

}

