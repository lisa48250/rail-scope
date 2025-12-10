package com.mia.rail_scope_api.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import com.mia.rail_scope_api.model.TrainRunModel;

@Mapper
public interface TidyTrainRunMapper {

	@Select("select count(distinct serviceDate )  "
			+ "from trainRun tr  "
			+ "where serviceDate between #{today} and #{after30Days} ")
	int queryTrainRunDay(LocalDate today,LocalDate after30Days);
	
	@Select("select trainNo from train")
	List<String> queryTrainAllTrainNo();
	
	
	@Insert({
	    "<script>",
	    "insert into trainRun (trainNo,serviceDate,updatedAt) VALUES",
	    "<foreach item='record' collection='trainRunList' separator=','>",
	    " (",
	    "   #{record.trainNo},",
	    "   #{record.serviceDate},",
	    "   GETDATE()",
	    " )",
	    "</foreach>",
	    "</script>"
	})
	int insertTrainRunNewDate(@Param("trainRunList") List<TrainRunModel> trainRunList);
	
	@Delete("delete from trainRun where serviceDate < #{today} ")
	int deleteTrainRunOld(LocalDate today);

}
