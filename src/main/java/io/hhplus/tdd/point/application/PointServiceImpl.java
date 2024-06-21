package io.hhplus.tdd.point.application;

import io.hhplus.tdd.exception.ErrorResponse;
import io.hhplus.tdd.point.domain.TransactionType;
import io.hhplus.tdd.point.dto.PointHistory;
import io.hhplus.tdd.point.dto.UserPoint;
import io.hhplus.tdd.point.exception.PointException;
import io.hhplus.tdd.point.infrastructure.PointRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Semaphore;

@Service
@AllArgsConstructor
public class PointServiceImpl implements PointService {

    private final Semaphore semaphore = new Semaphore(1);

    private final PointRepository pointRepository;

    @Override
    public UserPoint charge(long id, long amount) {
        try{
            semaphore.acquire();
            validateParameter(id, amount);
            UserPoint userPoint = pointRepository.findById(id);

            long totalPoint = userPoint.point() + amount;

            if (totalPoint < 0){
                ErrorResponse errorResponse = new ErrorResponse("OVERFLOW", "오버플로우 발생 했습니다.");
                throw new PointException(errorResponse);
            }
            return pointRepository.update(id, totalPoint);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while processing charge for user ID: " + id, e);
        } finally {
            pointRepository.insertHistory(id, amount, TransactionType.USE, System.currentTimeMillis());
            semaphore.release();
        }



    }

    @Override
    public UserPoint point(long id) {
        validateUserId(id);
        return pointRepository.findById(id);
    }




    @Override
    public UserPoint use(long id, long amount) {
        try {
            semaphore.acquire();
            validateParameter(id, amount);

            UserPoint userPoint = pointRepository.findById(id);
            if (userPoint.point() < amount){
                ErrorResponse errorResponse = new ErrorResponse("CONFLICT", "포인트가 충분하지 않습니다.");
                throw new PointException(errorResponse);
            }
            long remainingPoint = userPoint.point() - amount;
            return pointRepository.update(id, remainingPoint);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        finally {
            pointRepository.insertHistory(id, amount, TransactionType.USE, System.currentTimeMillis());
            semaphore.release();
        }

    }

    @Override
    public List<PointHistory> history(long userId) {
        validateUserId(userId);

        return pointRepository.findHistoryByUserId(userId);
    }

    public void validateUserId(long id){
        if (id <= 0 ) {
            ErrorResponse errorResponse = new ErrorResponse("INVALID_USER_ID", "유효하지 않은 ID 입니다.");
            throw new PointException(errorResponse);
        }
    }

    public void validateParameter(long id, long amount){
        validateUserId(id);
        if (amount <= 0 ) {
            throw new PointException(new ErrorResponse("INVALID_AMOUNT", "사용 포인트는 양수여야 합니다."));
        }
    }

}