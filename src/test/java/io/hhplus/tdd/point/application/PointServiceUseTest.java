package io.hhplus.tdd.point.application;

import io.hhplus.tdd.exception.CustomException;
import io.hhplus.tdd.point.dto.UserPoint;
import io.hhplus.tdd.point.exception.PointException;
import io.hhplus.tdd.point.infrastructure.PointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
    @DisplayName("잔고가 충분한 경우, 성공")
    void useSufficientPoint_Suceed(){
        //given : 포인트 소지량
        long id = 2L;
        long currentPoint = 2000L;

        //given : 포인트 사용량
        long minusAmount = 1200L;

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

    @Test
    @DisplayName("잔고가 부족한 경우, 실패")
    void useInsufficientPoint_Fail(){
        //given : 포인트 소지량
        long id = 2L;
        long currentPoint = 1000L;

        //given : 포인트 사용량
        long minusAmount = 1200L;

        //given : Repository 리턴값 설정
        UserPoint userPoint = new UserPoint(id, currentPoint, System.currentTimeMillis());
        when(pointRepositoryMock.findById(id)).thenReturn(userPoint);

        //when: 포인트 사용 시뮬레이션
        //then: 예외 발생 여부 검증
        Exception pointException = assertThrows(PointException.class, ()->{
            pointServiceMock.use(id, minusAmount);
        });
    }

    @Test
    @DisplayName("사용 포인트가 음수인 경우, 실패")
    void useNegativeValue_Fail(){
        //given : 포인트 소지량
        long id = 2L;
        long currentPoint = 1000L;

        //given : 포인트 사용량
        long minusAmount = -1200L;

        //given : Repository 리턴값 설정
        UserPoint userPoint = new UserPoint(id, currentPoint, System.currentTimeMillis());
        when(pointRepositoryMock.findById(id)).thenReturn(userPoint);

        //when: 포인트 사용 시뮬레이션
        //then: 예외 발생 여부 검증
        Exception pointException = assertThrows(PointException.class, ()->{
            pointServiceMock.use(id, minusAmount);
        });
    }

    @Test
    @DisplayName("사용 포인트가 0인 경우, 실패")
    void useZeroValue_Fail(){
        //given : 포인트 소지량
        long id = 2L;
        long currentPoint = 1000L;

        //given : 포인트 사용량
        long minusAmount = 0L;

        //given : Repository 리턴값 설정
        UserPoint userPoint = new UserPoint(id, currentPoint, System.currentTimeMillis());
        when(pointRepositoryMock.findById(id)).thenReturn(userPoint);

        //when: 포인트 사용 시뮬레이션
        //then: 예외 발생 여부 검증
        Exception pointException = assertThrows(PointException.class, ()->{
            pointServiceMock.use(id, minusAmount);
        });
    }

}
