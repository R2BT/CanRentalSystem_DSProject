import axios from "axios";

const User_API_BASE_URL = 'http://localhost:8081/Car_rental_backend/users/'

class UserService { 
    getUser(){
        return axios.get(User_API_BASE_URL)
    }
}

export default new UserService()