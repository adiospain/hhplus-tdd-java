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

/**
 * DB에 저장 되지 않은 사용자의 포인트를 조회할 경우,
 * Default한 UserPoint가 리턴 된다.
 */
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
    @DisplayName("성공")
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

    @Test
    @DisplayName("사용자 id가 음수인 경우, 실패")
    void pointNegativeID_Fail(){
        //given : 포인트 소지량
        long id = -2L;

        Exception pointException = assertThrows(PointException.class, ()->{
            pointServiceMock.point(id);
        });
    }
    @Test
    @DisplayName("사용자 id가 0인 경우, 실패")
    void pointZeroID_Fail(){
        //given : 포인트 소지량
        long id = 0L;

        Exception pointException = assertThrows(PointException.class, ()->{
            pointServiceMock.point(id);
        });
    }
}
