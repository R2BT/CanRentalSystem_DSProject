<template>
  <q-layout view="hHh lpR fFf">
    <Navbar v-if="userInfo.user_type === 'ADMIN'"></Navbar>
    <body v-if="userInfo.user_type === 'ADMIN'">
      <q-page-container>
        <div class="background">
          <div class="background-container">
            <div class="block-background" style="width: 40%">
              <h6>Car Rental System Management</h6>
              <h3>Admin Panel</h3>
            </div>
          </div>
        </div>
        <div class="managing-block">
          <p class="text1">จัดการข้อมูล</p>
          <div class="row">
            <div class="col">
              <div class="hrblock">
                <div class="row">
                  <div class="col-5 hrinfo"></div>
                  <div class="col">
                    <p class="text2">ข้อมูลรายการเช่ารถ</p>
                    <p class="text3">จัดการข้อมูลรายการเช่ารถทั้งหมด</p>
                    <div class="btn-next">
                      <router-link :to="{ name: 'adminreservation' }">
                        <q-btn label="ถัดไป" class="custom-btn" />
                      </router-link>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="col">
              <div class="hrblock">
                <div class="row">
                  <div class="col-5 hrcheckin"></div>
                  <div class="col">
                    <p class="text2">ข้อมูลรถเช่าทั้งหมด</p>
                    <p class="text3">จัดการข้อมูลรถเช่าทั้งหมด</p>
                    <div class="btn-next" >
                      <router-link :to="{ name: 'admincar' }">
                        <q-btn label="ถัดไป" class="custom-btn" />
                      </router-link>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="row">
            <div class="col">
              <div class="hrblock">
                <div class="row">
                  <div class="col-5 hrleave"></div>
                  <div class="col">
                    <p class="text2">ข้อมูลลูกค้าในระบบ</p>
                    <p class="text3">จัดการข้อมูลลูกค้าในระบบทั้งหมด</p>
                    <div class="btn-next">
                      <router-link :to="{ name: 'adminuser' }">
                        <q-btn label="ถัดไป" class="custom-btn" />
                      </router-link>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="col"></div>
          </div>
        </div>
      </q-page-container>
    </body>
  </q-layout>
</template>

<script>
import Navbar from "../../components/EmployeeHeader.vue";
export default {
  components: {
    Navbar,
  },
  setup() {
    const myItem = localStorage.getItem("user-info");
    const userInfo = JSON.parse(myItem);
    return {
      userInfo,
    };
  },
};
</script>

<style scoped>

.background {
  background-image: url("../../assets/image/background4.svg");
  background-size: cover;
  margin: 0;
  height: 40vh;
}
.background-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 40vh;
}
.block-background {
  display: grid;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 1.5rem;
  opacity: 1;
  background: #ffffff22;
  backdrop-filter: blur(10px);
  border-radius: 1.4rem;
  box-shadow: 0 0 1.2rem rgba(0, 0, 0, 0.35);
  color: white;
  text-align: center;
}
h2 {
  margin-top: 5px;
  margin-left: 10px;
  margin-right: 10px;
  margin-bottom: 10px;
}
h6 {
  margin-top: 10px;
  margin-left: 10px;
  margin-right: 10px;
  margin-bottom: 5px;
}
.managing-block {
  display: grid;
  flex-direction: column;
  padding: 1.5rem;
  opacity: 1;
  background: #ffffff22;
  background-color: white;
  /* border-radius: 1.4rem; */
  box-shadow: 0 0 1.2rem rgba(0, 0, 0, 0.35);
  color: white;
  text-align: center;
}
.hrblock {
  display: grid;
  flex-direction: column;
  margin: 1.4rem;
  /* padding: 1.4rem; */
  background: #ffffff22;
  background-color: white;
  border-radius: 0.5rem;
  border: 1px solid #d2d2d2;
}
.text1 {
  color: #395695;
  font-size: 1.5rem;
  font-weight: bold;
}
.hrinfo {
  background-image: url("../../assets/image/hrleave.png");
  background-size: cover;
  margin: 0;
  height: 12rem;
  border-top-left-radius: 0.5rem;
  border-bottom-left-radius: 0.5rem;
}
.hrcheckin {
  background-image: url("../../assets/image/background.jpg");
  background-size: cover;
  margin: 0;
  height: 12rem;
  border-top-left-radius: 0.5rem;
  border-bottom-left-radius: 0.5rem;
}
.hrleave {
  background-image: url("../../assets/image/customer.jpg");
  background-size: cover;
  margin: 0;
  height: 12rem;
  border-top-left-radius: 0.5rem;
  border-bottom-left-radius: 0.5rem;
}
.btn-next {
  display: flex;
  justify-content: end;
  align-items: end;
  padding: 1rem;
  height: 7rem;
}
.custom-btn {
  background-color: #212a3e;
  color: white;
}
.text2 {
  display: flex;
  justify-content: start;
  align-items: start;
  padding-top: 1rem;
  padding-left: 1rem;
  margin: 0rem;
  color: black;
  font-weight: bold;
}
.text3 {
  display: flex;
  justify-content: start;
  align-items: start;
  padding-top: 1rem;
  padding-left: 1rem;
  margin: 0rem;
  color: #7b7b7b;
}
</style>
