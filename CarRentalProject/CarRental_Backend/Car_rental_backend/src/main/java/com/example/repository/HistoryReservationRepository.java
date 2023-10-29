package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.model.HistoryReservation;
import com.example.model.Reservation;
import com.example.model.Users;

public interface HistoryReservationRepository extends CrudRepository<HistoryReservation, Integer> {
	
//	List<HistoryReservation> findByUser(Users user);
}
