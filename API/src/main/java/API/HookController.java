package API;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.commons.codec.digest.DigestUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class HookController {

    DatabaseController dbc = new DatabaseController();

    @RequestMapping(value = "/add/user/account", method = RequestMethod.POST)
    public void addUserAccount(@RequestBody String jsonString) throws Exception{
        JsonParser jsonParser = new JsonParser();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss");
	    LocalDateTime now = LocalDateTime.now();
        JsonElement jsonTree = jsonParser.parse(jsonString);
        if (jsonTree.isJsonObject()){
            JsonObject jsonObject = jsonTree.getAsJsonObject();
            JsonElement username = jsonObject.get("username");
            JsonElement password = jsonObject.get("password");
            JsonElement firstName = jsonObject.get("first_name");
            JsonElement lastName = jsonObject.get("last_name");
            JsonElement emailAddress = jsonObject.get("email_address");
            JsonElement phoneNumber = jsonObject.get("phone_number");
            JsonElement birthDate = jsonObject.get("birth_date");
            System.out.println("username: "+username+", password: "+password+", first name: "+firstName+", last name: "+lastName);
            System.out.println("email address: "+emailAddress+", phone number: "+phoneNumber+", birth_date: "+birthDate);
            String influencerId = DigestUtils.sha256Hex(username.toString()+firstName.toString()+lastName.toString()+birthDate.toString()+dtf.format(now));
            System.out.println(influencerId);
        }
    }

    @RequestMapping("/test")
    public String returnAll(){
        dbc.start();
        return "hello";
    }
}

