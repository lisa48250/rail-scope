package com.mia.rail_scope_api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mia.rail_scope_api.model.TrainRunModel;
import com.mia.rail_scope_api.repository.TidyTrainRunRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TidyTrainRunService {

	private final TidyTrainRunRepository tidyTrainRunRepository;

	@Transactional(rollbackFor = Exception.class)
	public void tidyTrainRun() {

		log.info("tidyTrainRun() 執行於：" + LocalDateTime.now());

		// 1. 獲取今天日期及30天後日期
		LocalDate today = LocalDate.now();
		LocalDate after30Days = today.plusDays(30);
		log.info("今天日期為:" + today);
		log.info("30天候日期為:" + after30Days);

		// 2. 查詢是否有未來30天資料
		int result = tidyTrainRunRepository.queryTrainRunDay(today, after30Days);
		log.info("trainRun30天內筆數為:" + result);

		if (result < 30) {
			
			//查詢所有trainNo做新增用
			List<String> trainNoList = tidyTrainRunRepository.queryTrainAllTrainNo();

			//新增用list
			List<TrainRunModel> trainRunList = new ArrayList<>();
			//需要新增日期筆數
			int insertNumber = 30 - result;
			
			//整理新增資料
			while (insertNumber > 0) {
				//30天-天數差
				LocalDate prevDay = after30Days.minusDays(insertNumber);
				
				//放入trainNo和日期
				for(int i = 0; i < trainNoList.size(); i++) {
					TrainRunModel model = new TrainRunModel();
					model.setTrainNo(trainNoList.get(i));
					model.setServiceDate(prevDay);
					trainRunList.add(model);
				}
				insertNumber--;
			}
			
			log.info("開始新增trainRun資料");
			tidyTrainRunRepository.insertTrainRunNewDate(trainRunList);
			log.info("開始刪除trainRun資料");
			tidyTrainRunRepository.deleteTrainRunOld(today);
			
		}

	}
}
