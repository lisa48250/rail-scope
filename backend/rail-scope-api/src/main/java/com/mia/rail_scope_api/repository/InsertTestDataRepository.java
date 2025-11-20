package com.mia.rail_scope_api.repository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.mia.rail_scope_api.mapper.InsertTestDataMapper;
import com.mia.rail_scope_api.model.TrainModel;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class InsertTestDataRepository {

	private final InsertTestDataMapper insertTestDataMapper;
	
	/**
	 * 查詢最新車次，取得最新的車次號碼
	 * 
	 * @param trainTypeId 車種
	 * @param direction 0=北上、1=南下
	 * @return trainNo : 車次號碼
	 * @throws IllegalStateException result is null
	 */
	public String queryNewestTrainData(TrainModel model) {
		
		String result = insertTestDataMapper.queryNewestTrainData(model);
		
		if(StringUtils.isBlank(result)) {
			throw new IllegalStateException("查無此火車班次");
		}
		return result;
	}
	
	/**
	 * 新增車次資料
	 */
	public void insertTrainTestData(TrainModel model) {
		insertTestDataMapper.insertTrainTestData(model);
	}
	
	/**
	 * 新增車次行駛資料
	 */
	public int insertTrainStopTimeTestData(String newTrainNo, String oldTrainNo) {
		int result = insertTestDataMapper.insertTrainStopTimeTestData(newTrainNo,oldTrainNo);
		return result;
	}
}

