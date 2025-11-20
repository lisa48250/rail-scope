package com.mia.rail_scope_api.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.mia.rail_scope_api.model.TrainModel;

@Mapper
public interface InsertTestDataMapper {
	
	//查詢最新車次
	@Select("select top 1 t.trainNo  from train t  "
			+ " where t.trainTypeId = #{trainTypeId} and t.direction = #{direction} "
			+ " and exists (select * from trainStopTime tst where t.trainNo = tst.trainNo )")
	String queryNewestTrainData(TrainModel model);
	
	//新增train測試資料
	@Insert("insert into RailScope.dbo.train (trainNo, trainTypeId,direction,updatedAt) values  "
			+ "(#{trainNo},#{trainTypeId},#{direction},GETDATE())")
	void insertTrainTestData(TrainModel model);
	
	//新增trainStopTime測試資料
	@Insert("INSERT INTO trainStopTime (trainNo, stationId, stopSequence, arrivalTime, departureTime,updatedAt) "
			+ "select  "
			+ "#{newTrainNo}  "
			+ ",stationId  "
			+ ",stopSequence  "
			+ ",DATEADD(MINUTE, 35, arrivalTime) AS NEWarrivalTime "
			+ ",DATEADD(MINUTE, 35, departureTime) AS NEWdepartureTime "
			+ ",GETDATE() "
			+ "from trainStopTime tst  "
			+ "where trainNo = #{oldTrainNo} "
			+ "order by stopSequence   ")
	int insertTrainStopTimeTestData(String newTrainNo, String oldTrainNo);
}
