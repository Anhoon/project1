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

	public List<Map<String, Object>> selectFileList(Map<String, Object> paramMap) {
		return sqlSession.selectList(NAMESPACE + "selectFileList",paramMap);
    }

	public Map<String, Object> selectFile(Map<String, Object> paramMap) {
		return sqlSession.selectOne(NAMESPACE + "selectFile",paramMap);
    }

	public int insertFile(List<Map<String, Object>> paramMap){
		int i = 0;
		for(Map<String,Object> map : paramMap){
			sqlSession.insert(NAMESPACE + "insertFile",map);
			i++;
		}
		return i;
	}

	public int insertFileList(List<Map<String, Object>> paramList){
		return sqlSession.insert(NAMESPACE + "insertFileList",paramList);
	}

	public int deleteFile(Map<String, Object> paramMap){
		return sqlSession.delete(NAMESPACE + "deleteFile",paramMap);
	}

	public int deleteFileList(Map<String, Object> paramMap){
		return sqlSession.delete(NAMESPACE + "deleteFileList",paramMap);
	}
}