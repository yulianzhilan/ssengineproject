package crawler;

import org.junit.Test;
import service.impl.CrawlerServiceImpl;

/**
 * Created by scott on 2016/12/20.
 */
public class CrawlerTest {
    @Test
    public void test(){
        try {
            CrawlerServiceImpl.gate("http://johnhany.net/");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
