<template>
  <q-layout view="hHh lpR fFf">
    <Navbar v-if="userInfo.user_type === 'USER'"></Navbar>
    <body v-if="userInfo.user_type === 'USER'">
      <q-page-container>
        <div class="clock-container">
          <div class="clock">
            <div class="successPic">
              <img
                src="../../assets/image/check.png"
                style="margin: 15px; height: 150px; width: 150px"
              />
            </div>
            <h4 class="textDone">ยืนยันรายการเช่ารถสำเร็จ</h4>
            <h4 class="textDone2">ตรวจสอบได้ที่รายการเช่ารถของฉัน</h4>
            <span style="margin-top: 15px"></span>
              <q-btn
                unelevated
                rounded
                color="grey"
                label="ย้อนกลับ"
                @click="reLoadUser"
                style="padding-left: 200px; padding-right: 200px"
              />
          </div>
        </div>
      </q-page-container>
    </body>
    <div v-else>
      <p>Page Not Found</p>
    </div>
  </q-layout>
</template>
<script>
import Navbar from "../../components/EmployeeHeader.vue";
import axios from "axios";
import router from "../../router";
export default {
  setup() {
    const myItem = localStorage.getItem("user-info");
    const userInfo = JSON.parse(myItem);
    return {
      userInfo,
    };
  },
  components: {
    Navbar,
  },
  methods: {
    async reLoadUser() {
  try {
    const myItem = localStorage.getItem("user-info");
    const userInfo = JSON.parse(myItem);
    const username = userInfo.user_username;
    const password = userInfo.user_password;

    const response = await axios.get(
      `http://localhost:8081/Car_rental_backend/users/login?username=${username}&password=${password}`
    );

    localStorage.clear();
    localStorage.setItem("user-info", JSON.stringify(response.data));
    router.push("/rentcar");
  } catch (error) {
    console.error("Error loading user data:", error);
  }
}

  }
};
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: "customfont";
}
body {
  background-image: url("../../assets/image/background3.svg");
  background-size: cover;
  margin: 0;
  min-block-size: 100vh;
}
.clock-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 90vh;
}

.clock {
  display: grid;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 1.2rem;
  background: #ffffff22;
  border-radius: 1.4rem;
  box-shadow: 0 0 1.2rem rgba(0, 0, 0, 0.35);
  color: white;
  width: 500px;
}

.time {
  font-size: 5rem;
  font-weight: bold;
  width: max-content;
}

.date {
  display: grid;
  flex-direction: column;
  justify-content: center;
  font-size: 1.5rem;
}
.textDone {
  color: #ffffff;
  font-size: 25px;
  font-weight: 700;
  line-height: 130%;
  justify-content: center;
  display: grid;
  flex-direction: column;
  margin-left: 15px;
  margin-right: 15px;
  margin-top: 15px;
}
.textDone2 {
  color: #ffffff;
  font-size: 25px;
  font-weight: 700;
  line-height: 130%;
  justify-content: center;
  display: grid;
  flex-direction: column;
  margin-bottom: 5px;
}
.successPic {
  justify-content: center;
  display: grid;
}
</style>
