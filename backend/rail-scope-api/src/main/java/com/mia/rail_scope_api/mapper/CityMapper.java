package com.mia.rail_scope_api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.mia.rail_scope_api.responseVo.QueryStationRespVo;

@Mapper
public interface CityMapper {

	@Select("select  "
			+ "c.cityName "
			+ ",s.stationName "
			+ ",c.cityId "
			+ ",s.stationId "
			+ "from city c join station s on c.cityId = s.cityId "
			+ "order by c.cityId , s.stationId asc")
	List<QueryStationRespVo> queryStationAll ();
}
