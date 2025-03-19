package org.codenova.studymate.repository;


import lombok.AllArgsConstructor;
import org.codenova.studymate.model.Avatar;
import org.codenova.studymate.model.User;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@AllArgsConstructor
public class UserRepository {
    private SqlSessionTemplate template;

    public int create(User user) {
        return template.insert("user.create",user);
    }

    public User findById(String id) {
        return template.selectOne("user.findById", id);
    }

    public int updateLoginCountByUserId(String id) {
        return template.update("user.updateLoginCountByUserId", id);
    }
}
