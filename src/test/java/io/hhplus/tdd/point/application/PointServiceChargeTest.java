package io.hhplus.tdd.point.application;

import io.hhplus.tdd.point.dto.UserPoint;
import io.hhplus.tdd.point.infrastructure.PointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    @DisplayName("포인트를 충전한다 : 성공")
    void charge()  {
        //given : 포인트 소지량
        long id = 2L;
        long currentPoint = 1000L;

        //given : 포인트 충전량
        long addAmount = 1200L;

        //given : Repository 리턴값 설정
        UserPoint userPoint = new UserPoint(id, currentPoint, System.currentTimeMillis());
        when(pointRepositoryMock.findById(id)).thenReturn(userPoint);
        when(pointRepositoryMock.update(id, currentPoint + addAmount)).thenReturn(new UserPoint(id, currentPoint + addAmount, System.currentTimeMillis()));

        //when : 포인트 충전 시뮬레이션
        userPoint = pointServiceMock.charge(id, addAmount);

        //then : 충전 금액이 올바르게 반영되었는지 검증
        assertEquals(id, userPoint.id());
        assertEquals(currentPoint + addAmount, userPoint.point());
    }
}
