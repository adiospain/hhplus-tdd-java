package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


public class PointServiceTest {

    private PointRepository pointRepository;
    private PointServiceSpy pointServiceSpy;

    private PointRepository pointRepositoryMock;
    private PointService pointServiceMock;

    @BeforeEach
    void setUp(){
        //MockitoAnnotations.openMocks(this);
        UserPointTable userPointTable = new UserPointTable();
        pointRepository = new PointRepositoryImpl (userPointTable);
        pointServiceSpy = new PointServiceSpy(pointRepository);

        pointRepositoryMock = Mockito.mock(PointRepositoryImpl.class);
        pointServiceMock = new PointServiceImpl(pointRepositoryMock);
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

        //then : 충전 금액이 올바르게 반영되었는지 검증
        assertEquals(id, userPoint.id());
        assertEquals(2000L-1234L, userPoint.point());
    }
}
