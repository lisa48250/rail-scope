package com.mia.rail_scope_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mia.rail_scope_api.repository.QueryTrainTimetableRrepository;
import com.mia.rail_scope_api.requestVo.QueryTrainTimetableReqVo;
import com.mia.rail_scope_api.responseVo.QueryTrainTimetableRespVo;

@ExtendWith(MockitoExtension.class)
class QueryTrainTimetableServiceTest {

    @Mock
    private QueryTrainTimetableRrepository repository;

    @InjectMocks
    private QueryTrainTimetableService service;

    @Test
    void testQueryTrainTimetable() {
        // arrange：準備測試資料
        QueryTrainTimetableReqVo req = new QueryTrainTimetableReqVo();
        req.setStationStart("9");
        req.setStationEnd("19");
        req.setDirection("1");
        // 回傳資料設定
        QueryTrainTimetableRespVo vo = new QueryTrainTimetableRespVo();
        vo.setTrainNo("1234");

        //當呼叫repository.queryTrainTimetable()時回應vo
        when(repository.queryTrainTimetable(req)).thenReturn(List.of(vo));

        // act：呼叫 service
        List<QueryTrainTimetableRespVo> result = service.queryTrainTimetable(req);

        // assert：驗證結果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1234", result.get(0).getTrainNo());
    }
}
