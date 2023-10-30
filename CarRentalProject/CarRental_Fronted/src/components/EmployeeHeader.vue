<template>
  <q-header elevated class="custom-navbar" height-hint="98">
    <q-toolbar>
      <q-btn dense flat round icon="menu" @click="toggleRightDrawer" />
      <q-toolbar-title> CAR RENTAL Project </q-toolbar-title>
      <!-- <q-tabs>
          <q-route-tab to="/sendleave" label="About Us" />
        </q-tabs> -->
    </q-toolbar>
  </q-header>

  <q-drawer
    v-model="rightDrawerOpen"
    show-if-above
    :width="300"
    :breakpoint="400"
    class="custom-drawer"
  >
    <q-scroll-area
      style="height: calc(100% - 150px); margin-top: 150px"
      active-class="active-link"
    >
      <q-list padding>
        <router-link
          :to="{ name: 'rentcar' }"
          class="custom-link"
          v-if="userInfo.user_type !== 'ADMIN'"
        >
          <q-item clickable v-ripple>
            <q-item-section avatar>
              <q-icon name="car_rental" />
            </q-item-section>
            <q-item-section> เช่ารถ </q-item-section>
          </q-item>
        </router-link>
        <router-link
          :to="{ name: 'viewreservation' }"
          class="custom-link"
          v-if="userInfo.user_type !== 'ADMIN'"
        >
          <q-item clickable v-ripple>
            <q-item-section avatar>
              <q-icon name="bookmark_added" />
            </q-item-section>
            <q-item-section> รายการเช่ารถของฉัน </q-item-section>
          </q-item>
        </router-link>
        <router-link
          :to="{ name: 'viewcars' }"
          class="custom-link"
          v-if="userInfo.user_type !== 'ADMIN'"
        >
          <q-item clickable v-ripple>
            <q-item-section avatar>
              <q-icon name="view_list" />
            </q-item-section>
            <q-item-section> รถเช่าที่ให้บริการในร้าน </q-item-section>
          </q-item>
        </router-link>
        <router-link
          :to="{ name: 'adminpanel' }"
          class="custom-link"
          v-if="userInfo.user_type !== 'USER'"
        >
          <q-item clickable v-ripple>
            <q-item-section avatar>
              <q-icon name="star" />
            </q-item-section>
            <q-item-section> Admin Panel</q-item-section>
          </q-item>
        </router-link>
        <q-expansion-item icon="person" label="แก้ไขโพร์ไฟล์">
          <q-card class="bg-grey-9">
            <q-card-section>
              <div
                class="custom-link"
                style="margin-bottom: 10px"
                @click="showEditDialog"
                v-if="userInfo.user_type !== 'ADMIN'"
              >
                <q-item clickable v-ripple>
                  <q-item-section avatar>
                    <q-icon name="edit" />
                  </q-item-section>
                  <q-item-section> แก้ไขข้อมูลส่วนตัว</q-item-section>
                </q-item>
                <EditProfile ref="EditProfile" />
              </div>

              <div
                class="custom-link"
                @click="showChangePasswordDialog"
                v-if="userInfo.user_type !== 'ADMIN'"
              >
                <q-item clickable v-ripple>
                  <q-item-section avatar>
                    <q-icon name="key" />
                  </q-item-section>
                  <q-item-section> เปลี่ยนรหัสผ่าน</q-item-section>
                </q-item>

                <ChangePassword ref="ChangePassword"></ChangePassword>
              </div>
            </q-card-section>
          </q-card>
        </q-expansion-item>

        <div class="custom-link" @click="showLogoutDialog">
          <q-item clickable v-ripple>
            <q-item-section avatar>
              <q-icon name="power_settings_new" />
            </q-item-section>
            <q-item-section> Logout </q-item-section>
          </q-item>
          <logout-dialog ref="logoutDialog" />
        </div>
      </q-list>
    </q-scroll-area>

    <q-img
      class="absolute-top"
      style="
        height: 150px;
        background-image: linear-gradient(
          90deg,
          rgba(2, 0, 36, 1) 0%,
          rgba(216, 139, 55, 1) 0%,
          rgba(255, 249, 0, 1) 100%
        );
      "
    >
      <div class="absolute-bottom bg-transparent">
        <q-avatar
          size="56px"
          class="q-mb-sm"
          v-if="userInfo.user_type === 'ADMIN'"
        >
          <img src="../assets/image/person.png" />
        </q-avatar>
        <q-avatar
          size="56px"
          class="q-mb-sm"
          v-if="userInfo.user_type === 'USER'"
        >
          <img src="../assets/image/man.png" />
        </q-avatar>
        <div>
          <div v-if="userInfo.user_type !== 'ADMIN'">ข้อมูลผู้เช่า</div>
          <div v-else>{{ userInfo.user_type }}</div>
        </div>
        <div class="text-weight-bold" v-if="userInfo.user_type !== 'ADMIN'">
          {{ userInfo.user_firstname }} {{ userInfo.user_surname }}
        </div>
        <div>
          <div v-if="userInfo.user_type !== 'ADMIN'">
            Tel : {{ userInfo.user_phonenumber }}
          </div>
        </div>
      </div>
    </q-img>
  </q-drawer>
</template>

<script>
import { ref } from "vue";
import LogoutDialog from "../components/LogoutDialog.vue";
import EditProfile from "../components/EditProfile.vue";
import ChangePassword from "../components/ChangePassword.vue";
export default {
  components: {
    LogoutDialog,
    EditProfile,
    ChangePassword,
  },
  methods: {
    showLogoutDialog() {
      this.$refs.logoutDialog.show = true;
    },
    showEditDialog() {
      this.$refs.EditProfile.show = true;
    },
    showChangePasswordDialog() {
      this.$refs.ChangePassword.showChangePassword = true;
    },
  },
  setup() {
    const rightDrawerOpen = ref(false);
    // Check if the item exists in localStorage
    const myItem = localStorage.getItem("user-info");
    const userInfo = JSON.parse(myItem);
    return {
      userInfo,
      rightDrawerOpen,
      toggleRightDrawer() {
        rightDrawerOpen.value = !rightDrawerOpen.value;
      },
    };
  },
};
</script>

<style>
.custom-navbar {
  background-color: #181818;
  color: white;
}

.custom-drawer {
  background-color: #0b0b0b;
  color: #ffffff;
}
.custom-link {
  text-decoration: none;
  color: #ffffff;
}
</style>
