package com.example.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.HistoryReservation;
import com.example.model.Reservation;
import com.example.model.Users;
import com.example.repository.HistoryReservationRepository;

@Service
public class HistoryReservationService {
	@Autowired
	private HistoryReservationRepository historyreservationRepository;
	
	public List<HistoryReservation> findAll(){
		return (List<HistoryReservation>) historyreservationRepository.findAll();
	}
	
//	public List<HistoryReservation> findByUser(Users user) {
//	    return historyreservationRepository.findByUser(user);
//	}

	public HistoryReservation createHistory(HistoryReservation historyreservation) {
        return historyreservationRepository.save(historyreservation);
    }
}
