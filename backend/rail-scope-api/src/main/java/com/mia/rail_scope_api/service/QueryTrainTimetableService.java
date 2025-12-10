package com.mia.rail_scope_api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mia.rail_scope_api.repository.QueryTrainTimetableRepository;
import com.mia.rail_scope_api.requestVo.QueryTrainTimetableReqVo;
import com.mia.rail_scope_api.responseVo.QueryTrainTimetableRespVo;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
@RequiredArgsConstructor
public class QueryTrainTimetableService {

	
	private final QueryTrainTimetableRepository queryTrainTimetableRrepository;
	
	public List<QueryTrainTimetableRespVo> queryTrainTimetable(QueryTrainTimetableReqVo reqVo){
		List<QueryTrainTimetableRespVo> resultList = new ArrayList<>();
		
		//檢查資料
		if(StringUtils.isBlank(reqVo.getStationStart())) {
			log.warn("起點車站不可為空");
			throw new IllegalArgumentException("起點車站不可為空");
		}
		if(StringUtils.isBlank(reqVo.getStationEnd())) {
			log.warn("終點車站不可為空");
			throw new IllegalArgumentException("終點車站不可為空");
		}
		if(StringUtils.isBlank(reqVo.getDirection())) {
			log.warn("列車往北往南，不可為空");
			throw new IllegalArgumentException("列車往北往南，不可為空");
		}
//		if(StringUtils.isBlank(reqVo.getTimeType())) {
//			log.warn("出發時間或抵達時間，不可為空");
//			throw new IllegalArgumentException("出發時間或抵達時間，不可為空");
//		}
		if(StringUtils.isBlank(reqVo.getVehicleType())) {
			log.warn("車輛類型，不可為空");
			throw new IllegalArgumentException("車輛類型，不可為空");
		}
//		if(StringUtils.isBlank(reqVo.getRouteType())) {
//			log.warn("直達或轉乘，不可為空");
//			throw new IllegalArgumentException("直達或轉乘，不可為空");
//		}
		if(reqVo.getDate() == null) {
			log.warn("日期不可為空");
			throw new NullPointerException("日期不可為空");
		}
		
		log.info("reqVo.getVehicleType(): "+reqVo.getVehicleType());
		//查詢火車時刻表
		resultList = queryTrainTimetableRrepository.queryTrainTimetable(reqVo);
		
		
		return resultList;
	}
}
