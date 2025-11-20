package com.mia.rail_scope_api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.mia.rail_scope_api.model.TrainModel;
import com.mia.rail_scope_api.repository.InsertTestDataRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class insertTestDataService {

	private final InsertTestDataRepository insertTestDataRepository;

	/**
	 * 新增train、trainStopTime資料
	 * @param trainTypeId 車種
	 * @param direction 0=北上、1=南下
	 * @return 新增幾筆trainStopTime
	 */
	@Transactional(rollbackFor = Exception.class)
	public int insertTestData(@RequestBody TrainModel model) {

		TrainModel insertData = new TrainModel();
		int result ;
		//trainNo增加單位
		int number = 15;

		//1.取得最新的車次號碼
		String oldTrainNo = insertTestDataRepository.queryNewestTrainData(model);
		
		//整理train新增資料
		BeanUtils.copyProperties(model,insertData);
		//給予新的trainNo
		String newTrainNo = String.valueOf(Integer.parseInt(oldTrainNo) + number);
		insertData.setTrainNo(newTrainNo);
		
		//3.新增train
		insertTestDataRepository.insertTrainTestData(insertData);

		//4.新增trainStopTime
		result = insertTestDataRepository.insertTrainStopTimeTestData(newTrainNo, oldTrainNo);

		return result;
	}
	
//	/**
//	 * 新增train、trainStopTime資料
//	 * @param trainTypeId 車種
//	 * @param direction 0=北上、1=南下
//	 * @return 新增幾筆trainStopTime
//	 */
//	@Transactional(rollbackFor = Exception.class)
//	public int insertTestData(@RequestBody TrainModel model) {
//		
//	}
}
