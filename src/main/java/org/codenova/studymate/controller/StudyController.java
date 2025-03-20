package org.codenova.studymate.controller;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.codenova.studymate.model.entity.StudyGroup;
import org.codenova.studymate.model.entity.StudyMember;
import org.codenova.studymate.model.entity.User;
import org.codenova.studymate.model.vo.StudyGroupWithCreator;
import org.codenova.studymate.repository.StudyGroupRepository;
import org.codenova.studymate.repository.StudyMemberRepository;
import org.codenova.studymate.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/study")
@AllArgsConstructor
public class StudyController {
    private StudyGroupRepository studyGroupRepository;
    private StudyMemberRepository studyMemberRepository;
    private UserRepository userRepository;

    @RequestMapping("/create")
    public String createHandle() {
        return "study/create";
    }

    @Transactional
    @RequestMapping("/create/verify")
    public String createVerifyHandle(@ModelAttribute StudyGroup studyGroup,
                                     @SessionAttribute("user")User user) {
        String randomId = UUID.randomUUID().toString().substring(24);
        studyGroup.setId(randomId);
        studyGroup.setCreatorId(user.getId());
        studyGroupRepository.create(studyGroup);
        StudyMember studyMember = new StudyMember();
        studyMember.setUserId(user.getId());
        studyMember.setGroupId(studyGroup.getId());
        studyMember.setRole("리더");
        studyMemberRepository.createApproved(studyMember);
        studyGroupRepository.addMemberCountById(studyGroup.getId());
        return "redirect:/";
    }
    @RequestMapping("/search")
    public String searchHandle(@RequestParam("word") Optional<String> word, Model model){
        if(word.isEmpty()) {
            return "redirect:/";
        }
        String wordValue = word.get();
        List<StudyGroup> result = studyGroupRepository.findByNameLikeOrGoalLike("%"+wordValue+"%");
        List<StudyGroupWithCreator> convertedResult = new ArrayList<>();
        for (StudyGroup one : result) {
            User found = userRepository.findById(one.getCreatorId());
            StudyGroupWithCreator c = StudyGroupWithCreator.builder().group(one).creator(found).build();
            convertedResult.add(c);
        }
        model.addAttribute("count",convertedResult.size());
        model.addAttribute("result",convertedResult);
        return "study/search";
    }

    @RequestMapping("/{id}")
    public String detailHandle(@PathVariable("id")String id, Model model) {
        System.out.println(id);

        StudyGroup group = studyGroupRepository.findById(id);
        if(group == null) {
            return "redirect:/";
        }
        model.addAttribute("group",group);

        return "study/view";
    }

    @Transactional
    @RequestMapping("/{id}/join")
    public String joinHandle(@PathVariable("id")String id,
                             @SessionAttribute("user") User user) {

        StudyMember member = StudyMember.builder().
                                        userId(user.getId()).groupId(id).role("맴버").build();

//        member.setUserId(user.getId());
//        member.setGroupId(id);
//        member.setRole("맴버");

        StudyGroup group = studyGroupRepository.findById(id);

        if (group.getType().equals("공개")) {
            studyMemberRepository.createApproved(member);
            studyGroupRepository.addMemberCountById(id);
        } else {
            studyMemberRepository.createPending(member);
        }
        return "redirect:/study/"+id;
    }

}
