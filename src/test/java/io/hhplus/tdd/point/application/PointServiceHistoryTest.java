package io.hhplus.tdd.point.application;

import io.hhplus.tdd.point.domain.TransactionType;
import io.hhplus.tdd.point.dto.PointHistory;
import io.hhplus.tdd.point.exception.PointException;
import io.hhplus.tdd.point.infrastructure.PointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Service 포인트 내역 조회")
public class PointServiceHistoryTest {

    private PointRepository pointRepositoryMock;
    private PointService pointServiceMock;

    @BeforeEach
    void setUp(){
        pointRepositoryMock = Mockito.mock(PointRepository.class);
        pointServiceMock = new PointServiceImpl(pointRepositoryMock);
    }

    @Test
    @DisplayName("포인트 내역이 있는 사용자의 경우, 내역 리스트를 리턴")
    void retrievePointHistory() throws Exception {
        //given
        long userId = 3L;
        long amount = 1000L;

        long time = System.currentTimeMillis();
        List<PointHistory> fakeHistories = List.of(
                new PointHistory(1L,3L, 2000L, TransactionType.CHARGE, System.currentTimeMillis()),
                new PointHistory(2L, 3L, 500L, TransactionType.USE, System.currentTimeMillis())

        );

        when(pointRepositoryMock.findHistoryByUserId(userId)).thenReturn(fakeHistories);

        //when
        List<PointHistory> histories = pointServiceMock.history(userId);
        verify(pointRepositoryMock).findHistoryByUserId(userId);
        //then
        assertEquals(histories, fakeHistories);
        assertEquals(histories.size(), fakeHistories.size());
    }

    @Test
    @DisplayName("포인트 내역이 없는 사용자의 경우, 비어있는 내역 리스트를 리턴")
    void retrievePointHistoryButEmpty() throws Exception {
        //given
        long userId = 3L;
        long time = System.currentTimeMillis();
        when(pointRepositoryMock.findHistoryByUserId(userId)).thenReturn(List.of());

        //when
        List<PointHistory> histories = pointServiceMock.history(userId);
        verify(pointRepositoryMock).findHistoryByUserId(userId);
        //then
        assertEquals(List.of(), histories);
        assertEquals(histories.size(), 0);
    }

    @Test
    @DisplayName("사용자 id가 음수인 경우, 실패")
    void historyNegativeID_Fail(){
        //given : 포인트 소지량
        long id = -2L;

        Exception pointException = assertThrows(PointException.class, ()->{
            pointServiceMock.history(id);
        });
    }

    @Test
    @DisplayName("사용자 id가 0인 경우, 실패")
    void historyZeroID_Fail(){
        //given : 포인트 소지량
        long id = 0L;

        Exception pointException = assertThrows(PointException.class, ()->{
            pointServiceMock.history(id);
        });
    }
}