package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.springframework.stereotype.Repository;

@Repository
public class PointRepositoryImpl implements PointRepository{
    UserPointTable userPointTable = new UserPointTable();


    public UserPoint chargeAmount(long id, long amount) {
        UserPoint userPoint = userPointTable.selectById(id);
        return userPointTable.insertOrUpdate(id, userPoint.point()+amount);
    }

    @Override
    public UserPoint findById(Long id) {
        return userPointTable.selectById(id);
    }

    @Override
    public UserPoint update(Long id, Long amount) {
        return userPointTable.selectById(id);
    }
}
