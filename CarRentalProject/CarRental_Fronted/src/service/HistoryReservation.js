import axios from "axios";

const HISTORY_RESERVATION_API_BASE_URL = 'http://localhost:8081/Car_rental_backend/historyreservations'

class HistoryReservationService { 
    getHistoryReservation(){
        return axios.get(HISTORY_RESERVATION_API_BASE_URL)
    }
}

export default new HistoryReservationService()