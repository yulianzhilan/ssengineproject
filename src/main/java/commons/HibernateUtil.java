package commons;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.annotation.Resource;

/**
 * Created by scott on 2016/12/22.
 */

public class HibernateUtil {
    private static SessionFactory factory;

    private static ThreadLocal<Session> session = new ThreadLocal();

    public static Session getLocalThreadSession(){
        Session s = session.get(); //获取当前线程中的session
        if(s == null){
            s = factory.getCurrentSession(); //创建线程
            session.set(s); //将线程放入线程池
        }
        return s;
    }

    public static void closeSession(){
        Session s = session.get();
        if(s != null){
            session.set(null);
        }
    }

}
