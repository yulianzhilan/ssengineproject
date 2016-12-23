package bean.thread;

import handler.UrlSearchHandler;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by scott on 2016/12/23.
 */
public class SearchThread implements Runnable{

    @Resource(name = "sessionFactory")
    private SessionFactory factory;

    @Resource(name = "urlSearchHandler")
    private UrlSearchHandler handler;

    private String mainUrl;

    public SearchThread(String mainUrl) {
        this.mainUrl = mainUrl;
    }

    @Override
    public void run() {
        factory.openSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                String sql= "SELECT * FROM crawler.record WHERE crawled = 0";
                Statement state = connection.createStatement();
                ResultSet result = state.executeQuery(sql);
                while(result.next()){
                    String path = result.getString(2);
                    handler.getByString(path,connection,mainUrl);
                }
                connection.close();
            }
        });
    }

}
