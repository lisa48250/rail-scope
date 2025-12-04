package com.mia.rail_scope_api.requestVo;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryTrainTimetableReqVo {

	private String stationStart;		//起點車站
	
	private String stationEnd;			//終點車站
	
	private String direction;			//0=北上、1=南下
	
	private String timeType;			//出發時間:0,抵達時間:1
	
	private String vehicleType;			//車輛類型(全部:0,對號:1非對號:2)
	
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate date;				//查詢日期
    
    private String routeType;			//直達:0轉乘:1
}
