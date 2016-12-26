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
        System.out.println("1");
        factory.openSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                System.out.println("2");
                String sql= "SELECT * FROM crawler.record WHERE crawled = 0";
                Statement state = connection.createStatement();
                ResultSet result = state.executeQuery(sql);
                int count = 0;
                while(result.next()&&count<20){
                    count++;
                    String path = result.getString(2);
                    handler.getByString(path,connection,mainUrl);
                    System.out.println("getByString");
                }
                System.out.println(Thread.currentThread().getName()+"search finished");
                connection.close();
            }
        });
    }

}
