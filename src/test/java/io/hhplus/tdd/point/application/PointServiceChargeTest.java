package io.hhplus.tdd.point.application;

import io.hhplus.tdd.point.dto.UserPoint;
import io.hhplus.tdd.point.exception.PointException;
import io.hhplus.tdd.point.infrastructure.PointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@DisplayName("Service 포인트 충전")
public class PointServiceChargeTest {

    private PointRepository pointRepositoryMock;
    private PointService pointServiceMock;

    @BeforeEach
    void setUp(){
        pointRepositoryMock = Mockito.mock(PointRepository.class);
        pointServiceMock = new PointServiceImpl(pointRepositoryMock);
    }

    @Test
    @DisplayName("성공")
    void charge()  {
        //given : 포인트 소지량
        long id = 2L;
        long currentPoint = 1000L;

        //given : 포인트 충전량
        long chargeAmount = 1200L;

        //given : Repository 리턴값 설정
        UserPoint userPoint = new UserPoint(id, currentPoint, System.currentTimeMillis());
        when(pointRepositoryMock.findById(id)).thenReturn(userPoint);
        when(pointRepositoryMock.update(id, currentPoint + chargeAmount)).thenReturn(new UserPoint(id, currentPoint + chargeAmount, System.currentTimeMillis()));

        //when : 포인트 충전 시뮬레이션
        userPoint = pointServiceMock.charge(id, chargeAmount);

        //then : 충전 금액이 올바르게 반영되었는지 검증
        assertEquals(id, userPoint.id());
        assertEquals(currentPoint + chargeAmount, userPoint.point());
    }

    @Test
    @DisplayName("충전 포인트가 음수인 경우, 실패")
    void chargeNegativePoint_Fail()  {
        //given : 포인트 소지량
        long id = -2L;

        //given : 포인트 충전량
        long chargeAmount = -1200L;

        //when: 포인트 사용 시뮬레이션
        //then: 예외 발생 여부 검증
        Exception pointException = assertThrows(PointException.class, () -> {
            pointServiceMock.charge(id, chargeAmount);
        });
    }

    @Test
    @DisplayName("충전 포인트가 0인 경우, 실패")
    void chargeZeroPoint_Fail()  {
        //given : 포인트 소지량
        long id = 2L;
        long currentPoint = 1000L;

        //given : 포인트 충전량
        long chargeAmount = 0L;

        //when: 포인트 사용 시뮬레이션
        //then: 예외 발생 여부 검증
        Exception pointException = assertThrows(PointException.class, () -> {
            pointServiceMock.use(id, chargeAmount);
        });
    }

    @Test
    @DisplayName("포인트 충전 후 포인트가 음수가 될 경우 : 오버플로우, 실패")
    void chargeExceedMaxValue_Fail()  {
        //given : 포인트 소지량
        long id = 2L;
        long currentPoint = Long.MAX_VALUE;

        //given : 포인트 충전량
        long chargeAmount = 300L;

        //given : Repository 리턴값 설정
        UserPoint userPoint = new UserPoint(id, currentPoint, System.currentTimeMillis());
        when(pointRepositoryMock.findById(id)).thenReturn(userPoint);

        //when: 포인트 사용 시뮬레이션
        //then: 예외 발생 여부 검증
        Exception pointException = assertThrows(PointException.class, () -> {
            pointServiceMock.charge(id, chargeAmount);
        });
    }
}
