package io.hhplus.tdd.point.application;

import io.hhplus.tdd.point.dto.PointHistory;
import io.hhplus.tdd.point.dto.UserPoint;

import java.util.List;


public interface PointService {
    public UserPoint charge(long id, long amount);
    public UserPoint point(long id);
    public UserPoint use(long id, long amount);
    public List<PointHistory> history (long id);
}
