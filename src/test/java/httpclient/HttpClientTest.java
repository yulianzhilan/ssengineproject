package httpclient;

import org.junit.Test;
import service.SimpleHttpClient;
import service.impl.SimpleHttpClientImpl;

/**
 * Created by scott on 2016/12/19.
 */
public class HttpClientTest {
    @Test
    public void test(){
        SimpleHttpClient client = new SimpleHttpClientImpl();
        client.simpleAccess("http://www.baidu.com/s?wd=jack");
    }
}
