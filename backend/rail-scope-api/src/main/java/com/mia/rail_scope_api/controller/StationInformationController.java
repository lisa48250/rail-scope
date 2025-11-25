package com.mia.rail_scope_api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mia.rail_scope_api.response.ApiResponse;
import com.mia.rail_scope_api.responseVo.QueryStationRespVo;
import com.mia.rail_scope_api.service.StationInformationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5500")   // 或開發階段先用 "*"
@RequestMapping("/api/stationInformation")
public class StationInformationController {

	private final StationInformationService stationInformationService;
	
	
	//查詢所有縣市車站
	@GetMapping("/all")
	public ApiResponse<?> queryStationAll(){
		List<QueryStationRespVo> resultList =  stationInformationService.queryStationAll();
		return ApiResponse.ok(resultList);
	}
	
}
