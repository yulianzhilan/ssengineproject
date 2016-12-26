package bean.thread;

import org.hibernate.SessionFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by scott on 2016/12/26.
 */
public class MutiThread {
    private Map sqlMap;
    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;
    private CountDownLatch endSingle;

    private MutiThread() {
    }

    public MutiThread(Map sqlMap) {
        super();
        this.sqlMap = sqlMap;
    }

    public void setSqlMap(Map sqlMap) {
        this.sqlMap = sqlMap;
        this.endSingle = endSingle;
    }

    public Map getSqlMap() {
        return sqlMap;
    }

    public Map createThread(){
        CountDownLatch endSingle = new CountDownLatch(this.sqlMap.size());

        Map resultMap = new HashMap<>();
        ExecutorService pool = Executors.newCachedThreadPool();

        Iterator iterator = this.sqlMap.keySet().iterator();
        Future<?> future = null;
        while(iterator.hasNext()){
            String tableName = (String)iterator.next();
            String sql = (String)this.sqlMap.get(tableName);
            if(tableName != null && sql != null){
                //new 子线程
                Callable task = new DaoThread(sessionFactory,sql,endSingle);
                //提交到线程池
                future = pool.submit(task);
                //执行结果放在resultMap
                resultMap.put(tableName,future);
            }
        }
        //线程池关闭，不接受新任务，只执行已提交的任务
        pool.shutdown();

        //等待所有线程执行完毕
        try{
            endSingle.await();
        } catch (InterruptedException e1){
            e1.printStackTrace();
        }
        return resultMap;
    }

}
