/*
 *
 *  Aberrant - Authentication
 *  Free authentication service to use with MySQL.
 *
 *  Below will describe current endpoints and their behavior.
 *
 *  Current Hooks:
 *  N/A
 *
 *  Desired Hooks:
 *
 *  (/user/add) Add Account.
 *  (/user/delete) Delete Account.
 *  (/user/update) Update Account.
 *  (/user/select/${name}) Select Specific Account.
 *  (/user/select/all) Select All Accounts.
 *
 *
 *
 *
 */

package API;

import API.generators.hash.HashGenerator;
import API.generators.salt.SaltGenerator;
import API.generators.session.SessionGenerator;
import API.persistance.model.Session;
import API.repository.SessionRepository;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class HookController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    Gson gson = new Gson();
    SaltGenerator saltGenerator = new SaltGenerator();
    HashGenerator hashGenerator = new HashGenerator();
    SessionGenerator sessionGenerator = new SessionGenerator();

    @GetMapping(path="/all")
    public @ResponseBody
    Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @RequestMapping(value = "/users/insert", method = RequestMethod.POST)
    public Integer insertUser(@RequestBody String jsonString) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jobject = jsonElement.getAsJsonObject();
        String username = jobject.get("username").getAsString();
        String email = jobject.get("email").getAsString();
        String passwordOriginal = jobject.get("password").getAsString();
        String salt_front = saltGenerator.generate();
        String salt_back = saltGenerator.generate();

        String password = hashGenerator.generateHash(salt_front,passwordOriginal,salt_back);

        User user = new User(username, email, password, salt_front, salt_back);
        List<User> userFound = userRepository.findByUsername(username);

        if(userFound.size() == 0) {
            userRepository.save(user);
            return 200;
        } else {
            return 99;
        }
    }

    @RequestMapping(value = "/users/delete", method = RequestMethod.POST)
    public Integer deleteUser(@RequestBody String jsonString) throws Exception {

        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jobject = jsonElement.getAsJsonObject();
        String username = jobject.get("username").getAsString();
        userRepository.deleteByUsername(username);
        return 200;
    }

    @RequestMapping(value = "/users/update", method = RequestMethod.POST)
    public Integer changeUser(@RequestBody String jsonString) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jobject = jsonElement.getAsJsonObject();
        int id = jobject.get("user_id").getAsInt();
        String username = jobject.get("username").getAsString();
        String email = jobject.get("email").getAsString();
        String password = jobject.get("password").getAsString();

        User user = userRepository.findById(id).get(0);

        if(!username.isEmpty()) { user.setUsername(username); }
        if(!email.isEmpty()) { user.setEmail(email); }
        if(!password.isEmpty()) { user.setPassword(password); }

        userRepository.save(user);

        return 200;
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<Session> authenticateUser(@RequestBody String jsonString, @RequestHeader("host") String host) throws Exception {
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

    @RequestMapping(value = "/checkSession", method = RequestMethod.POST)
    public boolean checkSession(@RequestParam(value = "sessionToken", required = true) String sessionToken,
                                @RequestParam(value = "requestNumber", required = true) int requestNumber){
        //Check if session token exists.
        if(sessionRepository.existsBySessionToken(sessionToken)) {
            Session session = sessionRepository.findBySessionToken(sessionToken);
            int realRequestNumber = session.getRequestNumber();
            // Check that the request number is somewhere near where the database thinks it should be.
            if(requestNumber >= (realRequestNumber - 10) && requestNumber <= (realRequestNumber + 10)) {
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

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public void testFunction(@RequestBody String jsonString) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jobject = jsonElement.getAsJsonObject();
        String sessionToken = jobject.get("sessionToken").getAsString();
        int requestNumber = jobject.get("requestNumber").getAsInt();

        System.out.println("Session Token: "+sessionToken);
        System.out.println("Request Number: "+requestNumber);

        System.out.println("Are we still authenticated?: "+checkSession(sessionToken,requestNumber));

    }





}

