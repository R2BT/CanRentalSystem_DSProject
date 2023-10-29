package com.example.controller;

import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Car;
import com.example.model.Car_Type;
import com.example.model.HistoryReservation;
import com.example.model.Reservation;
import com.example.model.Users;
import com.example.service.CarService;
import com.example.service.HistoryReservationService;
import com.example.service.ReservationService;
import com.example.service.UserService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/historyreservations")
@CrossOrigin
public class HistoryReservationController {
	
	@Autowired
	private HistoryReservationService historyreservationService;
	@Autowired
	private CarService carService;
	@Autowired
	private UserService userService;
	
	@GetMapping
	public List<HistoryReservation> findAll() {
		return (List<HistoryReservation>) historyreservationService.findAll();
	}
	
	@PostMapping
	public ResponseEntity<HistoryReservation> createHistory(@RequestBody HistoryReservation history) {
		HistoryReservation createdHistory = historyreservationService.createHistory(history);
		return new ResponseEntity<HistoryReservation>(createdHistory, HttpStatus.CREATED);
	}
}
