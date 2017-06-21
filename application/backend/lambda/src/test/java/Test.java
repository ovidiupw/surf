import com.fasterxml.jackson.core.JsonProcessingException;

import java.net.URI;
import java.net.URISyntaxException;

public class Test {
    @org.junit.Test
    public void test() throws JsonProcessingException, URISyntaxException {
        URI uri = new URI("https://console.aws.amazon.com/s3/buckets/surf-web-crawler/?region=eu-west-1&tab=overview");
        System.out.println("uri.getPath() = " + uri.getPath());
    }
}
