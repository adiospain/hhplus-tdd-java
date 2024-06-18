package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class PointServiceSpy implements PointService {
    private final PointRepository pointRepository;

    private final ThreadLocal<Long> idBefore = new ThreadLocal<>();
    private final ThreadLocal<Long> idAfter = new ThreadLocal<>();
    private final ThreadLocal<Long> amountBefore = new ThreadLocal<>();
    private final ThreadLocal<Long> amountAfter = new ThreadLocal<>();

    public PointServiceSpy(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @Override
    public UserPoint charge(long id, long amount) {
        UserPoint userPoint = pointRepository.findById(id);
        idBefore.set(userPoint.id());
        amountBefore.set(userPoint.point());
        userPoint = pointRepository.update(id, userPoint.point()+amount);
        idAfter.set(userPoint.id());
        amountAfter.set(userPoint.point());

        return userPoint;
    }

    @Override
    public UserPoint use(long id, long amount) {
        UserPoint userPoint = pointRepository.findById(id);
        idBefore.set(userPoint.id());
        amountBefore.set(userPoint.point());
        userPoint = pointRepository.update(id, userPoint.point() - amount);
        idAfter.set(userPoint.id());
        amountAfter.set(userPoint.point());
        return userPoint;
    }

    public boolean isTargetTheSame(){
        return Objects.equals(idBefore.get(), idAfter.get());
    }

    public long getAmountBeforeCharge() {
        return amountBefore.get();
    }

    public long getAmountAfterCharge() {
        return amountAfter.get();
    }
}
