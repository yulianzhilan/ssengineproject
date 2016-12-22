package spring;

import entity.RecordEntity;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by scott on 2016/12/21.
 */
public class DatabaseTest {
    private ApplicationContext ac;

    @Before
    public void init(){
        ac = new ClassPathXmlApplicationContext("spring/spring-context.xml");
    }

    @Test
    public void test2(){
        SessionFactory sf = ac.getBean("sessionFactory",SessionFactory.class);
        ScheduledExecutorService service = (ScheduledExecutorService) ac.getBean("scheduledExecutorService");
        service.execute(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<10;i++){
                    RecordEntity entity = sf.openSession().get(RecordEntity.class,i);
                    System.out.println(entity+"subThread");
                }
            }
        });
        for(int i=0;i<10;i++){
            RecordEntity entity = sf.openSession().get(RecordEntity.class,i);
            System.out.println(entity+"mainThread");
        }

    }
}
