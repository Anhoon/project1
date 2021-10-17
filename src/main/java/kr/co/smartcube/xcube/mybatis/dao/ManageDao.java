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
	public PageInfo<Map<String, Object>> selectParticipateManageList(Map<String,Object> paramMap) throws Exception {
		return new PageInfo<Map<String,Object>> (sqlSession.selectList(NAMESPACE + "selectParticipateManageList", paramMap));
    }

	public Map<String, Object> selectParticipateManage(Map<String, Object> paramMap) throws Exception {
		return sqlSession.selectOne(NAMESPACE + "selectParticipateManage", paramMap);
    }

	public int insertParticipateManage(Map<String, Object> paramMap) throws Exception {
		return sqlSession.insert(NAMESPACE + "insertParticipateManage", paramMap);
	}

	public int updateParticipateManage(Map<String, Object> paramMap) throws Exception {
		return sqlSession.update(NAMESPACE + "updateParticipateManage", paramMap);
	}

	public int updateParticipateManageApproval(Map<String, Object> paramMap) throws Exception {
		return sqlSession.update(NAMESPACE + "updateParticipateManageApproval", paramMap);
	}

	public int deleteParticipateManage(Map<String, Object> paramMap) throws Exception {
		return sqlSession.delete(NAMESPACE + "deleteParticipateManage", paramMap);
	}

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

	/*3D제작요청관리*/
	public PageInfo<Map<String, Object>> select3DManageList(Map<String,Object> paramMap) throws Exception {
		return new PageInfo<Map<String,Object>> (sqlSession.selectList(NAMESPACE + "select3DManageList", paramMap));
    }

	public Map<String, Object> select3DManage(Map<String, Object> paramMap) throws Exception {
		return sqlSession.selectOne(NAMESPACE + "select3DManage", paramMap);
    }

	public int insert3DManage(Map<String, Object> paramMap) throws Exception {
		return sqlSession.insert(NAMESPACE + "insert3DManage", paramMap);
    }

	public int update3DManage(Map<String, Object> paramMap) throws Exception {
		return sqlSession.update(NAMESPACE + "update3DManage", paramMap);
    }

	public int delete3DManage(Map<String, Object> paramMap) throws Exception {
		return sqlSession.delete(NAMESPACE + "delete3DManage", paramMap);
    }

	public int delete3DManageList(List<Map<String, Object>> paramList) throws Exception {
		return sqlSession.delete(NAMESPACE + "delete3DManageList", paramList);
    }
}