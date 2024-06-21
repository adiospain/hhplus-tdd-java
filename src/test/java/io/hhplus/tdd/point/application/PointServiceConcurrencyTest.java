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

    @Test
    void usePointConcurrency() throws InterruptedException {
        //given
        long userId = 1L;
        long point = 3000L;
        long useAmount = 300L;
        pointRepository.insert(userId, point);

        //when
        int trial = 5;
        ExecutorService executorService =Executors.newFixedThreadPool(trial);
        CountDownLatch latch = new CountDownLatch(trial);

        for (int i = 0; i < trial; i++) {
            executorService.submit(() -> {
                try {
                    pointService.use(userId, useAmount);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        // then
        UserPoint userPoint = pointRepository.findById(userId);
        assertEquals(point - useAmount * trial, userPoint.point());
    }

    @Test
    void TwoPathConcurrently() throws InterruptedException {
        //given
        pointService.charge(2L, 3000L);

        //when
        CompletableFuture.allOf(
                CompletableFuture.runAsync(() ->{
                    pointService.use(2L, 3000L);
                }),
                CompletableFuture.runAsync(()->{
                    pointService.charge(2L, 4000L);
                }),
                CompletableFuture.runAsync(()->{
                    pointService.use(2L,4000L);
                }),
                CompletableFuture.runAsync(()->{
                    pointService.charge(2L,7000L);
                }),
                CompletableFuture.runAsync(()->{
                    pointService.use(2L,1000L);
                })
        ).join();

        //then
        UserPoint userPoint = pointService.point(2L);
        assertEquals(userPoint.point(), 3000-3000+4000-4000+7000-1000);
    }

}

