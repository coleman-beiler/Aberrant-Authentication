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

import API.Hash.HashGenerator;
import API.Salt.SaltGenerator;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;

import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class HookController {

    @Autowired
    private UserRepository userRepository;

    Gson gson = new Gson();
    SaltGenerator saltGenerator = new SaltGenerator();
    HashGenerator hashGenerator = new HashGenerator();

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




}

