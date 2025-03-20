package org.codenova.studymate.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.codenova.studymate.model.Avatar;
import org.codenova.studymate.model.LoginLog;
import org.codenova.studymate.model.User;
import org.codenova.studymate.repository.AvatarRepository;
import org.codenova.studymate.repository.LoginLogRepository;
import org.codenova.studymate.repository.UserRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/my")
@AllArgsConstructor
public class MyController {

    private LoginLogRepository logRepository;
    private AvatarRepository avatarRepository;

    @RequestMapping("/profile")
    public String myProfileHandle(Model model, @SessionAttribute("user") @Nullable User user) {
        //User user = (User)session.getAttribute("user");
        if (user == null){
            return "rediect:/auth/login";
        }
        model.addAttribute("user",user);
        model.addAttribute("hiddenId",user.getId().substring(0,2)+"******");
        LoginLog latestLog = logRepository.findLatestByUserId(user.getId());
        model.addAttribute("latestLog",latestLog);

        Avatar avatar = avatarRepository.findById(user.getAvatarId());
        model.addAttribute("userAvatar",avatar);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String s = dtf.format(latestLog.getLoginAt());
        model.addAttribute("loginAt",s);

        return "my/profile";
    }

}
