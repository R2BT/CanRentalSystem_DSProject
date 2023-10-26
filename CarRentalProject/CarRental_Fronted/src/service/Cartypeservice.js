import axios from "axios";

const Cartype_API_BASE_URL = 'http://localhost:8081/Car_rental_backend/cartypes'

class CartypeService { 
    getCar(){
        return axios.get(Cartype_API_BASE_URL)
    }
}

export default new CartypeService()