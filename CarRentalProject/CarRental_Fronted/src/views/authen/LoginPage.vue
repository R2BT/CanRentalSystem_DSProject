<template>
  <body>
    <q-page-container>
      <div class="clock-container">
        <div class="clock">
          <div class="historyButton">
            <div class="col">
              <router-link :to="{ name: 'home' }" class="custom-link">
                <q-btn
                  dense
                  flat
                  round
                  icon="arrow_back_ios_new"
                  color="white"
                  @click="toggleRightDrawer"
                  label="Back"
              /></router-link>
            </div>
          </div>
          <div class="successPic">
            <img
              src="../../assets/image/logoCPE.png"
              style="margin: 15px; height: 150px; width: 150px"
            />
          </div>

          <h4 class="textDone">Car Rental System Management</h4>
          <q-form @submit="onSubmit" ref="form">
            <div class="col-margin">
              <q-input
                color="purple"
                bg-color="white"
                filled
                v-model="Username"
                label="Username"
                :rules="[(value) => !!value || 'กรุณากรอก Username']"
              ></q-input>
            </div>
            <div class="col-margin">
              <q-input
                v-model="Password"
                color="purple"
                bg-color="white"
                filled
                :type="isPwd ? 'password' : 'text'"
                label="Password"
                :rules="[(value) => !!value || 'กรุณากรอก Password']"
                ><template v-slot:append>
                  <q-icon
                    :name="isPwd ? 'visibility_off' : 'visibility'"
                    class="cursor-pointer"
                    @click="isPwd = !isPwd"
                  /> </template
              ></q-input>
            </div>

            <router-link :to="{ name: 'changepassword' }" class="custom-link">
              <h6 class="forgetPass">ลืมรหัสผ่าน?</h6>
            </router-link>
            <div class="col-margin">
              <q-btn
                unelevated
                rounded
                color="positive"
                label="Login"
                class="text-center"
                type="submit"
                style="padding-left: 200px; padding-right: 200px"
              />
            </div>

            <div class="col-margin">
              <router-link :to="{ name: 'register' }" class="custom-link">
                <q-btn
                  unelevated
                  rounded
                  color="grey"
                  label="Register"
                  class="text-center"
                  style="padding-left: 187px; padding-right: 187px"
                />
              </router-link>
            </div>
          </q-form>
          <q-dialog v-model="LockAlertDialog">
            <q-card
              style="
                width: 400px;
                padding: 10px;
                background-color: red;
                color: white;
              "
            >
              <q-card-section>
                <div class="text-h6" style="margin-bottom: 10px">
                  ใส่รหัสผ่านผิดบ่อยเกินไป
                </div>
              </q-card-section>
              <q-card-section class="q-pt-none">
                ใส่รหัสผ่านผิดบ่อยเกินไปกรุณาลองอีกครั้งใน
                {{ formattedBanDateTime }}
              </q-card-section>
              <q-card-actions align="right">
                <q-btn flat label="OK" color="white" v-close-popup />
              </q-card-actions>
            </q-card>
          </q-dialog>

          <q-dialog v-model="showAlertDialog">
            <q-card
              style="
                width: 400px;
                padding: 10px;
                background-color: red;
                color: white;
              "
            >
              <q-card-section>
                <div class="text-h6">Alert</div>
              </q-card-section>
              <q-card-section class="q-pt-none">
                Username หรือ Password ผิดกรุญาตรวจสอบใหม่อีกครั้ง?
              </q-card-section>
              <q-card-actions align="right">
                <q-btn flat label="OK" color="white" v-close-popup />
              </q-card-actions>
            </q-card>
          </q-dialog>
        </div>
      </div>
    </q-page-container>
  </body>
</template>

<script setup>
import axios from "axios";
import { ref, onBeforeMount } from "vue";
import router from "../../router";

const ACCOUNT_LOCKOUT_DURATION_MS = 300000; // 5 minutes in milliseconds

let lockoutTimer;
let timeShow;
let failedLoginAttempts = localStorage.getItem("count");
let MAX_FAILED_ATTEMPTS = 3;


const Username = ref(null);
const Password = ref(null);
const showAlertDialog = ref(false);
const LockAlertDialog = ref(false);
const isPwd = ref(true);
const currentDateTime = new Date();
let banDateTime = null;

