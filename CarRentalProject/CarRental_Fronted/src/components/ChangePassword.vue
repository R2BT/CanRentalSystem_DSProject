<template>
  <q-dialog v-model="showChangePassword" persistent>
    <q-card style="padding: 30px; width: 100%">
      <q-card-section>
        <q-card-title
          class="text-h6 start-container text-center"
          style="margin-bottom: 0"
        >
          <div style="font-size: 18px; font-weight: bold; margin-bottom: 30px">
            เปลี่ยนรหัสผ่าน<br />
          </div>
        </q-card-title>
        <q-card-title class="text-h6 start-container" style="margin-bottom: 0">
          <q-form ref="editForm" @submit="onSubmit" class="q-gutter-md">
            <h6>Username : {{ username }}</h6>

            <q-input
              v-model="oldPasswordInput"
              label="รหัสผ่านปัจจุบัน"
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

            <q-input
              v-model="newPassword"
              label="รหัสผ่านใหม่"
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
            <q-card-actions align="center" style="padding-top: 10px">
              <q-btn label="ยกเลิก" color="negative" @click="cancel" />
              <q-btn
                label="ยืนยัน"
                type="submit"
                color="positive"
                @click="onSubmit"
              />
            </q-card-actions>
          </q-form>
        </q-card-title>
      </q-card-section>
    </q-card>
  </q-dialog>
  <q-dialog v-model="showAlertDialog">
    <q-card
      style="width: 500px; padding: 10px; background-color: red; color: white"
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
      style="width: 500px; padding: 10px; background-color: green; color: white"
    >
      <q-card-section>
        <div class="text-h6">แจ้งเตือนการเปลี่ยนรหัสผ่าน</div>
      </q-card-section>
      <q-card-section class="q-pt-none"> เปลี่ยนรหัสผ่านสำเร็จ </q-card-section>
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
</template>

<script>
import router from "../router";
import axios from "axios";
import { ref } from "vue";
const showAlertDialog = ref(false);
const showAlertDialog2 = ref(false);
const userInfoGlo = ref(null);
export default {
  data() {
    return {
      showChangePassword: false,
    };
  },
  setup() {
    const myItem = localStorage.getItem("user-info");
    const userInfo = JSON.parse(myItem);
    userInfoGlo.value = JSON.parse(myItem);
    const idUser = userInfo.user_id;
    var AlertHeader = "";
    var AlertText = "";
    return {
      showAlertDialog,
      showAlertDialog2,
      idUser,
      username: ref(userInfo.user_username),
      oldPassword: ref(userInfo.user_password),
      oldPasswordInput: ref(null),
      newPassword: ref(null),
      confirmNewPassword: ref(null),
      firstname: ref(userInfo.user_firstname),
      surname: ref(userInfo.user_surname),
      phonenumber: ref(userInfo.user_phonenumber),
      showChangePassword: ref(false),
      isPwd: ref(true),
    };
  },
  methods: {
    async encryptPassword(password) {
      const msgBuffer = new TextEncoder().encode(password);
      const hashBuffer = await crypto.subtle.digest("SHA-256", msgBuffer);
      const hashArray = Array.from(new Uint8Array(hashBuffer));
      const hashHex = hashArray
        .map((b) => b.toString(16).padStart(2, "0"))
        .join("");
      return hashHex;
    },
    backToLogin() {
      router.push({ name: "login" });
      showAlertDialog2.value = false;
    },
    cancel() {
      this.showChangePassword = false;
      window.location.reload();
    },
    async onSubmit() {
      var oldPasswordInputEncrypt = await this.encryptPassword(
        this.oldPasswordInput
      );
      if (
        this.oldPasswordInput != null &&
        this.newPassword != null &&
        this.confirmNewPassword != null
      ) {
        if (oldPasswordInputEncrypt == this.oldPassword) {
          console.log("รหัสผ่านตรงกัน");
          if (this.newPassword == this.confirmNewPassword) {
            console.log("Comfirmpass");
            showAlertDialog2.value = true;

            var myHeaders = new Headers();
            myHeaders.append("Content-Type", "application/json; charset=UTF-8");
            var newpass = await this.encryptPassword(this.newPassword);
            const raw = {
              user_id: this.idUser,
              user_username: this.username,
              user_password: newpass,
              user_firstname: this.firstname,
              user_surname: this.surname,
              user_phonenumber: this.phonenumber,
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
                this.comfirmreSaveUser();
              });
          } else {
            console.log("Comfirmfail");
            showAlertDialog.value = true;
            this.AlertHeader = "รหัสผ่านใหม่กับยืนยันรหัสผ่านใหม่ไม่ตรงกัน";
            this.AlertText =
              "โปรดกรอกรหัสผ่านใหม่ และยืนยันรหัสผ่านใหม่ให้ถูกต้อง";
          }
        } else {
          console.log("รหัสผ่านไม่ตรงกัน");
          showAlertDialog.value = true;
          this.AlertHeader = "รหัสผ่านปัจจุบันไม่ถูกต้อง";
          this.AlertText = "โปรดกรอกรหัสผ่านปัจจุบันให้ถูกต้อง";
        }
      }
    },

    comfirmreSaveUser() {
      this.reSaveUser();
    },

    async reSaveUser() {
      let result = await axios.get(
        `http://localhost:8081/Car_rental_backend/users/login?username=${this.username}&password=${this.newpass}`
      );
      localStorage.clear();
      localStorage.setItem("user-info", JSON.stringify(result.data));
      window.location.reload();
    },
  },
};
</script>

<style scoped>
body {
  background-color: rgb(234, 234, 234);
  margin: 0;
  height: 100vh;
}

.background {
  background-image: url("../../assets/image/customer2.jpg");
  background-size: cover;
  margin: 0;
  height: 35vh;
}

.background-container {
  display: flex;
  justify-content: center;
  align-items: start;
  height: 35vh;
}

.block-background {
  display: grid;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 1.5rem;
  background: #ffffff22;
  backdrop-filter: blur(10px);
  /* border: 1px solid #000000; */
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

.search-bar {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
}

.search-input {
  width: 15%;
  margin: 0px;
  margin-bottom: 10px;
}

.add-button {
  margin: 0px;
  margin-bottom: 10px;
}

.close-container {
  display: flex;
  justify-content: flex-end;
}
.backtomenu {
  color: rgb(0, 0, 0);
  background-color: rgb(255, 255, 255);
  margin-top: 0.5rem;
  margin-left: 0.5rem;
}
</style>
