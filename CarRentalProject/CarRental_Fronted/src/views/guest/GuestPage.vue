<template>
  <q-toolbar class="bg-black text-white">
    <q-btn flat round dense icon="menu" />
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
    style="height: 92vh"
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
        <div class="textCenter">
          Car Rental System Management <br />เว็ปไซต์สำหรับให้บริการเช่ารถยนต์
        </div>
      </div></q-carousel-slide
    >
    <q-carousel-slide
      :name="2"
      img-src="https://cdn.quasar.dev/img/parallax1.jpg"
    >
      <div class="absolute-bottom custom-caption">
        <div class="textCenter">
          Car Rental System Management <br />เว็ปไซต์สำหรับให้บริการเช่ารถยนต์
        </div>
      </div></q-carousel-slide
    >
    <q-carousel-slide
      :name="3"
      img-src="https://cdn.quasar.dev/img/parallax1.jpg"
    >
      <div class="absolute-bottom custom-caption">
        <div class="textCenter">
          Car Rental System Management <br />เว็ปไซต์สำหรับให้บริการเช่ารถยนต์
        </div>
      </div></q-carousel-slide
    >
    <q-carousel-slide
      :name="4"
      img-src="https://cdn.quasar.dev/img/parallax1.jpg"
    >
      <div class="absolute-bottom custom-caption">
        <div class="textCenter">
          Car Rental System Management <br />เว็ปไซต์สำหรับให้บริการเช่ารถยนต์
        </div>
      </div></q-carousel-slide
    >
  </q-carousel>
  <div class="body">
    <div class="textHeader">ABOUT US</div>
  </div>

  <div class="row">
    <div class="col">
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
    </div>
    <div class="col">
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
    </div>
    <div class="col">
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
</template>

<script>
import axios from "axios";
import { ref, onBeforeMount } from "vue";
import router from "../../router";

export default {
  setup() {
    return {
      slide: ref(1),
      autoplay: ref(false),
    };
  },
};
</script>

<style>
.col {
  margin: 1rem;
}
.body {
  align-items: center;
  display: flex;
  justify-content: center;
  margin-top: 10vh;
  margin-bottom: 3vh;
}
.textHeader {
  color: black;
  font-size: 3vh;
}
.textCenter {
  color: white;
  font-size: 3vh;
  margin-bottom: 35vh;
}

.custom-caption {
  text-align: center;
  padding: 12px;
  color: white;
  /* background-color: rgba(0, 0, 0, 0.3); */
}
</style>
