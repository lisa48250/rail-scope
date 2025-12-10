package com.mia.rail_scope_api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.mia.rail_scope_api.mapper.TidyTrainRunMapper;
import com.mia.rail_scope_api.model.TrainRunModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TidyTrainRunRepository {

	private final TidyTrainRunMapper tidyTrainRunMapper;
	
	/**
	 * 查詢trainRun是否有30天日期資料
	 */
	public int queryTrainRunDay(LocalDate today,LocalDate after30Days) {
		int result = tidyTrainRunMapper.queryTrainRunDay(today,after30Days);
		return result;
	}
	
	public List<String> queryTrainAllTrainNo(){
		List<String> resultList = tidyTrainRunMapper.queryTrainAllTrainNo();
		return resultList;
	}
	
	
	public void insertTrainRunNewDate(List<TrainRunModel> trainRunList) {
		int result = tidyTrainRunMapper.insertTrainRunNewDate(trainRunList);
		log.info("trainRun新增"+result+"筆資料");
	}
	
	public void deleteTrainRunOld(LocalDate today) {
		int result = tidyTrainRunMapper.deleteTrainRunOld(today);
		log.info("trainRun刪除"+result+"筆資料");
	}
}
