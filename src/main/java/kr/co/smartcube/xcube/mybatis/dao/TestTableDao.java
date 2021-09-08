package kr.co.smartcube.xcube.mybatis.dao;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class TestTableDao {
	protected static final String NAMESPACE = "test1.";

    @Autowired
    @Qualifier("sqlSession")
	private SqlSession sqlSession;

	public String selectHistory(){
		return sqlSession.selectOne(NAMESPACE + "selectHistory");
	}

	public PageInfo<List<Map<String, Object>>> selectHistory1(Map<String,Object> paraMap){
		return new PageInfo<List<Map<String,Object>>> (sqlSession.selectList(NAMESPACE + "selectHistory1", paraMap));
	}
}