package handler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.io.IOException;
import java.sql.*;

/**
 * Created by scott on 2016/12/23.
 */
public class UrlSearchHandler {

    public final void getByString(String url, Connection conn, String mainUrl){
        // 先将数据库里面的状态改为已检索
        String sql = "INSERT INTO crawler.record (URL, crawled) VALUES ('" + url + "',1)";
        try {
            Statement state = conn.createStatement();
            state.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            parseFromString(responseBody,conn,mainUrl);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void parseFromString(String content,Connection conn,String mainurl) throws Exception{
        Parser parser = new Parser(content);
        HasAttributeFilter filter = new HasAttributeFilter("href");
        try {
            NodeList list = parser.parse(filter);
            int count = list.size();

            //process every link on this page
            for(int i=0; i<count; i++) {
                Node node = list.elementAt(i);

                if(node instanceof LinkTag) {
                    LinkTag link = (LinkTag) node;
                    String nextlink = link.extractLink();

                    // 手动处理相对路径
                    if(!nextlink.startsWith("http")){
                        if(!nextlink.startsWith("/")){
                            continue;
                        }
                        nextlink = mainurl.concat(nextlink);
                    }

                    //此处增加过滤掉非mainUrl下的其他链接
                    if(!nextlink.startsWith(mainurl)){
                        continue;
                    }

                    if(true) {
                        final String nextLink = nextlink;
                        String sql = null;
                        ResultSet rs = null;
                        PreparedStatement pstmt = null;
                        Statement stmt = null;
                        String tag = null;

                        try {
                            sql = "SELECT * FROM crawler.record WHERE URL = '" + nextLink + "'";
                            stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
                            rs = stmt.executeQuery(sql);

                            if(rs.next()) {

                            }else {
                                //if the link does not exist in the database, insert it
                                sql = "INSERT INTO crawler.record (URL, crawled) VALUES ('" + nextLink + "',0)";
                                pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                                pstmt.execute();
                                System.out.println(nextLink);

                            }
                        } catch (SQLException e) {
                            //handle the exceptions
                            System.out.println("SQLException: " + e.getMessage());
                            System.out.println("SQLState: " + e.getSQLState());
                            System.out.println("VendorError: " + e.getErrorCode());
                        } finally {
                            //close and release the resources of PreparedStatement, ResultSet and Statement
                            if(pstmt != null) {
                                try {
                                    pstmt.close();
                                } catch (SQLException e2) {}
                            }
                            pstmt = null;

                            if(rs != null) {
                                try {
                                    rs.close();
                                } catch (SQLException e1) {}
                            }
                            rs = null;

                            if(stmt != null) {
                                try {
                                    stmt.close();
                                } catch (SQLException e3) {}
                            }
                            stmt = null;
                        }
                    }
                }
            }
        } catch (ParserException e) {
            e.printStackTrace();
        }
    }
}
