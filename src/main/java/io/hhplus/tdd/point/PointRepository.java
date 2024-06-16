package io.hhplus.tdd.point;
public interface PointRepository {
    UserPoint findById(Long id);
    UserPoint update(Long id, Long amount);
}
