package io.hhplus.tdd.point.application;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.dto.UserPoint;
import io.hhplus.tdd.point.infrastructure.PointRepository;
import io.hhplus.tdd.point.infrastructure.PointRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Service 포인트 동시성 테스트")
public class PointServiceConcurrencyTest {


    private PointRepository pointRepository;
    private PointService pointService;

    @BeforeEach
    void setUp() {
        pointRepository = new PointRepositoryImpl(new UserPointTable(), new PointHistoryTable());
        pointService = new PointServiceImpl(pointRepository);
    }


    @Test
    void chargePointConcurrency() throws InterruptedException {
        //given
        long userId = 1L;
        long chargeAmount = 300L;

        //when
        int trial =5;
        ExecutorService executorService =Executors.newFixedThreadPool(trial);
        CountDownLatch latch = new CountDownLatch(trial);

        for (int i = 0; i < trial; i++) {
            executorService.submit(() -> {
                try {
                    pointService.charge(userId, chargeAmount);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        // then
        UserPoint userPoint = pointRepository.findById(userId);
        assertEquals(chargeAmount * trial+1, userPoint.point());
    }
}

