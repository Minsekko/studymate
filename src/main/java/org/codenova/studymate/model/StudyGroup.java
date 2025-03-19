package org.codenova.studymate.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StudyGroup {
    private String id;
    private String name;
    private String type;
    private String goal;
    private String createId;
    private String createAt;
    private String memberCont;
}
