package com.celest.backend.service.record.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.celest.backend.mapper.RecordMapper;
import com.celest.backend.mapper.UserMapper;
import com.celest.backend.pojo.VO.RecordVO;
import com.celest.backend.pojo.entity.User;
import com.celest.backend.service.record.RecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.celest.backend.pojo.entity.Record;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final RecordMapper recordMapper;

    private final UserMapper userMapper;

    @Override
    public List<RecordVO> getRecordList(Integer currentPage) {
        log.info("currentPage: {}",currentPage);
        IPage<Record> page = new Page<>(currentPage,10);
        LambdaQueryWrapper<Record> recordLambdaQueryWrapper = Wrappers.lambdaQuery(Record.class);
        recordLambdaQueryWrapper.orderByDesc(Record::getCreateTime);
        IPage<Record> recordIPage = recordMapper.selectPage(page, recordLambdaQueryWrapper);
        ArrayList<RecordVO> recordVOS = new ArrayList<>();
        List<Record> records = recordIPage.getRecords();
        for (Record record : records) {
            Integer playerA = record.getAId();
            Integer playerB = record.getBId();
            User userA = userMapper.selectById(playerA);
            User userB = userMapper.selectById(playerB);
            recordVOS.add(new RecordVO(userA.getPhoto(),userA.getUsername(),userB.getPhoto(),userB.getUsername(),record));
        }
        return recordVOS;
    }
}
