<template>
  <div style="font-family: customfont">
    <q-toolbar class="bg-black text-white">
   
      <q-toolbar-title> Car Rental System Management </q-toolbar-title>
      <router-link :to="{ name: 'login' }" class="custom-link">
        <q-btn
          unelevated
          rounded
          color="white"
          text-color="black"
          label="Login"
          class="text-center"
          style="margin: 10px; font-weight: bold"
        />
      </router-link>
    </q-toolbar>

    <q-carousel
      style="height: 40vh"
      animated
      v-model="slide"
      navigation
      infinite
      :autoplay="autoplay"
      arrows
      transition-prev="slide-right"
      transition-next="slide-left"
      @mouseenter="autoplay = false"
      @mouseleave="autoplay = true"
    >
      <q-carousel-slide :name="1" img-src="../../assets/image/background.jpg">
        <div class="absolute-bottom custom-caption">
          <div class="textCenter">รถยนต์หลากหลายพร้อมให้บริการ</div>
        </div></q-carousel-slide
      >
      <q-carousel-slide :name="2" img-src="../../assets/image/guest/allCar.jpg">
        <div class="absolute-bottom custom-caption">
          <div class="textCenter">นึกถึงรถเช่านึกถึงเรา Car Rental</div>
        </div></q-carousel-slide
      >
      <q-carousel-slide :name="3" img-src="../../assets/image/guest/rent.jpg">
        <div class="absolute-bottom custom-caption">
          <div class="textCenter">โปรโมชั่น เช่าครั้งแรกลด 10%!</div>
        </div></q-carousel-slide
      >
      <q-carousel-slide :name="4" img-src="../../assets/image/guest/check.jpg">
        <div class="absolute-bottom custom-caption">
          <div class="textCenter">มีการตรวจเช็คสภาพทุกครั้งก่อนให้บริการ</div>
        </div></q-carousel-slide
      >
    </q-carousel>
    <div style="background-color: rgb(19, 19, 19);">
    <div class="body">
      <div class="textHeader">GROUP MEMBERS</div>
    </div>

    <div class="row">
      <q-card class="my-card">
        <img src="../../assets/image/petch.jpg" />
        <q-card-section>
          <div class="text-h6">Pattrapol Jamjumrus</div>
          <div class="text-subtitle2">Front-End Developer</div>
        </q-card-section>

        <q-card-section class="q-pt-none">
          {{ lorem }}
        </q-card-section>
      </q-card>

      <q-card class="my-card">
        <img src="../../assets/image/top2.jpg" />
        <q-card-section>
          <div class="text-h6">Theerphat Aksaranan</div>
          <div class="text-subtitle2">Full-Stack Developer</div>
        </q-card-section>

        <q-card-section class="q-pt-none">
          {{ lorem }}
        </q-card-section>
      </q-card>

      <q-card class="my-card">
        <img src="../../assets/image/tam2.jpg" />
        <q-card-section>
          <div class="text-h6">Nared Fuengverojsakul</div>
          <div class="text-subtitle2">Back-End Developer</div>
        </q-card-section>

        <q-card-section class="q-pt-none">
          {{ lorem }}
        </q-card-section>
      </q-card>
    </div>
  </div>
  </div>
</template>

<script>
import axios from "axios";
import { ref, onBeforeMount } from "vue";
import router from "../../router";

export default {
  setup() {
    localStorage.removeItem('user-info'); 
    localStorage.removeItem('user-changepassword');
    return {
      slide: ref(1),
      autoplay: ref(false),
    };
  },
};
</script>

<style scoped>
@font-face {
  font-family: customfont;
  src: url(../../assets/fonts/Anuphan-Regular.ttf);
}
.my-font {
  font-family: "customfont";
}

.my-card {
  width: 100%;
  height: 100%;
  max-width: 250px;

  margin: 1rem;
}

.row {
  align-items: center;
  display: flex;
  justify-content: center;
}
.body {
  align-items: center;
  display: flex;
  justify-content: center;
  
  margin-bottom: 0vh;
}
.textHeader {
  margin-top: 10px;
  color: rgb(255, 255, 255);
  font-weight: bold;
  font-size: 3vh;
}
.textCenter {
  color: white;
  font-size: 5vh;
  margin-bottom: 15vh;
}

.custom-caption {
  text-align: center;
  padding: 12px;
  color: white;
  /* background-color: rgba(0, 0, 0, 0.3); */
}
</style>
