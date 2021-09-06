package kr.co.smartcube.xcube.mybatis.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
	protected static final String NAMESPACE = "user.";

    @Autowired
    @Qualifier("sqlSession")
	private SqlSession sqlSession;

	public List<Map<String, Object>> selectUserList() {
		return sqlSession.selectList(NAMESPACE + "selectUserList");
    }
	public Map<String, Object> selectUser(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "selectUser",param);
    }
	public int insertUser(Map<String, Object> param){
		return sqlSession.insert(NAMESPACE + "insertUser",param);
	}
	public int updateUser(Map<String, Object> param){
		return sqlSession.update(NAMESPACE + "updateUser",param);
	}
	public int updateUserStatus(Map<String, Object> param){
		return  sqlSession.update(NAMESPACE + "updateUserStatus",param);
	}
	public void insertUserHistory(Map<String, Object> param){
		sqlSession.insert(NAMESPACE + "insertUserHistory",param);
	}
	
}