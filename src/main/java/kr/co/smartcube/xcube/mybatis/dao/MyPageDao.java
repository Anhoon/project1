package kr.co.smartcube.xcube.mybatis.dao;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;

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

	public PageInfo<Map<String, Object>> selectParticipateApplyHist(Map<String,Object> param) {
		return new PageInfo<Map<String,Object>> (sqlSession.selectList(NAMESPACE + "selectParticipateApplyHist",param));
    }

	public Map<String, Object> selectParticipateApplyHistDetail(Map<String,Object> param) {
		return sqlSession.selectOne(NAMESPACE + "selectParticipateApplyHistDetail",param);
    }

	public PageInfo<Map<String, Object>> selectParticipateAuthHist(Map<String,Object> param) {
		return new PageInfo<Map<String,Object>> (sqlSession.selectList(NAMESPACE + "selectParticipateAuthHist",param));
    }

	public int insertParticipateAuth(Map<String, Object> paramMap) throws Exception {
		return sqlSession.insert(NAMESPACE + "insertParticipateAuth", paramMap);
	}
}