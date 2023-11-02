import { createRouter, createWebHistory } from "vue-router";
// import HomeView from '../views/HomeView.vue'
import CreateReservation from "../views/user/CreateReservation.vue";
import RentSuccess from "../views/user/RentSuccess.vue";
import Login from "../views/authen/LoginPage.vue";
import AdminPanel from "../views/admin/AdminMenu.vue";
import Viewcars from "../views/user/ViewCars.vue";
import AdminCar from "../views/admin/AdminCar.vue";
import AdminUser from "../views/admin/AdminUser.vue";
import AdminReservation from "../views/admin/AdminReservation.vue";
import AdminHistoryReservation from "../views/admin/AdminHistoryReservation.vue";
import ViewReservations from "../views/user/ViewReservation.vue";
import ComfirmChangePassword from "../views/authen/ComfirmChangePassword.vue";
import ChangePassword from "../views/authen/ChangePassword.vue";
import Register from "../views/authen/Register.vue";
import RegisterSuccess from "../views/authen/RegisterSuccess.vue";
import Home from "../views/guest/GuestPage.vue";
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/login",
      name: "/login",
      component: Login,
    },
    {
      path: "/",
      name: "home",
      component: Home,
    },
    {
      path: "/rentcar",
      name: "rentcar",

      component: CreateReservation,
    },

    {
      path: "/rentcar/success",
      name: "rentcarsuccess",

      component: RentSuccess,
    },
    {
      path: "/viewcars",
      name: "viewcars",
      component: Viewcars,
    },
    {
      path: "/viewreservation",
      name: "viewreservation",
      component: ViewReservations,
    },

    {
      path: "/login",
      name: "login",
      component: Login,
    },
    {
      path: "/register",
      name: "register",
      component: Register,
    },
    {
      path: "/register/changepassword",
      name: "changepassword",
      component: ChangePassword,
    },
    {
      path: "/register/changepassword/comfirm",
      name: "comfirmchangepassword",
      component: ComfirmChangePassword,
    },
    {
      path: "/register/success",
      name: "registersuccess",
      component: RegisterSuccess,
    },
    {
      path: "/adminpanel",
      name: "adminpanel",
      component: AdminPanel,
    },
    {
      path: "/adminpanel/reservations",
      name: "adminreservation",
      component: AdminReservation,
    },
    {
      path: "/adminpanel/reservations/history",
      name: "adminhistoryservation",
      component: AdminHistoryReservation,
    },
    {
      path: "/adminpanel/cars",
      name: "admincar",
      component: AdminCar,
    },
    {
      path: "/adminpanel/users",
      name: "adminuser",
      component: AdminUser,
    },
  ],
});

export default router;
