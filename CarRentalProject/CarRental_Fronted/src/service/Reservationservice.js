import axios from "axios";

const RESERVATION_API_BASE_URL = 'http://localhost:8081/Car_rental_backend/reservations'

class ReservationService { 
    getReservation(){
        return axios.get(RESERVATION_API_BASE_URL)
    }
}

export default new ReservationService()