package kr.co.smartcube.xcube.mybatis.dao;

import java.util.Map;

import com.github.pagehelper.PageInfo;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AuthDao {
	protected static final String NAMESPACE = "auth.";

    @Autowired
    @Qualifier("sqlSession")
	private SqlSession sqlSession;

	/*권한관리*/
	public PageInfo<Map<String, Object>> selectAuthManageList(Map<String,Object> paramMap) throws Exception {
		return new PageInfo<Map<String,Object>> (sqlSession.selectList(NAMESPACE + "selectAuthManageList", paramMap));
    }

	public Map<String, Object> selectAuthManage(Map<String, Object> paramMap) throws Exception {
		return sqlSession.selectOne(NAMESPACE + "selectAuthManage", paramMap);
    }

	public int insertAuthManage(Map<String, Object> paramMap) throws Exception {
		return sqlSession.insert(NAMESPACE + "insertAuthManage", paramMap);
	}

	public int updateAuthManage(Map<String, Object> paramMap) throws Exception {
		return sqlSession.update(NAMESPACE + "updateAuthManage", paramMap);
	}

	public int updateAuthManageAuth(Map<String, Object> paramMap) throws Exception {
		return sqlSession.update(NAMESPACE + "updateAuthManageAuth", paramMap);
	}

	public int deleteAuthManage(Map<String, Object> paramMap) throws Exception {
		return sqlSession.delete(NAMESPACE + "deleteAuthManage", paramMap);
	}
}