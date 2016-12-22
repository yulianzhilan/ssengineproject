package service.impl;

import database.ParsePage;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.*;

/**
 * Created by scott on 2016/12/20.
 */
@Service("crawlerService")
public class CrawlerServiceImpl {
    public final static void getByString(String url, Connection conn,String mainUrl) throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpGet httpGet = new HttpGet(url);
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                @Override
                public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if(status >=200 && status < 300){
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity):null;
                    } else{
                        throw new ClientProtocolException("'unexpected response status:" + status);
                    }
                }
            };
            String responseBody = httpClient.execute(httpGet,responseHandler);
            if(responseBody.split("<!DOCTYPE").length>1){
                responseBody = "<!DOCTYPE"+responseBody.split("<!DOCTYPE")[1];
            }
            ParsePage.parseFromString(responseBody,conn,mainUrl);
        } finally {
            httpClient.close();
        }
    }

    public static void gate(String url,String mainUrl) throws Exception{
        if(validateURL(url)){
            Connection conn = null;

            try {
                Class.forName("com.mysql.jdbc.Driver");
                String dbUrl = "jdbc:mysql://localhost:3306?useUnicode=true&characterEncoding=utf8";
                conn = DriverManager.getConnection(dbUrl,"root","root");
                System.out.println("connection build...");
            } catch (SQLException e){
                e.printStackTrace();
            } catch (ClassNotFoundException e){
                e.printStackTrace();
            }
            String sql;
            Statement state;
            ResultSet result;
            int count = 0;
            if(conn != null){
                try{
                    sql = "CREATE DATABASE IF NOT EXISTS crawler";
                    state = conn.createStatement();
                    state.executeUpdate(sql);

                    sql = "USE crawler";
                    state = conn.createStatement();
                    state.executeUpdate(sql);

                    sql = "CREATE TABLE IF NOT EXISTS record(recordId int (5) not null auto_increment, URL text not null, crawled tinyint(1) not null, primary key (recordID)) engine=InnoDB DEFAULT CHARSET=utf8;";
                    state = conn.createStatement();
                    state.executeUpdate(sql);

                    sql = "CREATE TABLE IF NOT EXISTS tags(tagnum int(4) not null auto_increment, tagname text not null, primary key (tagnum)) engine=InnoDB DEFAULT CHARSET=utf8";
                    state=conn.createStatement();
                    state.executeUpdate(sql);
                } catch (SQLException e){
                    e.printStackTrace();
                }

                while(true){
                    getByString(url,conn,mainUrl);
                    System.out.println("**************************");
                    System.out.println(count);
                    Thread.sleep(10000);
                    count++;

                    sql = "UPDATE record SET crawled =1 WHERE URL = '"+url+"'";
                    state = conn.createStatement();

                    if(state.executeUpdate(sql) > 0){
                        sql = "SELECT * FROM record WHERE crawled = 0";
                        state = conn.createStatement();
                        result = state.executeQuery(sql);

                        if(result.next()){
                            url = result.getString(2);
                        } else{
                            break;
                        }

                        if(count > 10 || url == null){
                            break;
                        }
                    }


                }
                conn.close();
                conn = null;

                System.out.println("DONE!");
                System.out.println(count);
            }
        }
    }


    private static boolean validateURL(String url){
//        if(!StringUtils.isEmpty(url)){
//            return true;
//        }
//        return false;
        return !StringUtils.isEmpty(url);
    }
}
