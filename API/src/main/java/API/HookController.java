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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class HookController {
    @Autowired
    private UserRepository userRepository;


    @GetMapping(path="/all")
    public @ResponseBody
    Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }



}

