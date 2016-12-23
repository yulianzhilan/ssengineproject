package bean.thread;

import entity.RecordEntity;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.annotation.Resource;
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

    @Override
    public void run() {
        Session session = factory.openSession();
        String sql = "SELECT COUNT(1) FROM record WHERE crawled = 0";
        List<RecordEntity> list = session.createSQLQuery(sql).list();
        if(list != null){

        }


    }
}
