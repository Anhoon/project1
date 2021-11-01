package kr.co.springboot.project.mybatis.dao;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import kr.co.springboot.project.common.security.jwt.LoginVO;

@Repository
public class LoginDao {
	protected static final String NAMESPACE = "login.";

	@Autowired
    @Qualifier("sqlSession")
	private SqlSession sqlSession;

	public Map<String, Object> selectLoginInfo(Map<String,Object> paramMape){
		return sqlSession.selectOne(NAMESPACE + "selectLoginInfo", paramMape);
	}

	public LoginVO getLoginInfo(Map<String,Object> paramMape){
		return sqlSession.selectOne(NAMESPACE + "getLoginInfo", paramMape);
	}
	
}