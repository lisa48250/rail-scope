package com.mia.rail_scope_api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mia.rail_scope_api.requestVo.QueryTrainTimetableReqVo;
import com.mia.rail_scope_api.response.ApiResponse;
import com.mia.rail_scope_api.responseVo.QueryTrainTimetableRespVo;
import com.mia.rail_scope_api.service.QueryTrainTimetableService;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5501")   // 或開發階段先用 "*"
@RequestMapping("/api/queryTrainTimetable")
public class QueryTrainTimetableController {
	
	private final QueryTrainTimetableService queryTrainTimetableService;
	
	@PostMapping("/query")
	public ApiResponse<?> queryTrainTimetable(@RequestBody QueryTrainTimetableReqVo reqVo) {
		List<QueryTrainTimetableRespVo> resultList = queryTrainTimetableService.queryTrainTimetable(reqVo);
		
		return ApiResponse.ok(resultList);
	}
	
}
