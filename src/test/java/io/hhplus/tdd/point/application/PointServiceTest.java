package io.hhplus.tdd.point.application;

import io.hhplus.tdd.point.dto.UserPoint;
import io.hhplus.tdd.point.infrastructure.PointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DisplayName("Service 포인트 조회")
public class PointServiceTest {

    private PointRepository pointRepositoryMock;
    private PointService pointServiceMock;

    @BeforeEach
    void setUp(){
        pointRepositoryMock = Mockito.mock(PointRepository.class);
        pointServiceMock = new PointServiceImpl(pointRepositoryMock);
    }
    @Test
    @DisplayName("포인트를 조회한다 : Mock 활용")
    void point() {
        //given : 사용자와 현재 포인트
        long id = 2L;
        long currentPoint = 2000L;

        //given : Repository 리턴값 설정
        UserPoint userPoint = new UserPoint(id, currentPoint, System.currentTimeMillis());
        when(pointRepositoryMock.findById(id)).thenReturn(userPoint);

        //when : 포인트 사용 시뮬레이션
        userPoint = pointServiceMock.point(id);

        //then : 금액이 올바르게 리턴되었는지 검증
        assertEquals(id, userPoint.id());
        assertEquals(currentPoint, userPoint.point());
    }
}
