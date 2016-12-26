package bean.thread;

import entity.RecordEntity;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;
import org.springframework.util.StringUtils;
import service.HandlerService;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by scott on 2016/12/23.
 */
public class DaemonThread implements Runnable{

    @Resource(name = "scheduledExecutorService")
    private ScheduledExecutorService service;

    @Resource(name = "sessionFactory")
    private SessionFactory factory;

    @Resource(name = "handlerService")
    private HandlerService handlerService;

    private String mainUrl;

    public DaemonThread(String mainUrl) {
        this.mainUrl = mainUrl;
    }

    @Override
    public void run() {
        System.out.println("1");
        factory.openSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                System.out.println("2");
                String sql = "SELECT COUNT(1) FROM crawler.record WHERE crawled = 0";
                Statement state = connection.createStatement();
                while(true) {
                    System.out.println("3");
                    ResultSet result = state.executeQuery(sql);
                    System.out.println("3-1");
                    if(!result.next()){
                        System.out.println(result.getString(1));
                        System.out.println("break");
                        break;
                    }else {
//                        result.next();
//                    if(result.next()){
                        System.out.println("4");
                        int count = result.getInt(1);
                        if (count > 100) {
                            System.out.println("4-1");
                            handlerService.searchFromDatabase(mainUrl);
                            System.out.println("4-2");
                        }
                        try {
                            System.out.println("5");
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(Thread.currentThread().getName() + "daemonThread");
                }
                connection.close();
            }
        });
        System.out.println("6");
    }
}
