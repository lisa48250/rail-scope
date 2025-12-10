package com.mia.rail_scope_api.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainRunModel {

	private String trainNo;
	
	private LocalDate serviceDate;
	
	private int isCancelled;
	
	private LocalDateTime updateTime;
}
