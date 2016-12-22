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
    SessionFactory sf;

    @Before
    public void init(){
        ac = new ClassPathXmlApplicationContext("spring/spring-context.xml");
        sf = ac.getBean("sessionFactory",SessionFactory.class);
    }

    @Test
    public void test2(){
        ScheduledExecutorService service = (ScheduledExecutorService) ac.getBean("scheduledExecutorService");
        service.execute(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<10;i++){
                    RecordEntity entity = sf.openSession().get(RecordEntity.class,i);
                    System.out.println(entity+"subThread"+Thread.currentThread().getId());
                }
            }
        });
        for(int i=0;i<10;i++){
            RecordEntity entity = sf.openSession().get(RecordEntity.class,i);
            System.out.println(entity+"mainThread"+Thread.currentThread().getId());
        }

    }

    @Test
    public void test3(){
        for(int j=0;j<1000;j++){
            ScheduledExecutorService service = (ScheduledExecutorService) ac.getBean("scheduledExecutorService");
            service.execute(new Runnable() {
                @Override
                public void run() {
                    for(int i=0;i<10;i++){
                        System.out.println(i+"\t"+Thread.currentThread().getId()+"\tsubThread\t"+sf.openSession().hashCode());
                        try {
                            Thread.sleep(10);
                        } catch (Exception e ){

                        }
                    }
                }
            });
        }
    }
}
