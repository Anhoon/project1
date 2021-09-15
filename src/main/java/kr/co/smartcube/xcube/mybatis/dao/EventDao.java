package kr.co.smartcube.xcube.mybatis.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class EventDao {
	protected static final String NAMESPACE = "event.";

    @Autowired
    @Qualifier("sqlSession")
	private SqlSession sqlSession;

	public List<Map<String, Object>> selectEventList(Map<String,Object> param) {
		return sqlSession.selectList(NAMESPACE + "selectEventList");
    }
	public Map<String, Object> selectEvent(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "selectEvent",param);
    }
	public Map<String, Object> selectParticipate(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "selectParticipate",param);
    }
	public int insertEvent(Map<String, Object> param){
		return sqlSession.insert(NAMESPACE + "insertEvent",param);
	}
	public int insertParticipate(Map<String, Object> param){
		return sqlSession.insert(NAMESPACE + "insertParticipate",param);
	}
	public int updateEvent(Map<String, Object> param){
		return sqlSession.update(NAMESPACE + "updateEvent",param);
	}
	public int updateParticipate(Map<String, Object> param){
		return sqlSession.update(NAMESPACE + "updateParticipate",param);
	}
}