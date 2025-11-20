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
@CrossOrigin(origins = "http://127.0.0.1:5500")   // 或開發階段先用 "*"
@RequestMapping("/api/insertTestData")
public class InsertTestDataController {

	private final InsertTestDataMapper insertTestDataMapper;
	
	private final insertTestDataService insertTestDataService;
	
	@PostMapping("/queryTrain")
	public ApiResponse<?> queryNewestTrainData(@RequestBody TrainModel model) {
		String result = insertTestDataMapper.queryNewestTrainData(model);
		
		return ApiResponse.ok(result);
	}
	
	@PostMapping("/insertData")
	public ApiResponse<?> insertTestData(@RequestBody TrainModel reqVo) {
		int insertNumber = insertTestDataService.insertTestData(reqVo);
		
		return ApiResponse.ok(insertNumber);
	}
}
