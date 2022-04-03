package hcmut.thesis.gpduserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class WebController {

  @GetMapping(value = "/ping")
  public String ping() {
    return "pong  =>>>>>>>>> 9";
  }

  @GetMapping(value = "/**")
  public String index() {
    return "index";
  }
}
