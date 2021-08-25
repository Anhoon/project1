package kr.co.smartcube.xcube.mybatis.dao;

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
}