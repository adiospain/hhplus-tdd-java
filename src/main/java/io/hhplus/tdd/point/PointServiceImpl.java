package io.hhplus.tdd.point;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PointServiceImpl implements PointService {

    private final PointRepositoryImpl pointRepository;

    public UserPoint chargeAmount(long id, long amount) {
        return pointRepository.chargeAmount(id, amount);
    }
}