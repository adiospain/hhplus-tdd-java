package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PointServiceSpy implements PointService {
    private final PointRepository pointRepository;

    private final ThreadLocal<Long> amountBeforeCharge = new ThreadLocal<>();
    private final ThreadLocal<Long> amountAfterCharge = new ThreadLocal<>();

    public PointServiceSpy(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @Override
    public UserPoint charge(long id, long amount) {
        UserPoint userPoint = pointRepository.findById(id);

        amountBeforeCharge.set(userPoint.point());
        userPoint = pointRepository.update(id, userPoint.point()+amount);

        amountAfterCharge.set(userPoint.point());

        return userPoint;
    }
    public long getAmountBeforeCharge() {
        return amountBeforeCharge.get();
    }

    public long getAmountAfterCharge() {
        return amountAfterCharge.get();
    }
}
