package kr.co.smartcube.xcube.services;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.smartcube.xcube.mybatis.dao.FileDao;
import kr.co.smartcube.xcube.mybatis.dao.TestTableDao;
import kr.co.smartcube.xcube.util.Util;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TestService {

    @Autowired
    private SqlSession sqlSession;

    @Autowired
    private FileUploadDownloadService fileService;

    @Autowired
    private TestTableDao testTableDao;

    @Autowired
    private FileDao fileDao;

    public void test() {
        List<String> test = sqlSession.selectList("test1.selectHistory");

        for (String s : test) log.info(s);
    }

    public PageInfo<List<Map<String, Object>>> selectHistory1(Map<String,Object> paramMap) {
        PageHelper.startPage((int)paramMap.get("pageNum"), (int)paramMap.get("pageSize"), (String) paramMap.get("orderBy"));
        return testTableDao.selectHistory1(paramMap);
    }

    @Transactional
    public void insertBoardInfo(Map<String, Object> map) throws Exception{
        Map<String,Object> paramMap = new HashMap<String,Object>();
        List<Map<String,Object>> paramList = new ArrayList<Map<String,Object>>();
        Map<String,Object> fileGroupInfoMap = new HashMap<String,Object>();
        List<Map<String,Object>> fileList = Util.objToList(map.get("files"));
        Map<String,Object> context = Util.objToMap(map.get("context"));
        Map<String,Object> fileMap = null;
        
        fileGroupInfoMap.put("fileGroup", map.get("fileGroup"));

        if(!Util.isEmpty(fileList)){
            for(int i=0 ; i<fileList.size() ; i++){
                fileMap = Util.objToMap(fileList.get(i));
                fileGroupInfoMap.put("fileSubGroup", fileMap.get("fileSubGroup"));
            }

            paramMap = fileService.fileUploadByte(Util.objToStr(fileMap.get("fileName")), Util.objToStr(fileMap.get("fileContent")), fileGroupInfoMap);
            paramList.add(paramMap);
        }
       
        fileDao.insertFile(paramList);
    }

}