package io.hhplus.tdd.point;

import lombok.AllArgsConstructor;


public interface PointService {
    public UserPoint charge(long id, long amount);
}
