package kr.co.smartcube.xcube.mybatis.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class SchedulerDao {
	protected static final String NAMESPACE = "scheduler.";

    @Autowired
    @Qualifier("sqlSession")
	private SqlSession sqlSession;

	public int updateEventStatus(){
		return sqlSession.update(NAMESPACE + "updateEventStatus");
	}
	public int updateParticipateStatus(){
		return sqlSession.update(NAMESPACE + "updateParticipateStatus");
	}
}