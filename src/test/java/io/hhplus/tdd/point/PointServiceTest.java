package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PointServiceTest {

    private PointRepositoryImpl pointRepositoryImpl;
    private PointServiceSpy pointServiceSpy;

    @BeforeEach
    void setUp(){
        UserPointTable userPointTable = new UserPointTable();
        pointRepositoryImpl = new PointRepositoryImpl(userPointTable);
        pointServiceSpy = new PointServiceSpy(pointRepositoryImpl);
    }

    @Test
    @DisplayName("포인트를 충전한다 : Spy 활용")
    void charge()  {
        //given : 포인트 충전 대상과 금액 설정
        long id = 1L;
        long amount = 1234L;

        //when : 충전 전과 후의 금액 차를 모니터링 하기 위해 스파이 객체의 충전 메서드 호출
        pointServiceSpy.chargeAmount(id, amount);

        //then : 충전 금액이 올바르게 반영되었는지 검증
        assertEquals (amount, pointServiceSpy.getAmountAfterCharge() - pointServiceSpy.getAmountBeforeCharge());
    }
}
