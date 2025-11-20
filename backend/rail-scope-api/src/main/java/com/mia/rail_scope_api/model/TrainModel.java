package com.mia.rail_scope_api.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TrainModel {

	private String trainNo;				//車次號碼
	
	private int trainTypeId;			//車種
	
	private int direction;				//0=北上、1=南下
	
	private String notes;				//備註說明
	
	private LocalDateTime updatedAt;	//更新時間
	
}
