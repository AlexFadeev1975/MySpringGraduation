package org.example.repository;

import org.example.data.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaSpecificationExecutor<Booking>, JpaRepository<Booking, Long> {
}
