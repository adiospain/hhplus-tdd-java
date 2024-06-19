package io.hhplus.tdd.point.infrastructure;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.dto.UserPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PointRepositoryUpdateTest {

    private PointRepository pointRepository;
    private UserPointTable userPointTable;

    @BeforeEach
    void setUp(){
        userPointTable = new UserPointTable();
        this.pointRepository = new PointRepositoryImpl(userPointTable);
    }

    @Test
    @DisplayName("포인트 충전하고 업데이트")
    void updateAfterCharge() {
        // given : 데이터베이스에 이미 존재하는 특정 사용자
        long id = 3L;
        long initialAmount = 3012L; // 초기 포인트 금액
        UserPoint userPoint = userPointTable.insertOrUpdate(id, initialAmount);
        long additionalAmount = 6500L; // 추가될 금액

        // when : 사용자 포인트 업데이트
        UserPoint updatedUserPoint = pointRepository.update(userPoint.id(), userPoint.point() + additionalAmount);

        // then : 반환된 결과가 예상 값과 일치하는지 검사
        assertNotEquals(userPoint, updatedUserPoint);
        assertEquals(userPoint.id(), updatedUserPoint.id());
        assertEquals(userPoint.point() + additionalAmount, updatedUserPoint.point());
        assertTrue(userPoint.updateMillis() < updatedUserPoint.updateMillis());
    }

    @Test
    @DisplayName("포인트 사용하고 업데이트")
    void updateAfterUse() {
        // given : 데이터베이스에 이미 존재하는 특정 사용자
        long id = 3L;
        long initialAmount = 3012L; // 초기 포인트 금액
        UserPoint userPoint = userPointTable.insertOrUpdate(id, initialAmount);
        long minusAmount = 6500L; // 차감될 금액

        // when : 사용자 포인트 업데이트
        UserPoint updatedUserPoint = pointRepository.update(userPoint.id(), userPoint.point() - minusAmount);

        // then : 반환된 결과가 예상 값과 일치하는지 검사
        assertNotEquals(userPoint, updatedUserPoint);
        assertEquals(userPoint.id(), updatedUserPoint.id());
        assertEquals(userPoint.point() - minusAmount, updatedUserPoint.point());
        assertTrue(userPoint.updateMillis() < updatedUserPoint.updateMillis());
    }
}