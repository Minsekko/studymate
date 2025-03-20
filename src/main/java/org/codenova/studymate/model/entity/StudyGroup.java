package org.codenova.studymate.model.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StudyGroup {
    private String id;
    private String name;
    private String type;
    private String goal;
    private String creatorId;
    private String creatAt;
    private String memberCount;
}
