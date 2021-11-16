package kr.co.springboot.project.services;

import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.springboot.project.mybatis.dao.BoardDao;

@Service
public class BoardService {

    @Autowired
    private BoardDao boardDao;

    public PageInfo<Map<String, Object>> selectBoardList(Map<String,Object> paramMap) {
        PageHelper.startPage((int)paramMap.get("pageNum"), (int)paramMap.get("pageSize"), (String) paramMap.get("orderBy"));
        return boardDao.selectBoardList(paramMap);
    }

    @Transactional
    public void insertBoard(Map<String, Object> map) throws Exception{
        boardDao.insertBoard(map);
    }

    @Transactional
    public void updateBoard(Map<String, Object> map) throws Exception{
        boardDao.updateBoard(map);
    }

    @Transactional
    public void deleteBoard(Map<String, Object> map) throws Exception{
        boardDao.deleteBoard(map);
    }
}