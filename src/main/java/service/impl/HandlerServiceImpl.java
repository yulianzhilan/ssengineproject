package service.impl;

import handler.UrlSearchHandler;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import service.HandlerService;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by scott on 2016/12/22.
 */
@Service("handlerService")
public class HandlerServiceImpl implements HandlerService {

    @Resource(name = "scheduledExecutorService")
    private ScheduledExecutorService service;

    @Resource(name = "sessionFactory")
    private SessionFactory factory;

    @Resource(name = "urlSearchHandler")
    private UrlSearchHandler handler;

    @Resource(name = "hibernateTemplate")
    private HibernateTemplate template;

    @Resource(name = "daemThread")
    private Runnable task;

    @Resource(name = "searchThread")
    private Runnable search;

//    private Executor executor = new ScheduledThreadPoolExecutor(10);
    private Executor executor = Executors.newFixedThreadPool(10);
    /**
     * (检查/创建数据库并)
     * 进行第一次检索
     *
     * @param url
     * @param mainUrl
     */
    @Override
    public void databaseAndFirstSearch(final String url, String mainUrl) throws InterruptedException {
        if(validateURL(url) || validateURL(mainUrl)){
            throw new RuntimeException("路径参数有误！");
        }
        factory.openSession().doWork(new Work() {
            @Override
            public void execute(Connection conn) throws SQLException {
                String sql;
                Statement state;
                if(conn != null){
                    try{
                        sql = "CREATE DATABASE IF NOT EXISTS crawler";
                        state = conn.createStatement();
                        state.executeUpdate(sql);

                        sql = "USE crawler";
                        state = conn.createStatement();
                        state.executeUpdate(sql);

                        sql = "CREATE TABLE IF NOT EXISTS record(recordId int (5) not null auto_increment, URL text not null, crawled tinyint(1) not null, primary key (recordID)) engine=InnoDB DEFAULT CHARSET=utf8;";
                        state = conn.createStatement();
                        state.executeUpdate(sql);

                        sql = "CREATE TABLE IF NOT EXISTS tags(tagnum int(4) not null auto_increment, tagname text not null, primary key (tagnum)) engine=InnoDB DEFAULT CHARSET=utf8";
                        state=conn.createStatement();
                        state.executeUpdate(sql);
                    } catch (SQLException e){
                        e.printStackTrace();
                    }
                    sql = "UPDATE crawler.record SET crawled =1 WHERE URL = '"+url+"'";
//                    DetachedCriteria criteria = DetachedCriteria.forClass(RecordEntity.class);
                    state = conn.createStatement();
                    //如果此链接没有被检索过，则将此链接存入数据库并检索
                    if(state.executeUpdate(sql) == 0){
                        handler.getByString(url,conn,mainUrl);
                    }else {
                        // TODO 此处编写如果该链接已经被检索过的逻辑
                    }
                    conn.close();
                    System.out.println("FIRST SEARCH DONE!");
                }
            }
        });
        daemonThread(mainUrl);
//        searchFromDatabase(mainUrl);
        Thread.sleep(5000);
    }

    /**
     * 守护线程，负责查询定时查询数据库中未被检索的链接数，并根据该结果动态创建子线程检索未被检索的链接
     */
    @Override
    public void daemonThread(String mainUrl) {
        service.execute(task);
    }


    /**
     * 从数据库中查询没有被检索过的数据并检索
     * @param mainUrl
     */
    @Override
    public void searchFromDatabase(String mainUrl){
        executor.execute(search);
    }


    /**
     * 实时推送
     */
    @Override
    public Object showDetails() {
        return null;
    }

    /**
     * 路径验证
     * @param url
     * @return
     */
    private boolean validateURL(String url){
        //TODO 具体的验证逻辑写在这里

        return StringUtils.isEmpty(url);
    }

}
