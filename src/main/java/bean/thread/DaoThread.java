package bean.thread;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * threadLocal把线程与hibernate session绑定
 * Created by scott on 2016/12/26.
 */
public class DaoThread implements Callable {
    private SessionFactory sessionFactory;
    private CountDownLatch endSingle;
    private String sql;
    //创建线程局部变量，用来保存hibernate session
    private static  final ThreadLocal threadLocal = new ThreadLocal();

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    private DaoThread() {
    }

    public DaoThread(SessionFactory sessionFactory, String sql, CountDownLatch endSingle) {
        this.sessionFactory = sessionFactory;
        this.endSingle = endSingle;
        this.sql = sql;
    }

    @Override
    public List<?> call() throws Exception {
        //threadLocal 得到每个子线程的变量副本
        Session session = (Session)threadLocal.get();

        //如果是该线程初次访问数据库（session = null），从sessionfactory得到一个session
        //sessionfactory是线程安全的
        if(session == null){
            session = this.sessionFactory.openSession();
            //把hibernate的session和当前的线程绑定
            threadLocal.set(session);
        }
        Query query = session.createSQLQuery(this.sql.toString());

        List<String[]> list = (List<String[]>) query.list();
        session.close();threadLocal.set(null);
        if(list == null || list.size()==0){
            System.out.println("No validate entry");

            //线程计数器减一
            endSingle.countDown();
            return null;
        }
        endSingle.countDown();
        return list;
    }
}
