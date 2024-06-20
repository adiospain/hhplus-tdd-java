package io.hhplus.tdd.point.application;

import io.hhplus.tdd.point.dto.UserPoint;
import io.hhplus.tdd.point.infrastructure.PointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DisplayName("Service 포인트 사용")
public class PointServiceUseTest {

    private PointRepository pointRepositoryMock;
    private PointService pointServiceMock;

    @BeforeEach
    void setUp(){
        pointRepositoryMock = Mockito.mock(PointRepository.class);
        pointServiceMock = new PointServiceImpl(pointRepositoryMock);
    }

    @Test
    @DisplayName("포인트를 사용한다 : 2000 소지 중 1234 사용 시, 766 포인트 반환")
    void use(){
        //given : 포인트 소지량
        long id = 2L;
        long currentPoint = 2000L;

        //given : 포인트 사용량
        long minusAmount = 1234L;

        //given : Repository 리턴값 설정
        UserPoint userPoint = new UserPoint(id, currentPoint, System.currentTimeMillis());
        when(pointRepositoryMock.findById(id)).thenReturn(userPoint);
        when(pointRepositoryMock.update(id, currentPoint - minusAmount)).thenReturn(new UserPoint(id, currentPoint - minusAmount, System.currentTimeMillis()));

        //when : 포인트 사용 시뮬레이션
        userPoint = pointServiceMock.use(id, minusAmount);

        //then : 사용 금액이 올바르게 반영되었는지 검증
        assertEquals(id, userPoint.id());
        assertEquals(currentPoint - minusAmount, userPoint.point());
    }
}