const storedBanTime = localStorage.getItem("banTime");
const storedDateTime = new Date(storedBanTime);
const options = {
  year: "numeric",
  month: "2-digit",
  day: "2-digit",
  hour: "2-digit",
  minute: "2-digit",
  second: "2-digit",
};

const formattedBanDateTime = ref(new Date(localStorage.getItem("banTime")).toLocaleString("en-US", options));

async function encryptPassword(password) {
  const msgBuffer = new TextEncoder().encode(password);
  const hashBuffer = await crypto.subtle.digest("SHA-256", msgBuffer);
  const hashArray = Array.from(new Uint8Array(hashBuffer));
  const hashHex = hashArray
    .map((b) => b.toString(16).padStart(2, "0"))
    .join("");
  return hashHex;
}

async function login() {
  const storedBanTime = localStorage.getItem("banTime");
  const currentDateTime = new Date();
  if (currentDateTime < new Date(storedBanTime)) {
    LockAlertDialog.value = true;
  } else {
    if (Username.value != null && Password.value != null) {
      
      try {
        let sha256Password = await encryptPassword(Password.value);
        let result = await axios.get(
          `http://localhost:8081/Car_rental_backend/users/login?username=${Username.value}&password=${sha256Password}`
        );

        if (result.status === 200) {
          failedLoginAttempts = 0;
          localStorage.setItem("count",  failedLoginAttempts);
          // Handle success
          localStorage.clear();
          if (result.data.user_type == "ADMIN") {
            localStorage.clear();
            localStorage.setItem("user-info", JSON.stringify(result.data));
            router.push("/adminpanel");
          } else if (result.data.user_type == "USER") {
            localStorage.clear();
            localStorage.setItem("user-info", JSON.stringify(result.data));
            router.push("/rentcar");
          }
        } else if (result.status == 404) {
          // Handle 404 (Not Found)

          showAlertDialog.value = true;
          alert("User not found");
        }
      } catch (error) {
        console.log(MAX_FAILED_ATTEMPTS);
        failedLoginAttempts++; 
        localStorage.setItem("count", failedLoginAttempts);
        if (failedLoginAttempts >= MAX_FAILED_ATTEMPTS) {
          // Wait for the encryptPassword function to complete before setting banDateTime
          banDateTime = new Date(currentDateTime.getTime() + 10 * 60000);
          const storedDateTime = new Date(banDateTime);
          localStorage.removeItem("banTime");
          localStorage.setItem("banTime", banDateTime);
          failedLoginAttempts=0;
          localStorage.setItem("count", 0);
          currentDateTime.value = new Date();
          formattedBanDateTime.value = new Date(localStorage.getItem("banTime")).toLocaleString("en-US", options);
        }
        showAlertDialog.value = true;
      }
    }
  }
}
function clearLockoutTimer() {
  clearTimeout(lockoutTimer);
}
function onSubmit() {
  login();
}

onBeforeMount(() => {
  localStorage.removeItem("user-info"); // เลือกเคลียร์ข้อมูลที่มีชื่อ 'myData'
  localStorage.removeItem("user-changepassword");
});
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
body {
  background-image: url("../../assets/image/background.jpg");
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
  padding: 1.5rem;
  background: #ffffff22;

  border-radius: 1.4rem;
  box-shadow: 0 0 1.2rem rgba(0, 0, 0, 0.35);
  color: white;
  width: 600px;

  backdrop-filter: blur(10px); /* Adjust the blur amount as needed */
}

.col-margin {
  margin-top: 30px;
  margin-left: 90px;
  margin-right: 90px;
}
.textDone {
  color: #ffffff;
  font-size: 25px;
  font-weight: 700;
  line-height: 130%;
  justify-content: center;
  display: grid;
  flex-direction: column;
  margin: 15px;
}
.forgetPass {
  color: #ffffff;
  font-size: 14px;
  font-weight: 400;
  line-height: 130%;
  justify-content: right;
  display: grid;
  flex-direction: column;

  margin-right: 100px;
  margin-top: 12px;
}
.successPic {
  justify-content: center;
  display: grid;
}
.q-dialog {
  width: 300px;
  height: 200px;
  background-color: red;
  color: white;
}
.historyButton {
  display: grid;
  flex-direction: column;
  justify-content: left;
  font-size: 1.5rem;
  margin-bottom: 10px;
  margin-left: 50px;
}
</style>
