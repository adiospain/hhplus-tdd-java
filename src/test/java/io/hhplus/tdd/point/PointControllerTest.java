package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.dto.ChargeAmountAPIRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;

public class PointControllerTest {

    private PointRepositoryImpl pointRepositoryImpl;
    private PointServiceSpy pointServiceSpy;

    @Captor
    private ArgumentCaptor<ChargeAmountAPIRequest> chargePointCaptor;

    @BeforeEach
    void setUp(){
        pointRepositoryImpl = new PointRepositoryImpl();
        pointServiceSpy = new PointServiceSpy(pointRepositoryImpl);
    }

    @Test
    @DisplayName("포인트를 충전한다:Spy")
    void charge() throws Exception {
        long id = 1L;
        long amount = 1000L;
        pointServiceSpy.chargeAmount(id, amount);
    }
}
