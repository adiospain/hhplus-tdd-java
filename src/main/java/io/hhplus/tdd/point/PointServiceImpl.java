package io.hhplus.tdd.point;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PointServiceImpl implements PointService {

    private final PointRepository pointRepository;

    public UserPoint chargeAmount(long id, long amount) {
        UserPoint userPoint = pointRepository.findById(id);
        return pointRepository.update(id, userPoint.point()+amount);
    }
}