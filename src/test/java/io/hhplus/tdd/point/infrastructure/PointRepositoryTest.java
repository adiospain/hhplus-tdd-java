package io.hhplus.tdd.point.infrastructure;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.dto.UserPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PointRepositoryTest {

    private PointRepository pointRepository;
    private UserPointTable userPointTable;

    @BeforeEach
    void setUp(){
        userPointTable = new UserPointTable();
        this.pointRepository = new PointRepositoryImpl(userPointTable);
    }

    @Test
    @DisplayName("데이터베이스에 이미 존재하는 특정 사용자와 포인트 리턴")
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



    void insert() {

    }
}
