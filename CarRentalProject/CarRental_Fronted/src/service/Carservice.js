import axios from "axios";

const Car_API_BASE_URL = 'http://localhost:8081/Car_rental_backend/cars'

class CarService { 
    getCar(){
        return axios.get(Car_API_BASE_URL)
    }
}

export default new CarService()