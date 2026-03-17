package bai4;

import java.util.List;

public interface OrderRepository {
    void save(bai5.model.Order order);
    List<bai5.model.Order> findAll();
}
