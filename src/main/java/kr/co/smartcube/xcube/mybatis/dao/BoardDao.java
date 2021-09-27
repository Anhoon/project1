package kr.co.smartcube.xcube.mybatis.dao;

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

	/*자료등록*/
	public PageInfo<Map<String, Object>> selectBoardList(Map<String, Object> paramMap) {
		return new PageInfo<Map<String, Object>>(sqlSession.selectList(NAMESPACE + "selectBoardList", paramMap));
    }

	public Map<String, Object> selectBoard(Map<String, Object> paramMap) {
		return sqlSession.selectOne(NAMESPACE + "selectBoard", paramMap);
    }

	public int insertBoard(Map<String, Object> paramMap) {
		return sqlSession.insert(NAMESPACE + "insertBoard",paramMap);
    }

	public int updateBoard(Map<String, Object> paramMap) {
		return sqlSession.update(NAMESPACE + "updateBoard", paramMap);
    }

	public int deleteBoard(Map<String, Object> paramMap) {
		return sqlSession.delete(NAMESPACE + "deleteBoard", paramMap);
    }

	public int deleteBoardList(Map<String, Object> paramMap) {
		return sqlSession.delete(NAMESPACE + "deleteBoardList",paramMap);
    }
}