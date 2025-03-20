package org.codenova.studymate.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.codenova.studymate.model.LoginLog;
import org.codenova.studymate.model.User;
import org.codenova.studymate.repository.AvatarRepository;
import org.codenova.studymate.repository.LoginLogRepository;
import org.codenova.studymate.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private AvatarRepository avatarRepository;
    private UserRepository userRepository;
    private LoginLogRepository loginLogRepository;

    @RequestMapping("/signup")
    public String signupHandle(Model model) {
        model.addAttribute("avatars", avatarRepository.findAll());
        return "auth/signup";
    }

    @RequestMapping("/signup/verify")
    public String signupVerifyHandle(@ModelAttribute @Valid User user, BindingResult result, Model model) {
        System.out.println(result.hasErrors());
        if (result.hasErrors()) {
            //에러처리하고
            return "auth/signup/verify-failed";
        }
        if (userRepository.findById(user.getId()) != null) {
            return "auth/signup/verify-failed";
        }
        userRepository.create(user);

        return "redirect:/index";
    }

    @RequestMapping("/login")
    public String loginHandel(Model model) {
        return "auth/login";
    }

    @Transactional
    @RequestMapping("/login/verify")
    public String loginVerifyHandle(@RequestParam("id") String id,
                                    @RequestParam("password") String password,
                                    HttpSession session) {

        User found = userRepository.findById(id);

        if (found == null || !found.getPassword().equals(password)) {
            //인증실패
            return "auth/login/verify-failed";

        } else {
            //인증성공
            userRepository.updateLoginCountByUserId(id);
            loginLogRepository.create(id);
            session.setAttribute("user",found);

            return "redirect:/index";
        }
    }

    @RequestMapping("/logout")
    public String logoutHandle(HttpSession session){
        session.invalidate();
        return "redirect:/index";
    }

}
