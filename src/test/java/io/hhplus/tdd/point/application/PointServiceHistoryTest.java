package io.hhplus.tdd.point.application;

import io.hhplus.tdd.point.dto.PointHistory;
import io.hhplus.tdd.point.infrastructure.PointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    @DisplayName("포인트 내역이 없는 사용자의 내역을 조회 한다")
    void history() throws Exception {
        //given
        long userId = 3L;
        long time = System.currentTimeMillis();
        when(pointRepositoryMock.findHistoryById(userId)).thenReturn(List.of());

        //when
        List<PointHistory> histories = pointServiceMock.history(userId);
        verify(pointRepositoryMock).findHistoryById(userId);
        //then
        assertEquals(List.of(), histories);
        assertEquals(histories.size(), 0);
    }
}