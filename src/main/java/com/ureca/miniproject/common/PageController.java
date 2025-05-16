package com.ureca.miniproject.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    @GetMapping("/page/gameDetail")
    public String test(@RequestParam("roomId") Long roomId) {
        return "/game/gameDetail.html";
    }
    
    @GetMapping("/page/gameDetail/invite")
    public String inviteGame(@RequestParam("roomId") Long roomId) {    	
    	return "/user/inviteGameRoom.html";
    }
    
    @GetMapping("/login")
    public String login() {
    	return "/login.html";
    }
    
    @GetMapping("/register")
    public String register() {
    	return "/register.html";
    }
}
