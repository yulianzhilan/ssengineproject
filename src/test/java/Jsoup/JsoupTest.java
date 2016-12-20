package jsoup;

import bean.spider.LinkData;
import bean.spider.Rule;
import org.junit.Test;
import service.impl.SpiderServiceImpl;

import java.util.List;

/**
 * Created by scott on 2016/12/20.
 */
public class JsoupTest {
    @Test
    public void getDataByClass(){
        Rule rule = new Rule("baidu",new String[]{"地址","日期"},new String[]{"上海","12月25"},"",Rule.CLASS,Rule.GET);
        List<LinkData> results = SpiderServiceImpl.search(rule);
        printf(results);
    }

    @Test
    public void getDataById(){
        Rule rule = new Rule("https://www.baidu.com/s",new String[]{"地址","日期"},new String[]{"上海","12月25"},"",Rule.ID,Rule.GET);
        List<LinkData> results = SpiderServiceImpl.search(rule);
        printf(results);
    }

    @Test
    public void getDataBySelection(){
        Rule rule = new Rule("http://www.hao123.com/",new String[]{"word"},new String[]{"上海"},"div",Rule.SELECTTION,Rule.POST);
        List<LinkData> results = SpiderServiceImpl.search(rule);
        printf(results);
    }

    @Test
    public void getLinks(){
        Rule rule = new Rule("http://news.ycombinator.com/",null,null,null,Rule.GET,Rule.CLASS);
        SpiderServiceImpl.listLinks(rule);

    }


    private void printf (List<LinkData> datas){
        for(LinkData data : datas){
            System.out.println(data.getLinkText());
            System.out.println(data.getContent());
            System.out.println(data.getLinkHref());
            System.out.println("*****************************************");
        }
    }
}
