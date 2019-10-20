/*
 *
 *  Aberrant - Authentication
 *  Free authentication service to use with MySQL.
 *
 */

package API;

import API.generators.hash.HashGenerator;
import API.generators.refresh.RefreshGenerator;
import API.generators.salt.SaltGenerator;
import API.generators.session.SessionGenerator;
import API.persistance.model.Group;
import API.persistance.model.Session;
import API.persistance.model.SessionResponse;
import API.persistance.model.User;
import API.repository.GroupRepository;
import API.repository.SessionRepository;
import API.repository.UserRepository;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    private SaltGenerator saltGenerator = new SaltGenerator();
    private HashGenerator hashGenerator = new HashGenerator();
    private SessionGenerator sessionGenerator = new SessionGenerator();
    private RefreshGenerator refreshGenerator = new RefreshGenerator();

    @RequestMapping(value = { "/users/select", "/users/select/{username}" }, method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<Iterable<User>> getAllUsers(@RequestHeader Map<String, String> headers, @PathVariable Optional<String> username) {
        String sessionToken = headers.get("sessiontoken");
        String refreshToken = headers.get("refreshtoken");
        int requestNumber = Integer.valueOf(headers.get("requestnumber"));
        SessionResponse sessionResponse = checkSession(sessionToken,refreshToken,requestNumber);
        if(sessionResponse.getAnswer()) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("refreshToken",sessionResponse.getRefreshToken());
            if(username.isPresent()) {
                return ResponseEntity.ok().headers(responseHeaders).body(userRepository.findByUsername(username.get()));
            } else {
                return ResponseEntity.ok().headers(responseHeaders).body(userRepository.findAll());
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/users/insert", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> insertUser(@RequestBody String jsonString, @RequestHeader Map<String, String> headers) {
        String sessionToken = headers.get("sessiontoken");
        String refreshToken = headers.get("refreshtoken");
        int requestNumber = Integer.valueOf(headers.get("requestnumber"));
        SessionResponse sessionResponse = checkSession(sessionToken,refreshToken,requestNumber);
        if(sessionResponse.getAnswer()) {
            JsonElement jsonElement = new JsonParser().parse(jsonString);
            JsonObject jobject = jsonElement.getAsJsonObject();
            String username = jobject.get("username").getAsString();
            String email = jobject.get("email").getAsString();
            String passwordOriginal = jobject.get("password").getAsString();
            String salt_front = saltGenerator.generate();
            String salt_back = saltGenerator.generate();
            Set<Group> groups = new HashSet<Group>();
            String password = hashGenerator.generateHash(salt_front, passwordOriginal, salt_back);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("refreshToken",sessionResponse.getRefreshToken());
            User user = new User(username, email, password, groups, salt_front, salt_back);
            List<User> userFound = userRepository.findByUsername(username);
            if (userFound.size() == 0) {
                userRepository.save(user);
                return ResponseEntity.ok().headers(responseHeaders).build();
            } else {
                return ResponseEntity.badRequest().headers(responseHeaders).build();
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/users/delete", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> deleteUser(@RequestBody String jsonString, @RequestHeader Map<String, String> headers) {
        String sessionToken = headers.get("sessiontoken");
        String refreshToken = headers.get("refreshtoken");
        int requestNumber = Integer.valueOf(headers.get("requestnumber"));
        SessionResponse sessionResponse = checkSession(sessionToken,refreshToken,requestNumber);
        if(sessionResponse.getAnswer()) {
            JsonElement jsonElement = new JsonParser().parse(jsonString);
            JsonObject jobject = jsonElement.getAsJsonObject();
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("refreshToken",sessionResponse.getRefreshToken());
            String username = jobject.get("username").getAsString();
            userRepository.deleteByUsername(username);
            return ResponseEntity.ok().headers(responseHeaders).build();
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/users/update", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> changeUser(@RequestBody String jsonString, @RequestHeader Map<String, String> headers) {
        String sessionToken = headers.get("sessiontoken");
        String refreshToken = headers.get("refreshtoken");
        int requestNumber = Integer.valueOf(headers.get("requestnumber"));
        SessionResponse sessionResponse = checkSession(sessionToken,refreshToken,requestNumber);
        if(sessionResponse.getAnswer()) {
            JsonElement jsonElement = new JsonParser().parse(jsonString);
            JsonObject jobject = jsonElement.getAsJsonObject();
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("refreshToken",sessionResponse.getRefreshToken());
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
            return ResponseEntity.ok().headers(responseHeaders).build();
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/groups/insert", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> addGroup(@RequestBody String jsonString, @RequestHeader Map<String, String> headers) {
        String sessionToken = headers.get("sessiontoken");
        String refreshToken = headers.get("refreshtoken");
        int requestNumber = Integer.valueOf(headers.get("requestnumber"));
        SessionResponse sessionResponse = checkSession(sessionToken,refreshToken,requestNumber);
        if(sessionResponse.getAnswer()) {
            JsonElement jsonElement = new JsonParser().parse(jsonString);
            JsonObject jobject = jsonElement.getAsJsonObject();
            String groupName = jobject.get("groupName").getAsString();
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("refreshToken",sessionResponse.getRefreshToken());
            Group group = new Group();
            group.setGroupName(groupName);
            groupRepository.save(group);
            return ResponseEntity.ok().headers(responseHeaders).build();
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/groups/delete", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> deleteGroup(@RequestBody String jsonString, @RequestHeader Map<String, String> headers) {
        String sessionToken = headers.get("sessiontoken");
        String refreshToken = headers.get("refreshtoken");
        int requestNumber = Integer.valueOf(headers.get("requestnumber"));
        SessionResponse sessionResponse = checkSession(sessionToken,refreshToken,requestNumber);
        if(sessionResponse.getAnswer()) {
            JsonElement jsonElement = new JsonParser().parse(jsonString);
            JsonObject jobject = jsonElement.getAsJsonObject();
            String groupName = jobject.get("groupName").getAsString();
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("refreshToken",sessionResponse.getRefreshToken());
            groupRepository.deleteByGroupName(groupName);
            return ResponseEntity.ok().headers(responseHeaders).build();
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = {"/groups/select", "/groups/select/{groupName}"}, method = RequestMethod.GET)
    public ResponseEntity getGroups(@RequestHeader Map<String,String> headers, @PathVariable Optional<String> groupName) {
        String sessionToken = headers.get("sessiontoken");
        String refreshToken = headers.get("refreshtoken");
        int requestNumber = Integer.valueOf(headers.get("requestnumber"));

        SessionResponse sessionResponse = checkSession(sessionToken,refreshToken,requestNumber);
        if(sessionResponse.getAnswer()) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("refreshToken",sessionResponse.getRefreshToken());
            if(groupName.isPresent()){
                return ResponseEntity.ok().headers(responseHeaders).body(groupRepository.findByGroupName(groupName.get()));
            } else {
                return ResponseEntity.ok().headers(responseHeaders).body(groupRepository.findAll());
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
        String refreshToken = headers.get("refreshtoken");
        int requestNumber = Integer.valueOf(headers.get("requestnumber"));

        SessionResponse sessionResponse = checkSession(sessionToken,refreshToken,requestNumber);
        if(sessionResponse.getAnswer()) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("refreshToken",sessionResponse.getRefreshToken());
            return ResponseEntity.ok().headers(responseHeaders).body(sessionRepository.findAll());
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/checkSession", method = RequestMethod.POST)
    public SessionResponse checkSession(@RequestParam(value = "sessionToken", required = true) String sessionToken,
                                        @RequestParam(value = "refreshToken", required = true) String refreshToken,
                                        @RequestParam(value = "requestNumber", required = true) int requestNumber){
        SessionResponse sessionResponse = new SessionResponse(false, null);
        //Check if session token exists.
        if(sessionRepository.existsBySessionToken(sessionToken)) {
            Session session = sessionRepository.findBySessionToken(sessionToken);
            int realRequestNumber = session.getRequestNumber();
            // Check that the request number is somewhere near where the database thinks it should be.
            if(requestNumber >= (realRequestNumber - 10) && requestNumber <= (realRequestNumber + 10)) {
                String realRefreshToken = session.getRefreshToken();
                // Check refresh token is the correct one.
                if(refreshToken.equals(realRefreshToken)){
                    LocalDateTime currentTime = LocalDateTime.now();
                    // Generate a new refresh token.
                    session.setRefreshToken(refreshGenerator.generate());
                    // Update session last used.
                    session.setSessionLastUsed(currentTime);
                    // Update request number.
                    session.setRequestNumber(session.getRequestNumber() + 1);
                    sessionRepository.save(session);
                    sessionResponse.setAnswer(true);
                    sessionResponse.setRefreshToken(session.getRefreshToken());
                    return sessionResponse;
                } else {
                    return sessionResponse;
                }
            } else {
                return sessionResponse;
            }
        } else {
            return sessionResponse;
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity logout(@RequestHeader Map<String, String> headers){
        String sessionToken = headers.get("sessiontoken");
        String refreshToken = headers.get("refreshtoken");
        int requestNumber = Integer.valueOf(headers.get("requestnumber"));
        SessionResponse sessionResponse = checkSession(sessionToken,refreshToken,requestNumber);
        if(sessionResponse.getAnswer()) {
            sessionRepository.deleteBySessionToken(sessionToken);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

}

