package kr.co.smartcube.xcube.services;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TestService {

    @Autowired
    private SqlSession sqlSession;

    public void test() {
        List<String> test = sqlSession.selectList("test1.selectHistory");

        for (String s : test) log.info(s);
    }

}