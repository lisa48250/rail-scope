package com.mia.rail_scope_api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mia.rail_scope_api.requestVo.QueryTrainTimetableReqVo;
import com.mia.rail_scope_api.responseVo.QueryTrainTimetableRespVo;

@Mapper
public interface QueryTrainTimetableMapper {
	
	
	
	
	/**
	 * 查詢火車時刻表
	 * @param direction : 0=北上、1=南下
	 * @param tst.stationId : 起點車站
	 * @param tst2.stationId : 終點車站
	 * @return
	 */
	@Select("<script>"
			+ "select  "
			+ "tst.trainNo as trainNo "
			+ ",tt.typeName as typeName "
			+ ",s1.stationName as stationNameStart "
			+ ",tst.arrivalTime AS arrivalTimeStart "
			+ ",DATEADD(MINUTE, "
			+ "    DATEDIFF(MINUTE, tst.arrivalTime, tst2.departureTime), "
			+ "    CAST('00:00' AS TIME) "
			+ ") AS timeDiff"
			+ ",s2.stationName as stationNameEnd "
			+ ",tst2.departureTime AS arrivalTimeEnd "
			+ "from trainStopTime tst  "
			+ "join station s1 on tst.lineOrder = s1.lineOrder "
			+ "join trainStopTime tst2 on tst.trainNo = tst2.trainNo "
			+ "join station s2 on tst2.lineOrder = s2.lineOrder "
			+ "join train t on tst.trainNo = t.trainNo "
			+ "join trainType tt on t.trainTypeId = tt.trainTypeId  "
			+ "join trainRun tr on tr.trainNo = t.trainNo "
			+ "where  t.direction = #{direction} "
			+ "and tst.lineOrder = #{stationStart} "
			+ "and tst2.lineOrder = #{stationEnd} "
			+ "and tr.serviceDate = #{date} "
			+ "<if test=\" vehicleType != null and vehicleType == '1'.toString() \"> "
			+ " and tt.trainTypeId not in(5,6) "
			+ "</if>"
			+ "<if test=\" vehicleType != null and vehicleType == '2'.toString() \"> "
			+ " and tt.trainTypeId in(5,6) "
			+ "</if>"
			+ "order by tst.arrivalTime asc"
			+  "</script>")
	List<QueryTrainTimetableRespVo> queryTrainTimetable (QueryTrainTimetableReqVo reqVo);

}
