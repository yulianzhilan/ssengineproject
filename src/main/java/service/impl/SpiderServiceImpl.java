package service.impl;

import bean.spider.LinkData;
import bean.spider.Rule;
import bean.spider.RuleException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;
import service.SpiderService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2016/12/20.
 */
public class SpiderServiceImpl implements SpiderService{
    public static List<LinkData> search(Rule rule){
        // 验证规则有效性
        rule = validateRule(rule);

        List<LinkData> datas = new ArrayList<>();
        LinkData data;
        try{
            String url = rule.getUrl();
            String[] params = rule.getParams();
            String[] values = rule.getValues();
            String resultTagName = rule.getResultTagName();
            int type = rule.getType();
            int requestType = rule.getRequestMethod();

            //设置查询参数
            Connection conn = Jsoup.connect(url);
            if(params != null){
                for(int i=0;i<params.length;i++){
                    conn.data(params[i],values[i]);
                }
            }

            //设置请求方式
            Document doc = null;
            switch (requestType){
                case Rule.GET :
                    doc = conn.timeout(100000).get();
                    break;
                case Rule.POST:
                    doc = conn.timeout(100000).post();
                    break;
            }

            //处理返回数据
            Elements results = new Elements();
            switch (type){
                case Rule.CLASS:
                    results = doc.getElementsByClass(resultTagName);
                    break;
                case Rule.ID:
                    Element result = doc.getElementById(resultTagName);
                    results.add(result);
                    break;
                case Rule.SELECTTION:
                    results = doc.select(resultTagName);
                    break;
                default:
                    //当resultTagName为空时，默认获取body标签
                    if(StringUtils.isEmpty(resultTagName)){
                        results = doc.getElementsByTag("body");
                    }
            }

            for(Element result : results){
                Elements links = result.getElementsByTag("a");

                for(Element link : links){
                    //必要的筛选
                    String linkHref = link.attr("href");
                    String linkText = link.text();
                    String detail = link.data();

                    data = new LinkData();
                    data.setLinkHref(linkHref);
                    data.setLinkText(linkText);
                    data.setContent(detail);

                    datas.add(data);
                }
            }


        }catch (IOException e){
            e.printStackTrace();
        }

        return datas;
    }

    public static void listLinks(Rule rule){
        rule = validateRule(rule);
        try {
            Document doc = Jsoup.connect(rule.getUrl()).get();
            Elements links = doc.select("a[href]");
            Elements media = doc.select("[src]");
            Elements imports = doc.select("link[href]");

            System.out.println("media:" + media.size());
            for(Element src : media){
                if("img".equals(src.tagName())){
                    printf(src.tagName(),src.attr("abs:src"),src.attr("height"),trim(src.attr("alt"),20));
                }else {
                    printf(src.tagName(),src.attr("abs:src"));
                }
            }

            System.out.println("imports:"+imports.size());
            for(Element _import : imports){
                printf(_import.tagName(),_import.attr("abs:href"),_import.attr("rel"));
            }

            System.out.println("links:"+links.size());
            for(Element link : links){
                printf(link.attr("abs:href"),trim(link.text(),35));
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 对传入的参数进行验证
     * @param rule
     */
    private static Rule validateRule(Rule rule){
        String url = rule.getUrl();
        if(StringUtils.isEmpty(url)){
            throw new RuleException("url不能为空!");
        }
//        if(!url.startsWith("http://www") && !url.startsWith("https://www")){
//            rule.setUrl("http://www.baidu.com/s");
//        }
        if(rule.getParams()!=null && rule.getValues()!=null){
            if(rule.getParams().length != rule.getValues().length){
                throw new RuleException("参数的键值不匹配!");
            }
        }
        return rule;
    }

    private static String trim (String str,int width){
        if(str.length()>width){
            return str.substring(0,width-1)+".";
        }else{
            return str;
        }

    }

    private static void printf(String... msgs){
        for(String str: msgs){
            System.out.println("**************************");
            System.out.print("#"+str);
        }
    }
}
