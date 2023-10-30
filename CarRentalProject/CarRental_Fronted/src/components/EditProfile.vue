<template>
    <q-dialog v-model="show" persistent>
      <q-card style="padding: 30px; width: 100%">
        <q-card-section>
          <q-card-title
            class="text-h6 start-container text-center"
            style="margin-bottom: 0"
          >
            <div style="font-size: 18px; font-weight: bold; margin-bottom: 10px;">
              แก้ไขโปรไฟล์<br />
            </div>
          </q-card-title>
          <q-card-title class="text-h6 start-container" style="margin-bottom: 0">
            <q-form ref="editForm" @submit="onSubmit" class="q-gutter-md">
              <q-input v-model="username" label="Username" ref="usernameField" :rules="[(value) => !!value || 'กรุณากรอก Username']" />
              <q-input
                v-model="password"
                label="Password"
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
              <q-input v-model="firstname" label="ชื่อจริง" ref="firstnameField" :rules="[(value) => !!value || 'กรุณากรอกชื่อจริง']" />
              <q-input v-model="surname" label="นามสกุล" ref="surnameField" :rules="[(value) => !!value || 'กรุณากรอกนามสกุล']" />
              <q-input
                v-model="phonenumber"
                label="เบอร์โทรศัพท์"
                type="number"
                maxlength=10
                ref="phonenumberField"
                :rules="[(value) => !!value || 'กรุณากรอกเบอร์โทรศัพท์']"
              />
              <q-card-actions align="center" style="padding-top: 10px">
                <q-btn label="ยกเลิก" color="negative" @click="cancel" />
                <q-btn label="ยืนยัน" type="submit" color="positive" @click="onSubmit" />
              </q-card-actions>
            </q-form>
          </q-card-title>
        </q-card-section>
      </q-card>
    </q-dialog>
  </template>
  
<script>
import router from "../router";
import axios from "axios";
import { ref } from "vue";
const userInfoGlo = ref(null);
export default {
  setup() {

    const myItem = localStorage.getItem("user-info");
    const userInfo = JSON.parse(myItem);
    userInfoGlo.value = JSON.parse(myItem);
    const idUser = userInfo.user_id;
    return {
      idUser,
      username: ref(userInfo.user_username),
      password: ref(userInfo.user_password),
      firstname: ref(userInfo.user_firstname),
      surname: ref(userInfo.user_surname),
      phonenumber: ref(userInfo.user_phonenumber),
      show: ref(false),
      isPwd: ref(true),
    };
  },
  methods: {
    cancel() {

      this.show = false;
      window.location.reload();
     
    },
    async onSubmit() {
      if(this.username!=''&&this.password!=''&&this.firstname!=''&&this.surname!=''&&this.phonenumber!=''){
        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json; charset=UTF-8");
        const raw = {
          user_id: this.idUser,
          user_username: this.username,
          user_password: this.password,
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
        }
    },

    comfirmreSaveUser() {
      this.reSaveUser();
    },

    async reSaveUser() {
      let result = await axios.get(
        `http://localhost:8081/Car_rental_backend/users/login?username=${this.username}&password=${this.password}`
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
