package kr.co.smartcube.xcube.mybatis.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class MyPageDao {
	protected static final String NAMESPACE = "myPage.";

    @Autowired
    @Qualifier("sqlSession")
	private SqlSession sqlSession;

	public List<Map<String, Object>> selectMyEventList(Map<String,Object> param) {
		return sqlSession.selectList(NAMESPACE + "selectMyEventList",param);
    }

	public List<Map<String, Object>> participateCompanyHist(Map<String,Object> param) {
		return sqlSession.selectList(NAMESPACE + "participateCompanyHist",param);
    }

	public List<Map<String, Object>> participateUserHist(Map<String,Object> param) {
		return sqlSession.selectList(NAMESPACE + "participateUserHist",param);
    }

	public int insertParticipateUser(Map<String, Object> paramMap) throws Exception {
		return sqlSession.insert(NAMESPACE + "insertParticipateUser", paramMap);
	}
}