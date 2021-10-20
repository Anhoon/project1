package kr.co.smartcube.xcube.mybatis.dao;

import java.util.Map;

import com.github.pagehelper.PageInfo;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class ParticipateDao {
	protected static final String NAMESPACE = "participate.";

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
}