package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by scott on 2016/12/19.
 */
@Controller
@RequestMapping("/main")
public class MainController {

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
}
