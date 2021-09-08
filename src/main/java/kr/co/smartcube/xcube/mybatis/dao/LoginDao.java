package kr.co.smartcube.xcube.mybatis.dao;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class LoginDao {
	protected static final String NAMESPACE = "login.";

	@Autowired
    @Qualifier("sqlSession")
	private SqlSession sqlSession;

	public Map<String, Object> selectLoginInfo(String username){
		return sqlSession.selectOne(NAMESPACE + "selectLoginInfo", username);
	}

	public Map<String, Object> selectLoginInfo(Map<String,Object> paramMape){
		return sqlSession.selectOne(NAMESPACE + "selectLoginInfo", paramMape);
	}

	public int updateToken(Map<String,Object> paramMap){
		return sqlSession.update(NAMESPACE + "updateToken", paramMap);
	}

	public int deleteToken(Map<String,Object> paramMap){
		return sqlSession.update(NAMESPACE + "deleteToken", paramMap);
	}
}