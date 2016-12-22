package service;

/**
 * Created by scott on 2016/12/22.
 */
public interface HandlerService {
    /**
     * 检查/创建数据库并进行第一次检索
     */
    void databaseAndFirstSearch(String url,String mainUrl);

    /**
     * 守护线程，负责查询定时查询数据库中未被检索的链接数，并根据该结果动态创建子线程检索未被检索的链接
     */
    void daemonThread();

    /**
     * 实时推送
     */
    Object showDetails();

}
