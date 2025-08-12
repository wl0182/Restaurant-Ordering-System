package com.wassimlagnaoui.RestaurantOrder.Repository;

import com.wassimlagnaoui.RestaurantOrder.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
  // find staff by Employee ID
    Optional<Staff> findByEmployeeId(Long employeeId);
}

