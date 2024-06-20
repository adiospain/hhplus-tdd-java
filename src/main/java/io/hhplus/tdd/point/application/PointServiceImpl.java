package io.hhplus.tdd.point.application;

import io.hhplus.tdd.exception.CustomException;
import io.hhplus.tdd.exception.ErrorResponse;
import io.hhplus.tdd.point.domain.TransactionType;
import io.hhplus.tdd.point.dto.PointHistory;
import io.hhplus.tdd.point.dto.UserPoint;
import io.hhplus.tdd.point.exception.PointException;
import io.hhplus.tdd.point.infrastructure.PointRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PointServiceImpl implements PointService {

    private final PointRepository pointRepository;

    @Override
    public UserPoint charge(long id, long amount) {
        UserPoint userPoint = pointRepository.findById(id);
        long totalPoint = userPoint.point() + amount;
        pointRepository.insertHistory(id, amount, TransactionType.CHARGE, System.currentTimeMillis());
        return pointRepository.update(id, totalPoint);
    }

    @Override
    public UserPoint point(long id) {
        if (id <= 0) {
            ErrorResponse errorResponse = new ErrorResponse("INVALID_USER_ID", "유효하지 않은 ID 입니다.");
            throw new PointException(errorResponse);
        }
        return pointRepository.findById(id);
    }




    @Override
    public UserPoint use(long id, long amount) {
        if (amount <= 0 ) {
            ErrorResponse errorResponse = new ErrorResponse("INVALID_AMOUNT", "사용 포인트는 양수여야 합니다.");
            throw new PointException(errorResponse);
        }
        UserPoint userPoint = pointRepository.findById(id);
        if (userPoint.point() < amount){
            ErrorResponse errorResponse = new ErrorResponse("CONFLICT", "포인트가 충분하지 않습니다.");
            throw new PointException(errorResponse);
        }
        long remainingPoint = userPoint.point() - amount;
        pointRepository.insertHistory(id, amount, TransactionType.USE, System.currentTimeMillis());
        return pointRepository.update(id, remainingPoint);
    }

    @Override
    public List<PointHistory> history(long id) {
        return pointRepository.findHistoryById(id);
    }
}