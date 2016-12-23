package service;

/**
 * Created by scott on 2016/12/22.
 */
public interface HandlerService {
    /**
     * 检查/创建数据库并进行第一次检索
     */
    void databaseAndFirstSearch(final String url,String mainUrl) throws InterruptedException;

    /**
     * 守护线程，负责查询定时查询数据库中未被检索的链接数，并根据该结果动态创建子线程检索未被检索的链接
     */
    void daemonThread();

    /**
     * 实时推送
     */
    Object showDetails();

    /**
     * 从数据库中查询没有被检索过的数据并检索
     * @param mainUrl
     */
    void searchFromDatabase(String mainUrl);

}
