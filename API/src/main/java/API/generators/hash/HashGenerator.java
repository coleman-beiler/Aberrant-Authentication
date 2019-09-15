package API.generators.hash;

import org.apache.commons.codec.digest.DigestUtils;

public class HashGenerator {

    public String generateHash(String front, String middle, String back){
        String combinedString = front.concat(middle).concat(back);
        return DigestUtils.sha256Hex(combinedString);
    }
}
