package kr.co.smartcube.xcube.mybatis.dao;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class FileManageDao {
	protected static final String NAMESPACE = "fileManage.";

    @Autowired
    @Qualifier("sqlSession")
	private SqlSession sqlSession;

	/*파일관리*/
	public PageInfo<Map<String, Object>> selectFileManageList(Map<String,Object> paramMap) throws Exception {
		return new PageInfo<Map<String,Object>> (sqlSession.selectList(NAMESPACE + "selectFileManageList", paramMap));
    }

	public Map<String, Object> selectFileManage(Map<String, Object> paramMap) throws Exception {
		return sqlSession.selectOne(NAMESPACE + "selectFileManage", paramMap);
    }

	public int insertFileManage(Map<String, Object> paramMap) throws Exception {
		return sqlSession.insert(NAMESPACE + "insertFileManage", paramMap);
    }

	public int updateFileManage(Map<String, Object> paramMap) throws Exception {
		return sqlSession.update(NAMESPACE + "updateFileManage", paramMap);
    }

	public int deleteFileManage(Map<String, Object> paramMap) throws Exception {
		return sqlSession.delete(NAMESPACE + "deleteFileManage", paramMap);
    }

	public int deleteFileManageList(List<Map<String, Object>> paramList) throws Exception {
		return sqlSession.delete(NAMESPACE + "deleteFileManageList", paramList);
    }
}