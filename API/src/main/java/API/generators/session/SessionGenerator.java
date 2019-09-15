package API.generators.session;

import API.persistance.model.Session;
import org.apache.commons.codec.digest.DigestUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class SessionGenerator {

    public Session generateSession(int accountId, String username, String host) {

        Date now = new Date();
        LocalDateTime timeForDB = LocalDateTime.now();
        SimpleDateFormat frontDate = new SimpleDateFormat("EyyMMddHHmmss");

        /*
        session token will be created with the frontDate string followed by a hash of:
            - hostIP:port
            - LocalDateTime.now()
            - username
        */
        String valueToHash = host.concat(timeForDB.toString()).concat(username);
        String sessionToken = frontDate.format(now).concat(DigestUtils.sha256Hex(valueToHash));

        Session session = new Session(sessionToken, accountId, 1, timeForDB, timeForDB);
        return session;
    }
}
