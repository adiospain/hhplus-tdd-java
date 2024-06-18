package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PointServiceTest {

    private PointRepository pointRepository;
    private PointServiceSpy pointServiceSpy;
    private PointService pointService;

    @BeforeEach
    void setUp(){
        UserPointTable userPointTable = new UserPointTable();
        pointRepository = new PointRepositoryImpl (userPointTable);
        pointServiceSpy = new PointServiceSpy(pointRepository);
        pointService = new PointServiceImpl(pointRepository);
    }

    @Test
    @DisplayName("포인트를 충전한다 : Spy 활용")
    void charge()  {
        //given : 포인트 충전 대상과 금액 설정
        long id = 1L;
        long amount = 1234L;

        //when : 충전 전과 후의 금액 차를 모니터링 하기 위해 스파이 객체의 충전 메서드 호출
        pointServiceSpy.charge(id, amount);

        //then : 충전 금액이 올바르게 반영되었는지 검증
        assertEquals(true, pointServiceSpy.isTargetTheSame());
        assertEquals (amount, pointServiceSpy.getAmountAfterCharge() - pointServiceSpy.getAmountBeforeCharge());
        int a = 0;
    }

    @Test
    @DisplayName("포인트를 사용한다 : 2000 충전 후 1234 사용 시, 766 포인트 반환")
    void use(){
        //given
        long id = 2L;
        long amount = 2000L;
        pointService.charge(id, amount);

        //when
        amount = 1234L;
        UserPoint userPoint = pointServiceSpy.use(id, amount);

        assertEquals(true, pointServiceSpy.isTargetTheSame());
        assertEquals(2000L-1234L, pointServiceSpy.getAmountAfterCharge());
        assertEquals(pointServiceSpy.getAmountBeforeCharge(), pointServiceSpy.getAmountAfterCharge() + amount);
    }
}
