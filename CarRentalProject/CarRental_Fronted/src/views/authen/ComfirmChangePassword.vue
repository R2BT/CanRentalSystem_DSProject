<template>
  <!-- <body v-if="userInfo.user_type != null"> -->
  <body v-if="userInfo && userInfo.user_type !== null">
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
          <h4 class="textDone">เปลี่ยนรหัสผ่าน</h4>

          <q-form @submit="onSubmit" ref="form">
            <div class="col-margin">
              <q-input
                filled
                bg-color="white"
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
                filled
                bg-color="white"
                v-model="confirmNewPassword"
                label="ยืนยันรหัสผ่านใหม่"
                :type="isPwd2 ? 'password' : 'text'"
                ref="passwordField"
                :rules="[(value) => !!value || 'กรุณากรอก Password']"
              >
                <template v-slot:append>
                  <q-icon
                    :name="isPwd2 ? 'visibility_off' : 'visibility'"
                    class="cursor-pointer"
                    @click="isPwd2 = !isPwd2"
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
                width: 500px;
                padding: 10px;
                background-color: red;
                color: white;
              "
            >
              <q-card-section>
                <div class="text-h6">{{ AlertHeader }}</div>
              </q-card-section>
              <q-card-section class="q-pt-none">
                {{ AlertText }}
              </q-card-section>
              <q-card-actions align="right">
                <q-btn flat label="OK" color="white" v-close-popup />
              </q-card-actions>
            </q-card>
          </q-dialog>
          <q-dialog v-model="showAlertDialog2">
            <q-card
              style="
                width: 500px;
                padding: 10px;
                background-color: green;
                color: white;
              "
            >
              <q-card-section>
                <div class="text-h6">ตั้งค่ารหัสผ่านใหม่สำเร็จ</div>
              </q-card-section>
              <q-card-section class="q-pt-none">
                เปลี่ยนรหัสผ่านสำเร็จ กดปุ่ม OK เพื่อไปยังหน้า Login
              </q-card-section>
              <q-card-actions align="right">
                <q-btn
                  flat
                  label="OK"
                  color="white"
                  v-close-popup
                  @click="backToLogin"
                />
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
const showAlertDialog2 = ref(false);
export default {
  setup() {
    const myItem = localStorage.getItem("user-changepassword");
    const userInfo = JSON.parse(myItem);
    const idUser = userInfo.user_id;
    const count = userInfo.count_rent;
    console.log(userInfo);
    return {
      userInfo,
      isPwd: ref(true),
      isPwd2: ref(true),
      idUser,
      count,
      username: userInfo.user_username,
      firstname: userInfo.user_firstname,
      surname: userInfo.user_surname,
      phonenumber: userInfo.user_phonenumber,
      newPassword: ref(null),
      confirmNewPassword: ref(null),
      showAlertDialog,
      showAlertDialog2,
    };
  },
  methods: {
    validatePassword(password) {
      const regex =
        /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
      return regex.test(password);
    },
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
    backToLogin() {
      router.push({ name: "login" });
      showAlertDialog2.value = false;
    },
    async onSubmit() {
      if (
        this.newPassword != null &&
        this.confirmNewPassword != null &&
        this.newPassword.length > 8 &&
        /[a-z]/.test(this.newPassword) &&
        /[A-Z]/.test(this.newPassword) &&
        /[!@#$%^&*]/.test(this.newPassword)&&
        !/[\u0E00-\u0E7F]/.test(this.newPassword)
      ) {
        console.log("notnull");
        if (this.newPassword == this.confirmNewPassword) {
          console.log("equal");
          var myHeaders = new Headers();
          myHeaders.append("Content-Type", "application/json; charset=UTF-8");
          var newpass = await this.encryptPassword(this.newPassword);
          console.log("Pass:" + this.newPassword);
          console.log("Passencrypt:" + newpass);
          const raw = {
            user_id: this.idUser,
            user_username: this.username,
            user_password: newpass,
            user_firstname: this.firstname,
            user_surname: this.surname,
            user_phonenumber: this.phonenumber,
            count_rent: this.count,
            user_type: "USER",
          };
          var requestOptions = {
            method: "PUT",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(raw),
            redirect: "follow",
          };
          fetch(
            `http://localhost:8081/Car_rental_backend/users/${this.idUser}`,
            requestOptions
          )
            .then((response) => response.text())
            .then((result) => {
              showAlertDialog2.value = true; // this.comfirmreSaveUser();
            });
          // showAlertDialog2.value = true;
        } else {
          showAlertDialog.value = true;
          this.AlertHeader = "รหัสผ่านใหม่กับยืนยันรหัสผ่านไม่ตรงกัน";
          this.AlertText = "โปรดกรอกรหัสผ่านใหม่ และยืนยันรหัสผ่านให้ถูกต้อง";
        }
      } else {
        showAlertDialog.value = true;
        this.AlertHeader = "กรุณากรอกข้อมูลให้ครบถ้วน";
        this.AlertText = "โปรดกรอกรหัสผ่านใหม่และยืนยันรหัสผ่านให้ครบถ้วน";
      }
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
    // comfirmreSaveUser() {
    //   this.reSaveUser();
    // },

    // async reSaveUser() {
    //   let result = await axios.get(
    //     `http://localhost:8081/Car_rental_backend/users/login?username=${this.username}&password=${this.newpass}`
    //   );
    //   localStorage.clear();
    //   localStorage.setItem("user-info", JSON.stringify(result.data));
    //   window.location.reload();
    // },
  },
};
</script>

<style scoped>
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
