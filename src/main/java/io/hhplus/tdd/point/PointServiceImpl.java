package io.hhplus.tdd.point;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PointServiceImpl implements PointService {

    private final PointRepository pointRepository;

    @Override
    public UserPoint charge(long id, long amount) {
        UserPoint userPoint = pointRepository.findById(id);
        return pointRepository.update(id, userPoint.point()+amount);
    }

    @Override
    public UserPoint use(long id, long amount) {
        UserPoint userPoint = pointRepository.findById(id);
        long remainingPoint = userPoint.point() - amount;
        return pointRepository.update(id, remainingPoint);
    }
}