<template>
  <q-dialog v-model="show" persistent>
    <q-card>
      <q-card-section style="width: 500px; height: 50px">
        <q-card-main> ต้องการออกจากระบบ? </q-card-main>
      </q-card-section>
      <q-card-actions align="right">
        <q-btn label="ยกเลิก" color="primary" @click="cancel" />
        <q-btn label="ยืนยัน" color="negative" @click="confirm" />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script>
import router from "../router";
export default {
  data() {
    return {
      show: false,
    };
  },
  methods: {
    cancel() {
      this.show = false;
    },
    confirm() {
      router.push({ name: "login" });
      this.show = false; // Close the dialog
    },
  },
};
</script>
<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: "customfont";
}
.q-dialog {
  width: 500px;
}
</style>
