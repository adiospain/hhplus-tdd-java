package io.hhplus.tdd.point.application;

import io.hhplus.tdd.point.domain.TransactionType;
import io.hhplus.tdd.point.dto.PointHistory;
import io.hhplus.tdd.point.dto.UserPoint;
import io.hhplus.tdd.point.infrastructure.PointRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Semaphore;

@Service
@AllArgsConstructor
public class PointServiceImpl implements PointService {

    private final Semaphore sema = new Semaphore(1);

    private final PointRepository pointRepository;

    @Override
    public UserPoint charge(long id, long amount) {

        try{
            sema.acquire();
            UserPoint userPoint = pointRepository.findById(id);
            pointRepository.insertHistory(id, amount, TransactionType.CHARGE, System.currentTimeMillis());
            return pointRepository.update(id, userPoint.point()+amount);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while processing charge for user ID: " + id, e);
        } finally {
            pointRepository.insertHistory(id, amount, TransactionType.USE, System.currentTimeMillis());
            sema.release();
        }
    }

    @Override
    public UserPoint point(long id) {
        return pointRepository.findById(id);
    }

    @Override
    public UserPoint use(long id, long amount) {
        try {
            sema.acquire();
            UserPoint userPoint = pointRepository.findById(id);
            long remainingPoint = userPoint.point() - amount;
            pointRepository.insertHistory(id, amount, TransactionType.USE, System.currentTimeMillis());
            return pointRepository.update(id, remainingPoint);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
         finally {
            pointRepository.insertHistory(id, amount, TransactionType.USE, System.currentTimeMillis());
            sema.release();
        }
    }


    @Override
    public List<PointHistory> history(long id) {
        return pointRepository.findHistoryByUserId(id);
    }
}