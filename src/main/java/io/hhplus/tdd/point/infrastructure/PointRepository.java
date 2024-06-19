package io.hhplus.tdd.point.infrastructure;

import io.hhplus.tdd.point.dto.UserPoint;

public interface PointRepository {
    UserPoint findById(long id);
    UserPoint update(long id, long amount);
    UserPoint insert(long id, long amount);
}
