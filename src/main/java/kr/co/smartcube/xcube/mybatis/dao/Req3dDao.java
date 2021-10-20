package kr.co.smartcube.xcube.mybatis.dao;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class Req3dDao {
	protected static final String NAMESPACE = "req3d.";

    @Autowired
    @Qualifier("sqlSession")
	private SqlSession sqlSession;

	/*3D제작요청관리*/
	public PageInfo<Map<String, Object>> selectReq3dList(Map<String,Object> paramMap) throws Exception {
		return new PageInfo<Map<String,Object>> (sqlSession.selectList(NAMESPACE + "selectReq3dList", paramMap));
    }

	public Map<String, Object> selectReq3d(Map<String, Object> paramMap) throws Exception {
		return sqlSession.selectOne(NAMESPACE + "selectReq3d", paramMap);
    }

	public int insertReq3d(Map<String, Object> paramMap) throws Exception {
		return sqlSession.insert(NAMESPACE + "insertReq3d", paramMap);
    }

	public int updateReq3d(Map<String, Object> paramMap) throws Exception {
		return sqlSession.update(NAMESPACE + "updateReq3d", paramMap);
    }

	public int updateReq3dStatus(Map<String, Object> paramMap) throws Exception {
		return sqlSession.update(NAMESPACE + "updateReq3dStatus", paramMap);
    }

	public int updateReq3dDone(Map<String, Object> paramMap) throws Exception {
		return sqlSession.update(NAMESPACE + "updateReq3dDone", paramMap);
    }

	public int deleteReq3d(Map<String, Object> paramMap) throws Exception {
		return sqlSession.delete(NAMESPACE + "deleteReq3d", paramMap);
    }

	public int deleteReq3dList(List<Map<String, Object>> paramList) throws Exception {
		return sqlSession.delete(NAMESPACE + "deleteReq3dList", paramList);
    }
}