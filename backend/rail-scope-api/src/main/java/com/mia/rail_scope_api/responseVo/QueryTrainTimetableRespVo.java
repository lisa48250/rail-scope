package com.mia.rail_scope_api.responseVo;

import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryTrainTimetableRespVo {

	private String trainNo;				//車次編號
	
	private String typeName;			//車種
	
	private String stationNameStart;	//起點車站
	
	private LocalTime arrivalTimeStart;	//抵達時間(起點車站)
	
	private String stationNameEnd;		//終點車站
	
	private LocalTime arrivalTimeEnd;	//抵達時間(終點車站)
	
	private LocalTime timeDiff;			//時間差
	
}
