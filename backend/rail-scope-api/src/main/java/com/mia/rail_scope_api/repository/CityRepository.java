package com.mia.rail_scope_api.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mia.rail_scope_api.mapper.CityMapper;
import com.mia.rail_scope_api.responseVo.QueryStationRespVo;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CityRepository {

	private final CityMapper cityMapper;
	
	//查詢所有縣市車站
	public List<QueryStationRespVo> queryStationAll(){
		List<QueryStationRespVo> resultList = cityMapper.queryStationAll();
		if(resultList.isEmpty()) {
			throw new IllegalStateException("queryStationAll查詢失敗");
		}
		return resultList;
	}
}
