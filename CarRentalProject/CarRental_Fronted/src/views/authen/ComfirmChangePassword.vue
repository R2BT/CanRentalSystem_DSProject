<template>
  <body v-if="userInfo.user_type != null">
    <q-page-container>
      <div class="clock-container">
        <div class="clock">
          <div class="historyButton">
            <div class="col">
              <router-link :to="{ name: 'login' }" class="custom-link">
                <q-btn
                  dense
                  flat
                  round
                  icon="arrow_back_ios_new"
                  color="white"
                  @click="toggleRightDrawer"
                  label="Back"
                />
              </router-link>
            </div>
          </div>
          <h4 class="textDone">
          เปลี่ยนรหัสผ่าน
          </h4>
          <q-form @submit="onSubmit" ref="form">
            <div class="col-margin">
              <q-input
              v-model="newPassword"
              label="รหัสผ่านใหม่"
              :type="isPwd ? 'password' : 'text'"
              ref="passwordField"
              :rules="[
                (value) =>
                  validatePassword(value) ||
                  'กรุณากรอกรหัสผ่านที่ถูกต้อง 1 ตัวอักษรพิเศษ 1 ตัวพิมพ์เล็ก 1 ตัวพิมพ์ใหญ่ และมีความยาวมากกว่า8ตัวอักษร',
              ]"
            >
              <template v-slot:append>
                <q-icon
                  :name="isPwd ? 'visibility_off' : 'visibility'"
                  class="cursor-pointer"
                  @click="isPwd = !isPwd"
                />
              </template>
            </q-input>
            </div>
            <div class="col-margin">
              <q-input
              v-model="confirmNewPassword"
              label="ยืนยันรหัสผ่านใหม่"
              :type="isPwd ? 'password' : 'text'"
              ref="passwordField"
              :rules="[(value) => !!value || 'กรุณากรอก Password']"
            >
              <template v-slot:append>
                <q-icon
                  :name="isPwd ? 'visibility_off' : 'visibility'"
                  class="cursor-pointer"
                  @click="isPwd = !isPwd"
                />
              </template>
            </q-input>
            </div>
            <div class="col-margin">
              <q-btn
                unelevated
                rounded
                color="positive"
                label="Submit"
                class="text-center"
                type="submit"
                @click="onSubmit"
                style="padding-left: 200px; padding-right: 200px"
              />
            </div>
          </q-form>
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
                ข้อมูลไม่ถูกต้องกรุณาตรวจสอบข้อมูลอีกครั้ง
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

<script>
import axios from "axios";
import { ref } from "vue";
import router from "../../router";
const showAlertDialog = ref(false);
export default {
  setup() {
    const myItem = localStorage.getItem("user-info");
    const userInfo = JSON.parse(myItem);
    console.log(userInfo);
    return {
      username: ref(null),
      firstname: ref(null),
      surname: ref(null),
      phonenumber: ref(null),
     showAlertDialog 

    };
  },
  methods: {
    validatePhoneNumber(number) {
      const regex = /^[0-9]{10}$/;
      return regex.test(number);
    },
    validateName(name) {
      const regex = /^[A-Za-zก-๙]+$/;
      return regex.test(name);
    },
    cancel() {
      this.show = false;
      window.location.reload();
    },
    async onSubmit() {
    
    },
    async encryptPassword(password) {
      const msgBuffer = new TextEncoder().encode(password);
      const hashBuffer = await crypto.subtle.digest("SHA-256", msgBuffer);
      const hashArray = Array.from(new Uint8Array(hashBuffer));
      const hashHex = hashArray
        .map((b) => b.toString(16).padStart(2, "0"))
        .join("");
      return hashHex;
    },
  },
};
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: sans-serif;
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
  font-size: 20px;
  font-weight: 700;
  line-height: 130%;
  justify-content: center;
  display: grid;
  flex-direction: column;
  margin: 15px;
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
