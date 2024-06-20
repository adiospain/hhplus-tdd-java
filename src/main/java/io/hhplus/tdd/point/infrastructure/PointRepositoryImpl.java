package io.hhplus.tdd.point.infrastructure;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.dto.PointHistory;
import io.hhplus.tdd.point.dto.UserPoint;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class PointRepositoryImpl implements PointRepository {
    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    @Override
    public UserPoint findById(long id) {
        return userPointTable.selectById(id);
    }

    @Override
    public UserPoint update(long id, long amount) {
        return userPointTable.insertOrUpdate(id, amount);
    }

    @Override
    public UserPoint insert(long id, long amount) {
        return userPointTable.insertOrUpdate(id, amount);
    }

    @Override
    public List<PointHistory> findHistoryById(long id) {
        return pointHistoryTable.selectAllByUserId(id);
    }
}
