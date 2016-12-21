package spring;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by scott on 2016/12/21.
 */
public class DatabaseTest {
    private ApplicationContext ac;

    @Before
    public void init(){
        ac = new ClassPathXmlApplicationContext("spring/spring-database.xml");
    }

    @Test
    public void test2(){
        SessionFactory sf = ac.getBean("sessionFactory",SessionFactory.class);
        for(int i=0;i<100;i++){
            System.out.println(sf.openSession().hashCode());
        }

    }
}
