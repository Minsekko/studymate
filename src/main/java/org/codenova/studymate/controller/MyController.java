package org.codenova.studymate.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.codenova.studymate.model.LoginLog;
import org.codenova.studymate.model.User;
import org.codenova.studymate.repository.LoginLogRepository;
import org.codenova.studymate.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/my")
@AllArgsConstructor
public class MyController {

    private LoginLogRepository logRepository;

    @RequestMapping("/profile")
    public String myProfileHandle(HttpSession session, Model model) {
        User user = (User)session.getAttribute("user");
        model.addAttribute("user",user);
        LoginLog latestLog = logRepository.findLatestByUserId(user.getId());
        model.addAttribute("latestLog",latestLog);

        return "my/profile";
    }

}
