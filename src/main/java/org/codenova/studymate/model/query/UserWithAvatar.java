package org.codenova.studymate.model.query;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserWithAvatar {
    private String id;
    private String name;
    private String password;
    private int avatarId;
    private int loginCount;
    private int studyTime;

    private String avatarName;
    private String avatarUrl;

}
