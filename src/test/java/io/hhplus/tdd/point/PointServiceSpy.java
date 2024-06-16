package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PointServiceSpy implements PointService {
    private static final Logger log = LoggerFactory.getLogger(PointServiceSpy.class);
    private final PointRepository pointRepository;


    public PointServiceSpy(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @Override
    public UserPoint chargeAmount(long id, long amount) {

        UserPoint userPoint = pointRepository.findById(id);
        log.info("Before charge | id : {}, amount : {}", userPoint.id(), userPoint.point());
        userPoint = pointRepository.update(id, userPoint.point()+amount);
        log.info("After charge | id : {}, amount : {}", userPoint.id(), userPoint.point());
        return userPoint;
    }
}
