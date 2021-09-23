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
	public PageInfo<List<Map<String, Object>>> selectJoinCompanyList(Map<String,Object> param) throws Exception {
		return new PageInfo<List<Map<String,Object>>> (sqlSession.selectList(NAMESPACE + "selectJoinCompany"));
    }

	public Map<String, Object> selectJoinCompany(Map<String, Object> param) throws Exception {
		return sqlSession.selectOne(NAMESPACE + "selectJoinCompany",param);
    }

	public int insertJoinCompany(Map<String, Object> param) throws Exception {
		return sqlSession.insert(NAMESPACE + "insertJoinCompany",param);
	}

	public int updateJoinCompany(Map<String, Object> param) throws Exception {
		return sqlSession.update(NAMESPACE + "updateJoinCompany",param);
	}

	public int deleteJoinCompany(Map<String, Object> param) throws Exception {
		return sqlSession.update(NAMESPACE + "deleteJoinCompany",param);
	}

	/*참여인력*/
	public PageInfo<List<Map<String, Object>>> selectJoinUserList(Map<String,Object> param) throws Exception {
		return new PageInfo<List<Map<String,Object>>> (sqlSession.selectList(NAMESPACE + "selectJoinUser"));
    }

	public Map<String, Object> selectJoinUser(Map<String, Object> param) throws Exception {
		return sqlSession.selectOne(NAMESPACE + "selectJoinUser",param);
    }

	public int insertJoinUser(Map<String, Object> param) throws Exception {
		return sqlSession.insert(NAMESPACE + "insertJoinUser",param);
	}

	public int updateJoinUser(Map<String, Object> param) throws Exception {
		return sqlSession.update(NAMESPACE + "updateJoinUser",param);
	}

	public int deleteJoinUser(Map<String, Object> param) throws Exception {
		return sqlSession.update(NAMESPACE + "deleteJoinUser",param);
	}
}