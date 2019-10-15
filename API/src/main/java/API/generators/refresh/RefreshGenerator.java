package API.generators.refresh;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class RefreshGenerator {
    public String generate() {
        Random random = new Random();
        int length = random.nextInt(50)+25;
        return RandomStringUtils.random(length,true,true);
    }
}
