package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import service.HandlerService;

import javax.annotation.Resource;

/**
 * Created by scott on 2016/12/19.
 */
@Controller
@RequestMapping("/main")
public class MainController {
//    @Resource(name = "crawlerService")
//    private CrawlerServiceImpl crawlerService;
    @Resource(name = "handlerService")
    private HandlerService handlerService;

    @RequestMapping("/init")
    public ModelAndView init(){
        return init("baidu");
    }

    @RequestMapping("/index")
    public ModelAndView init(@ModelAttribute("name") String website){
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("website",website);
        return mav;
    }

    @RequestMapping("/cal")
    public void calculate(){
        String url = "http://johnhany.net/";
        try {
//            crawlerService.gate(url,url);
            handlerService.databaseAndFirstSearch(url,url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
