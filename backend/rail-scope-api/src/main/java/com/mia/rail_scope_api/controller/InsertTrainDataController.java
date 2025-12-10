package com.mia.rail_scope_api.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mia.rail_scope_api.model.TrainModel;
import com.mia.rail_scope_api.response.ApiResponse;
import com.mia.rail_scope_api.service.InsertTrainDataService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5500")   // 或開發階段先用 "*"
@RequestMapping("/api/insertTestData")
public class InsertTrainDataController {

	private final InsertTrainDataService insertTrainDataService;
	
	//單筆新增train、trainStopTime資料(一班次)
	@PostMapping("/insertTrainWithStops")
	public ApiResponse<?> insertTrainWithStops(@RequestBody TrainModel reqVo) {
		int result = insertTrainDataService.insertTrainWithStops(reqVo);
		
		return ApiResponse.ok(result);
	}
	
	//多筆新增train、trainStopTime資料(一天班次)
	@PostMapping("/insertBatchTrainWithStops")
	public ApiResponse<?> insertBatchTrainWithStops(@RequestBody TrainModel reqVo) {
		int result = insertTrainDataService.insertBatchTrainWithStops(reqVo);
		return ApiResponse.ok(result);
	}
	
	//新增"全部車種"一日火車班次(所有出車種一天班次、包含南上北下)
	@GetMapping("/batchInsertTrainsAndStopsForAllTypes")
	public ApiResponse<?> batchInsertTrainsAndStopsForAllTypes(){
		int result = insertTrainDataService.batchInsertTrainsAndStopsForAllTypes();
		return ApiResponse.ok(result);
	}
	
	
}