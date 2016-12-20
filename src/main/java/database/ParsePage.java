package database;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.net.URLDecoder;
import java.sql.*;

/**
 * Created by scott on 2016/12/20.
 */
public class ParsePage {
    public static void parseFromString(String content, Connection conn) throws Exception{
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
                    String mainurl = "http://johnhany.net/";
                    String wpurl = mainurl + "wp-content/";

                    //only save page from "http://johnhany.net"
                    if(nextlink.startsWith(mainurl)) {
                        String sql = null;
                        ResultSet rs = null;
                        PreparedStatement pstmt = null;
                        Statement stmt = null;
                        String tag = null;

                        //do not save any page from "wp-content"
                        if(nextlink.startsWith(wpurl)) {
                            continue;
                        }

                        try {
                            //check if the link already exists in the database
                            sql = "SELECT * FROM record WHERE URL = '" + nextlink + "'";
                            stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
                            rs = stmt.executeQuery(sql);

                            if(rs.next()) {

                            }else {
                                //if the link does not exist in the database, insert it
                                sql = "INSERT INTO record (URL, crawled) VALUES ('" + nextlink + "',0)";
                                pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                                pstmt.execute();
                                System.out.println(nextlink);

                                //use substring for better comparison performance
                                nextlink = nextlink.substring(mainurl.length());
                                //System.out.println(nextlink);

                                if(nextlink.startsWith("tag/")) {
                                    tag = nextlink.substring(4, nextlink.length()-1);
                                    //decode in UTF-8 for Chinese characters
                                    tag = URLDecoder.decode(tag,"UTF-8");
                                    sql = "INSERT INTO tags (tagname) VALUES ('" + tag + "')";
                                    pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                                    //if the links are different from each other, the tags must be different
                                    //so there is no need to check if the tag already exists
                                    pstmt.execute();
                                }
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
