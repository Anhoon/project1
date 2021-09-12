package kr.co.smartcube.xcube.mybatis.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class FileDao {
	protected static final String NAMESPACE = "file.";

    @Autowired
    @Qualifier("sqlSession")
	private SqlSession sqlSession;

	public List<Map<String, Object>> selectFileList(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "selectFileList",param);
    }
	public Map<String, Object> selectFile(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "selectFile",param);
    }
	public int insertFile(List<Map<String, Object>> param){
		int i = 0;
		for(Map<String,Object> map : param){
			sqlSession.insert(NAMESPACE + "insertFile",map);
			i++;
		}
		return i;
	}
	public int updateFile(List<Map<String, Object>> param){
		return sqlSession.update(NAMESPACE + "updateFile",param);
	}	
}