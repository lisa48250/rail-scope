package com.mia.rail_scope_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mia.rail_scope_api.repository.CityRepository;
import com.mia.rail_scope_api.responseVo.QueryStationRespVo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StationInformationService {
	
	private final CityRepository cityRepository;

	//查詢所有縣市車站
	public List<QueryStationRespVo> queryStationAll(){
		return cityRepository.queryStationAll();
	}
}
