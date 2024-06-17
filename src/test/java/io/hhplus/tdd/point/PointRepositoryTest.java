package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PointRepositoryTest {


    private PointRepository pointRepository;
    private UserPointTable userPointTable;

    @BeforeEach
    void setUp(){
        userPointTable = new UserPointTable();
        this.pointRepository = new PointRepositoryImpl (userPointTable);
    }

    @Test
    void findById() {
        //given : 데이터베이스에 이미 존재하는 특정 사용자
        long id = 3L;
        long amount = 2000L;
        UserPoint userPoint = userPointTable.insertOrUpdate(id, amount);

        //when : findById 메서드 호출
        UserPoint result = pointRepository.findById(id);

        //then : 반환된 결과가 예상 값과 일치하는지 검사
        assertEquals(userPoint.id(), result.id());
        assertEquals(userPoint.point(), result.point());
        assertEquals(userPoint.updateMillis(), result.updateMillis());
    }

    @Test
    void update() {
        // given : 데이터베이스에 이미 존재하는 특정 사용자
        long id = 3L;
        long initialAmount = 3012L; // 초기 포인트 금액
        UserPoint userPoint = userPointTable.insertOrUpdate(id, initialAmount);
        long additionalAmount = 6500L; // 추가될 금액

        // when : 사용자 포인트 업데이트
        UserPoint updatedUserPoint = pointRepository.update(userPoint.id(), userPoint.point() + additionalAmount);

        // then : 반환된 결과가 예상 값과 일치하는지 검사
        assertEquals(userPoint.id(), updatedUserPoint.id());
        assertEquals(userPoint.point() + additionalAmount, updatedUserPoint.point());
        assertTrue(userPoint.updateMillis() < updatedUserPoint.updateMillis());
    }
    void insert() {

    }
}