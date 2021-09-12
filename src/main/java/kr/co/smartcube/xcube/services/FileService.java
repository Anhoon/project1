package kr.co.smartcube.xcube.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.smartcube.xcube.exception.XcubeException;
import kr.co.smartcube.xcube.mybatis.dao.FileDao;

@Service
public class FileService {

    @Autowired
    private FileDao fileDao;

    @Transactional
    public void insertFile(List<Map<String, Object>> fileList){
        try {
            fileDao.insertFile(fileList);
        } catch (Exception e) {
            throw new XcubeException(e.toString());
        }
    }
}