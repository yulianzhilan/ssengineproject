package service.impl;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;
import service.SimpleHttpClient;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by scott on 2016/12/19.
 */
@Service("simpleHttpClient")
public class SimpleHttpClientImpl implements SimpleHttpClient{

//    @Resource(name = "httpClient")
    private HttpClient client = new HttpClient();

    @Override
    public void simpleAccess(String path) {
        simpleAccess("GET", path);
    }

    @Override
    public void simpleAccess(String method, String path) {
        if("".equals(method)){
            throw new RuntimeException("unknown access method");
        }
        HttpMethod targetPath = null;
        if("GET".equalsIgnoreCase(method)){
            // GET
            targetPath = new GetMethod(path);
        } else if("POST".equalsIgnoreCase(method)){
            // POST
            targetPath = new PostMethod(path);
        }
        //此处可以增加多个访问方式

        if(targetPath != null){
            try {
                client.executeMethod(targetPath);
                byte[] bodies = targetPath.getResponseBody();
                System.out.println(new String(bodies));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void parse(String body){
        SAXReader reader = new SAXReader();
    }
}
