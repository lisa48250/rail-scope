package com.mia.rail_scope_api.model;

import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainStopTimeModel {
	
	private String trainNo;				//車次編號
	
    private int stationId;				//停靠站
    
    private int stopSequence;			//停靠順序
    
    private LocalTime departureTime;	//到站時間
    
    private LocalTime arrivalTime;		//離站時間
    
    private LocalDateTime updatedAt;	//更新時間
}
