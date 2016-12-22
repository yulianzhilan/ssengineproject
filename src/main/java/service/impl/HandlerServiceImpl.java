package service.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import service.HandlerService;

import javax.annotation.Resource;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by scott on 2016/12/22.
 */
@Service("handlerService")
public class HandlerServiceImpl implements HandlerService {

    @Resource(name = "scheduledExecutorService")
    private ScheduledExecutorService service;



    /**
     * 检查/创建数据库并进行第一次检索
     *
     * @param url
     * @param mainUrl
     */
    @Override
    public void databaseAndFirstSearch(String url, String mainUrl) {
        if(validateURL(url) || validateURL(mainUrl)){
            throw new RuntimeException("路径参数有误！");
        }
        service.execute(new Runnable() {
            @Override
            public void run() {

            }
        });

    }

    /**
     * 守护线程，负责查询定时查询数据库中未被检索的链接数，并根据该结果动态创建子线程检索未被检索的链接
     */
    @Override
    public void daemonThread() {

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

        return !StringUtils.isEmpty(url);
    }
}
