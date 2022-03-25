package eu.filip.loginredis.controller;

import eu.filip.loginredis.util.Credentials;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginController {

    @PostMapping("/login")
    public void login(@RequestBody Credentials credentials){}

    @PostMapping("/logout")
    public void logout(HttpServletRequest request){
        try{
            request.logout();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @PostMapping("/check")
    public void check(){}


}
