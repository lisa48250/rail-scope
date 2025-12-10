package com.mia.rail_scope_api.repository;

import org.springframework.stereotype.Repository;

import com.mia.rail_scope_api.mapper.InsertTrainDataMapper;
import com.mia.rail_scope_api.model.TrainModel;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class InsertTrainDataRepository {

	private final InsertTrainDataMapper insertTrainDataMapper;
	
	/**
	 * 查詢最新車次，取得最新的車次號碼
	 * 
	 * @param trainTypeId 車種
	 * @param direction 0=北上、1=南下
	 * @return trainNo : 車次號碼
	 * @throws IllegalStateException result is null
	 */
	public String queryNewestTrainData(TrainModel model) {
		
		String result = insertTrainDataMapper.queryNewestTrainData(model);
		
		if(result == null) {
			throw new IllegalStateException("查無此火車班次");
		}
		return result;
	}
	
	/**
	 * 新增車次資料
	 */
	public void insertTrainTestData(String trainNo,int trainTypeId,int direction) {
		System.out.println("帶入變數值trainNo,trainTypeId, direction:"+ trainNo+","+trainTypeId+","+direction);
		int result =  insertTrainDataMapper.insertTrainTestData(trainNo,trainTypeId, direction);
	    if (result == 0) {
	        throw new IllegalStateException("未新增任何 train 資料");
	    }
	}
	
	/**
	 * 新增車次行駛資料
	 */
	public int insertTrainStopTimeTestData(String newTrainNo, String oldTrainNo, int time) {
		int result = insertTrainDataMapper.insertTrainStopTimeTestData(newTrainNo,oldTrainNo,time);
	    if (result == 0) {
	        throw new IllegalStateException("未新增任何 trainStopTime 資料");
	    }
		return result;
	}
}
