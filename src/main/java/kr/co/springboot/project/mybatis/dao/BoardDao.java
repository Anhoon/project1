package kr.co.springboot.project.mybatis.dao;

import java.util.Map;

import com.github.pagehelper.PageInfo;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class BoardDao {
	protected static final String NAMESPACE = "board.";

    @Autowired
    @Qualifier("sqlSession")
	private SqlSession sqlSession;
	
	public PageInfo<Map<String, Object>> selectBoardList(Map<String,Object> paraMap){
		return new PageInfo<Map<String,Object>> (sqlSession.selectList(NAMESPACE + "selectBoardList", paraMap));
	}

	public int insertBoard(Map<String, Object> param){
		return sqlSession.insert(NAMESPACE + "insertBoard",param);
	}

	public int updateBoard(Map<String, Object> param){
		return sqlSession.update(NAMESPACE + "updateBoard",param);
	}

	public int deleteBoard(Map<String, Object> param){
		return sqlSession.delete(NAMESPACE + "deleteBoard",param);
	}
}