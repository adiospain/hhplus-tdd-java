package io.hhplus.tdd.point;
public interface PointRepository {
    UserPoint findById(long id);
    UserPoint update(long id, long amount);
    UserPoint insert(long id, long amount);
}
