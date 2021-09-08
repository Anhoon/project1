package kr.co.smartcube.xcube.services;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.smartcube.xcube.mybatis.dao.TestTableDao;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TestService {

    @Autowired
    private SqlSession sqlSession;

    @Autowired
    private TestTableDao testTableDao;

    public void test() {
        List<String> test = sqlSession.selectList("test1.selectHistory");

        for (String s : test) log.info(s);
    }

    public PageInfo<List<Map<String, Object>>> selectHistory1(Map<String,Object> paramMap) {
        PageHelper.startPage((int)paramMap.get("pageNum"), (int)paramMap.get("pageSize"), (String) paramMap.get("orderBy"));
        return testTableDao.selectHistory1(paramMap);
    }

}