package API.generators.salt;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SaltGenerator {

    public String generate() {
        Random random = new Random();
        int length = random.nextInt(20)+55;
        return RandomStringUtils.random(length,true,true);
    }
}
