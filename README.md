# ssengineproject   <br/>
一、项目目的   <br/>
    根据关键词和网址检索出所有匹配的链接。   <br/>
二、项目规划   <br/>
1. 项目环境和技术   <br/>
    数据缓存:使用mysql或者BerkeleyDB或者LinkQueue队列   <br/>
        ** http://www.baike.com/wiki/BerkeleyDB   <br/>
        ** http://blog.sina.com.cn/s/blog_502c8cc40100yqkj.html   <br/>
    性能考虑:使用多线程异步处理和加载数据。   <br/>
    页面展示:使用jsp作为主要页面展示手段。(可以考虑freeMark等)。   <br/>
    版本控制:使用git+GitHub。   <br/>
    框架:springMVC+[Hibernate/MyBatis](可以考虑使用springBoot)。   <br/>
    检索技术:Apache HttpComponents 4.3(可以考虑使用JSoup)   <br/>
        ** http://johnhany.net/2013/11/web-crawler-using-java-and-mysql/   <br/>
        ** http://blog.csdn.net/lmj623565791/article/details/23272657   <br/>
    jdk版本:1.8.0_66。   <br/>
2. 项目实施计划(细则)   <br/>
    1)HandlerService
        1.1主线程创建子线程进行数据库表检查/创建以及检索mainURL下的所有href并存入数据库<br/>
        1.2守护线程定时查询数据库中未被检索的链接数，并根据该结果动态创建子线程检索未被检索的链接。(采用广度优先)<br/>
        1.3主线程实现页面的实时推送<br/>
            **http://blog.csdn.net/anluo_sun/article/details/48652215<br/>

   <br/>
3. 项目最终效果(初稿)   <br/>
    1、根据指定关键词搜索所有相关网页连接并展示，解析网站的出现频度。   <br/>
    2、根据指定网站解析网站的模块和相应内容的分布情况。   <br/>
   <br/>
三、项目总结   <br/>
1. 实时记录   <br/>
    1)Hibernate中使用Threadlocal创建线程安全的Session   <br/>
    **http://blog.sina.com.cn/s/blog_7ffb8dd5010146i3.html   <br/>
    2)前端的展示技术   <br/>
    **http://blog.csdn.net/anluo_sun/article/details/48652215 <br/>
    3)Hibernate Criteria  <br/>
    **http://blog.csdn.net/saindy5828/article/details/16893257 <br/>
    **http://docs.jboss.org/hibernate/orm/3.5/reference/zh-CN/html/querycriteria.html <br/>
    4)Hibernate  Template  <br/>
    **http://www.360doc.com/content/14/0418/23/7669533_370187570.shtml  <br/>
    5)Spring + Hibernate 多线程 <br/>
    **http://blog.csdn.net/cgjkjk/article/details/16801347  <br/>


