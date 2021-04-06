package controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import spring.AuthInfo;
import spring.AuthService;
import spring.WrongIdPasswordException;

@Controller
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
    private AuthService authService;

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

//    @GetMapping
//    public String form(LoginCommand loginCommand,
//    		@CookieValue(value = "REMEMBER", required = false) Cookie rCookie) {
//		if (rCookie != null) {
//			loginCommand.setEmail(rCookie.getValue());
//			loginCommand.setRememberEmail(true);
//		}
//    	return "login/loginForm";
//    }
    
    @GetMapping
    public String form(LoginCommand loginCommand) {
    	return "login/loginForm";
    }
    
    @PostMapping
    public String submit(
    		LoginCommand loginCommand, Errors errors, HttpSession session) {
        new LoginCommandValidator().validate(loginCommand, errors);
        if (errors.hasErrors()) {
            return "login/loginForm";
        }
        try {
            AuthInfo authInfo = authService.authenticate(
                    loginCommand.getEmail(),
                    loginCommand.getPassword());
            
            session.setAttribute("authInfo", authInfo);

            return "login/loginSuccess";
        } catch (WrongIdPasswordException e) {
            errors.reject("idPasswordNotMatching");
            return "login/loginForm";
        }
    }
}