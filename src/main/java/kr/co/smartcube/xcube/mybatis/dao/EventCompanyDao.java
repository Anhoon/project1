package kr.co.smartcube.xcube.mybatis.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class EventCompanyDao {
	protected static final String NAMESPACE = "eventCompany.";

    @Autowired
    @Qualifier("sqlSession")
	private SqlSession sqlSession;

	public List<Map<String, Object>> selectMasterLicenseList(Map<String,Object> param) {
		return sqlSession.selectList(NAMESPACE + "selectMasterLicenseList");
    }
	public Map<String, Object> selectMasterLicense(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "selectMasterLicense",param);
    }
	public List<Map<String, Object>> selectMasterLicenseListDetailList(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "selectMasterLicenseListDetailList",param);
    }
	public int insertMasterLicense(Map<String, Object> param){
		return sqlSession.insert(NAMESPACE + "insertMasterLicense",param);
	}
	public int insertMasterLicenseList(List<Map<String, Object>> param){
		return sqlSession.insert(NAMESPACE + "insertMasterLicenseList",param);
	}
	public int updateMasterLicense(Map<String, Object> param){
		return sqlSession.update(NAMESPACE + "updateMasterLicense",param);
	}
	public int updateMasterLicenseList(Map<String, Object> param){
		return sqlSession.update(NAMESPACE + "updateMasterLicenseList",param);
	}
}