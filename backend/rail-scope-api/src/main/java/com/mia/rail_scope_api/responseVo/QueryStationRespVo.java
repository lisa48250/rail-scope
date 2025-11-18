package com.mia.rail_scope_api.responseVo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryStationRespVo {
	private String cityName;
	private String stationName;
	private int cityId;
	private int stationId;
}
