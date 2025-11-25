package com.mia.rail_scope_api.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mia.rail_scope_api.mapper.QueryTrainTimetableMapper;
import com.mia.rail_scope_api.requestVo.QueryTrainTimetableReqVo;
import com.mia.rail_scope_api.responseVo.QueryTrainTimetableRespVo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Repository
@Slf4j
public class QueryTrainTimetableRrepository {

	private final QueryTrainTimetableMapper queryTrainTimetableMapper;
	
	public List<QueryTrainTimetableRespVo> queryTrainTimetable(QueryTrainTimetableReqVo reqVo){
		
		List<QueryTrainTimetableRespVo> resultList = queryTrainTimetableMapper.queryTrainTimetable(reqVo);
		if(resultList.isEmpty()) {
			log.info("查無火車資訊");
		}
		return resultList;
	}
	
}
