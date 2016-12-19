package service;

/**
 * Created by scott on 2016/12/19.
 */
public interface SimpleHttpClient {
    // 简单访问方式 (默认以get请求)
    void simpleAccess(String path);
    // 简单访问方式 (指定方式)
    void simpleAccess(String method, String path);
}
