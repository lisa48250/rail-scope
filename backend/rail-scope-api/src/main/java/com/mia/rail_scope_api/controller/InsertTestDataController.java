package com.mia.rail_scope_api.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mia.rail_scope_api.mapper.InsertTestDataMapper;
import com.mia.rail_scope_api.model.TrainModel;
import com.mia.rail_scope_api.response.ApiResponse;
import com.mia.rail_scope_api.service.insertTestDataService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5501")   // 或開發階段先用 "*"
@RequestMapping("/api/insertTestData")
public class InsertTestDataController {

	private final insertTestDataService insertTestDataService;
	
	//單筆新增train、trainStopTime資料
	@PostMapping("/insertTrainWithStops")
	public ApiResponse<?> insertTrainWithStops(@RequestBody TrainModel reqVo) {
		int result = insertTestDataService.insertTrainWithStops(reqVo);
		
		return ApiResponse.ok(result);
	}
	
	//多筆新增train、trainStopTime資料
	@PostMapping("/insertBatchTrainWithStops")
	public ApiResponse<?> insertBatchTrainWithStops(@RequestBody TrainModel reqVo) {
		int result = insertTestDataService.insertBatchTrainWithStops(reqVo);
		return ApiResponse.ok(result);
	}
}