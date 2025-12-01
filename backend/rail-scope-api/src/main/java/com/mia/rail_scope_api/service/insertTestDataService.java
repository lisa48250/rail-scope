package com.mia.rail_scope_api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mia.rail_scope_api.model.TrainModel;
import com.mia.rail_scope_api.repository.InsertTestDataRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class insertTestDataService {

	private final InsertTestDataRepository insertTestDataRepository;

	//-----------------------------新增相關-----------------------------------------
	/**
	 * 新增一班火車班次
	 * 單筆新增train、trainStopTime資料
	 * 
	 * @param trainTypeId 車種
	 * @param direction   0=北上、1=南下
	 */
	@Transactional(rollbackFor = Exception.class)
	public int insertTrainWithStops(TrainModel model) {

		// 取得最新的車次號碼
		String trainNo = this.getNewestTrainNo(model);
		// 新增train、trainStopTime
		return this.insertData(model, trainNo);
	}

	/**
	 * 新增"單一車種"一日火車班次量
	 * 多筆新增train、trainStopTime資料
	 * 
	 * @param trainTypeId 車種
	 * @param direction   0=北上、1=南下
	 */
	@Transactional(rollbackFor = Exception.class)
	public int insertBatchTrainWithStops(TrainModel model) {

		//新增筆數
		int number = 0;
		//循環次數(一日需要幾班火車)
		int count = 0;
		switch (model.getTrainTypeId()) {
		case 1 -> count = 10; // 自強號: 一天11班
		case 2, 4 -> count = 2; // 普悠瑪、莒光號: 一天3班
		case 3 -> count = 1; // 太魯閣: 一天2班
		case 5 -> count = 47; // 區間: 一天48班
		case 6 -> count = 5; // 區間快: 一天6班
		case 7 -> count = 4; // EMU3000 自強號: 一天5班
		default -> throw new IllegalArgumentException("trainTypeId 不合法: " + model.getTrainTypeId());
		}
		
		//迴圈新增
		for (int i = 0; i < count; i++) {
			number += this.insertTrainWithStops(model);
		}
		return number;
	}
	
	/**
	 * 新增"全部車種"一日火車班次
	 * 多筆新增train、trainStopTime資料
	 * 
	 * @param trainTypeId 車種
	 * @param direction   0=北上、1=南下
	 */
	@Transactional(rollbackFor = Exception.class)
	public int batchInsertTrainsAndStopsForAllTypes() {
		
		log.info("新增所有車種一日全部班次資料");
		//新增幾筆資料
		int intNumber = 0;
		//trainType種類
		int[] arr = {1,2,4,5,6,7};
		for(int i = 0; i < arr.length ; i++) {
			TrainModel model = new TrainModel();
			//設定為南下(0=北上、1=南下)
			model.setDirection(1);
			//設定為車種類型
			model.setTrainTypeId(arr[i]);
			log.info("新增 北下 車種類型為"+model.getTrainTypeId()+"，方向為:"+model.getDirection()+"的資料");
			intNumber += this.insertBatchTrainWithStops(model);
			
			//設定為北上(0=北上、1=南下)
			model.setDirection(0);
			log.info("新增 南上 車種類型為"+model.getTrainTypeId()+"，方向為:"+model.getDirection()+"的資料");
			intNumber += this.insertBatchTrainWithStops(model);
		}
		
		return intNumber;
	}
	
	//-----------------------------呼叫方法-----------------------------------------4
	

	// 取得最新的車次號碼
	public String getNewestTrainNo(TrainModel model) {
		return insertTestDataRepository.queryNewestTrainData(model);
	}

	// 新增train、trainStopTime
	public int insertData(TrainModel model, String trainNo) {
		int result;

		// trainNo增加單位
		int number = 3;
		// 增加每站間隔時間
		int time;
		// 整理train新增資料(每班次間隔時間)
		switch (model.getTrainTypeId()) {
		case 1 -> time = 60; // 自強號: 一天18班
		case 2-> time = 360; // 普悠瑪: 一天3班
		case 3-> time = 540; // 太魯閣、莒光號: 一天2班
		case 4-> time = 240; // 莒光號: 一天2班
		case 5 -> time = 16; // 區間: 一天65班
		case 6 -> time = 180; // 區間快: 一天6班
		case 7 -> time = 160; // EMU3000 自強號: 一天5班
		default -> throw new IllegalArgumentException("trainTypeId 不合法: " + model.getTrainTypeId());
		}
		// 給予新的trainNo
		String newTrainNo = String.valueOf(Integer.parseInt(trainNo) + number);

		// 3.新增train
		log.info("開始新增train測試資料: ", newTrainNo, model.getTrainTypeId(), model.getDirection());
		insertTestDataRepository.insertTrainTestData(newTrainNo, model.getTrainTypeId(), model.getDirection());

		// 4.新增trainStopTime
		log.info("開始新增trainStopTime測試資料: ", newTrainNo, trainNo, time);
		result = insertTestDataRepository.insertTrainStopTimeTestData(newTrainNo, trainNo, time);
		log.info("新增完成: {} 筆", result);

		return result;
	}

}
