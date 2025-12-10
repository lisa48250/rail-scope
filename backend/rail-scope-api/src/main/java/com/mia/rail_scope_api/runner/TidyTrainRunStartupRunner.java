package com.mia.rail_scope_api.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mia.rail_scope_api.service.TidyTrainRunService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TidyTrainRunStartupRunner implements ApplicationRunner {

    private final TidyTrainRunService tidyTrainRunService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(ApplicationArguments args) {
        log.info("應用啟動完成 → 立即執行 tidyTrainRun()");
        tidyTrainRunService.tidyTrainRun();
    }
}

