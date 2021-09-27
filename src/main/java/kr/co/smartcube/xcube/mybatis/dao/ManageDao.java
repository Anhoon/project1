package kr.co.smartcube.xcube.mybatis.dao;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class ManageDao {
	protected static final String NAMESPACE = "manage.";

    @Autowired
    @Qualifier("sqlSession")
	private SqlSession sqlSession;

	/*참가기업*/
	public PageInfo<Map<String, Object>> selectJoinCompanyList(Map<String,Object> paramMap) throws Exception {
		return new PageInfo<Map<String,Object>> (sqlSession.selectList(NAMESPACE + "selectJoinCompanyList", paramMap));
    }

	public Map<String, Object> selectJoinCompany(Map<String, Object> paramMap) throws Exception {
		return ( Map<String, Object>)sqlSession.selectOne(NAMESPACE + "selectJoinCompany", paramMap);
    }

	public int insertJoinCompany(Map<String, Object> paramMap) throws Exception {
		return sqlSession.insert(NAMESPACE + "insertJoinCompany", paramMap);
	}

	public int updateJoinCompany(Map<String, Object> paramMap) throws Exception {
		return sqlSession.update(NAMESPACE + "updateJoinCompany", paramMap);
	}

	public int deleteJoinCompany(Map<String, Object> paramMap) throws Exception {
		return sqlSession.update(NAMESPACE + "deleteJoinCompany", paramMap);
	}

	/*참여인력*/
	public PageInfo<Map<String, Object>> selectJoinUserList(Map<String,Object> paramMap) throws Exception {
		return new PageInfo<Map<String,Object>> (sqlSession.selectList(NAMESPACE + "selectJoinUserList", paramMap));
    }

	public Map<String, Object> selectJoinUser(Map<String, Object> paramMap) throws Exception {
		return sqlSession.selectOne(NAMESPACE + "selectJoinUser", paramMap);
    }

	public int insertJoinUser(Map<String, Object> paramMap) throws Exception {
		return sqlSession.insert(NAMESPACE + "insertJoinUser", paramMap);
	}

	public int updateJoinUser(Map<String, Object> paramMap) throws Exception {
		return sqlSession.update(NAMESPACE + "updateJoinUser", paramMap);
	}

	public int deleteJoinUser(Map<String, Object> paramMap) throws Exception {
		return sqlSession.update(NAMESPACE + "deleteJoinUser", paramMap);
	}
}