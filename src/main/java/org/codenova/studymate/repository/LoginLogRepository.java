package org.codenova.studymate.repository;

import org.codenova.studymate.model.LoginLog;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public class LoginLogRepository {

    @Autowired
    private SqlSessionTemplate sqlSession;

    public int create(String userId) {
        return sqlSession.insert("loginLog.create", userId);
    }

    public List<LoginLog> findByUserId(String userId) {
        return sqlSession.selectList("loginLog.findByUserId", userId);
    }

    public LoginLog findLatestByUserId(String userId){
        return sqlSession.selectOne("loginLog.findLatestByUserId", userId);
    }

}
