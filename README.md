# ssengineproject
一、项目目的
    根据关键词和网址检索出所有匹配的链接。
二、项目规划
1. 项目环境和技术
    数据缓存:使用mysql或者BerkeleyDB或者LinkQueue队列
        ** http://www.baike.com/wiki/BerkeleyDB
        ** http://blog.sina.com.cn/s/blog_502c8cc40100yqkj.html
    性能考虑:使用多线程异步处理和加载数据。
    页面展示:使用jsp作为主要页面展示手段。(可以考虑freeMark等)。
    版本控制:使用git+GitHub。
    框架:springMVC+[Hibernate/MyBatis](可以考虑使用springBoot)。
    检索技术:Apache HttpComponents 4.3(可以考虑使用JSoup)
        ** http://johnhany.net/2013/11/web-crawler-using-java-and-mysql/
        ** http://blog.csdn.net/lmj623565791/article/details/23272657
    jdk版本:1.8.0_66。
2. 项目实施计划(细则)

3. 项目最终效果(初稿)
    1、根据指定关键词搜索所有相关网页连接并展示，解析网站的出现频度。
    2、根据指定网站解析网站的模块和相应内容的分布情况。

三、项目总结
1. 实时记录
    1)Hibernate中使用Threadlocal创建线程安全的Session
    **http://blog.sina.com.cn/s/blog_7ffb8dd5010146i3.html


