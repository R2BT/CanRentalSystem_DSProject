<template>
  <q-layout view="hHh lpR fFf">
    <Navbar v-if="userInfo.user_type === 'ADMIN'"></Navbar>
    <body v-if="userInfo.user_type === 'ADMIN'">
      <q-page-container>
        <div class="background">
          <div>
            <router-link :to="{ name: 'adminpanel' }">
              <q-btn class="backtomenu" icon="arrow_back" />
            </router-link>
          </div>

          <div class="background-container">
            <div class="block-background" style="width: 35%; height: 60%">
              <h6>จัดการข้อมูล</h6>
              <h2>ข้อมูลลูกค้า</h2>
            </div>
          </div>
        </div>
        <div class="q-pa-md">
          <div class="search-bar">
            <q-input
              v-model="filter"
              placeholder="ค้นหา"
              outlined
              dense
              class="search-input"
              style="background-color: white"
            />
          </div>
          <q-table
            class="my-sticky-header-table"
            flat
            bordered
            :rows="rows"
            :columns="columns"
            row-key="user_id"
            :filter="filter"
            :rows-per-page-options="[10]"
            no-data-label="ไม่มีข้อมูลลูกค้า"
          >
            <template v-slot:body-cell-action="props">
              <q-td :props="props">
                <q-btn
                  icon="delete"
                  color="negative"
                  @click="
                    reservationAlert(
                      props.row.user_id,
                      props.row.user_firstname,
                      props.row.user_surname,
                      props.row.user_phonenumber
                    )
                  "
                ></q-btn>
              </q-td>
            </template>
          </q-table>
        </div>
      </q-page-container>
    </body>
    <div v-else>
      <p>Page Not Found</p>
    </div>
  </q-layout>
  <q-dialog v-model="dialog" persistent>
    <q-card style="padding: 30px">
      <q-card-section>
        <q-card-title
          class="text-h6 start-container text-center"
          style="margin-bottom: 0"
        >
          <div style="font-size: 18px font-weight: bold; margin-bottom: 10px;">
            {{ dialogMessage }}<br />
          </div>
        </q-card-title>
        <q-card-title class="text-h6 start-container" style="margin-bottom: 0">
          <div style="font-size: 13px">
            {{ name }}<br />
            {{ phoneNumber }}<br />
          </div>
        </q-card-title>
        <q-card-title class="text-h6 close-container" style="margin-bottom: 0">
          <div style="font-size: 13px; color: red"></div>
        </q-card-title>
      </q-card-section>
      <q-card-actions align="center" style="padding-top: 10px">
        <q-btn label="ยกเลิก" color="negative" @click="dialog = false" />
        <q-btn label="ยืนยัน" color="positive" @click="confirmDeleteUser" />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script>
import Navbar from "../../components/EmployeeHeader.vue";
import axios from "axios";
import UserService from "../../service/Userservice.js";
import { ref } from "vue";
import router from "../../router";

const rows = ref([]);
const columns = ref([
  //   { name: "ID", align: "left", label: "ID", field: "user_id", sortable: true },
  {
    name: "Firstname",
    align: "left",
    label: "ชื่อจริง",
    field: "user_firstname",
    sortable: true,
  },
  {
    name: "Lastname",
    align: "left",
    label: "นามสกุล",
    field: "user_surname",
    sortable: true,
  },
  {
    name: "Phonenumber",
    align: "left",
    label: "เบอร์โทรศัพท์",
    field: "user_phonenumber",
    sortable: true,
  },
  { name: "action", align: "center", field: "user_id" },
]);
const fetchData = () => {
  UserService.getUser().then((response) => {
    // Filter out rows where user_type is 'ADMIN'
    const filteredRows = response.data.filter((row) => row.user_type !== 'ADMIN');
    rows.value = filteredRows;
  });
};


export default {
  setup() {
    fetchData();
    const myItem = localStorage.getItem("user-info");
    const userInfo = JSON.parse(myItem);
    return {
      userInfo,
      filter: ref(""),
      columns,
      rows,
    };
  },

  data() {
    return {
      pagination: {
        sortBy: "name",
      },
      dialog: false,
      searchText: "",
      dialogMessage: "",
      eidChangeRoles: "",
    };
  },
  components: {
    Navbar,
  },
  methods: {
    reservationAlert(id, firstname, lastname, phonenumber) {
      this.dialogMessage = "ยืนยันการลบบัญชีลูกค้า ";
      this.name = `ชื่อ : ${firstname} ${lastname}`;
      this.phoneNumber = `เบอร์โทรศัพท์ : ${phonenumber}`;
      this.dialog = true;
      this.actionColor = "negative";
      this.uidToDelete = id;
    },

    deleteUser(id) {
      var myHeaders = new Headers();
      myHeaders.append("Content-Type", "application/json");

      var raw = {
        user_id: id,
      };

      var requestOptions = {
        method: "DELETE",
        headers: myHeaders,
        body: raw,
        redirect: "follow",
      };

      fetch("http://localhost:8081/Car_rental_backend/users/" + id, requestOptions)
        .then((response) => response.json())
        .then((result) => {
            window.location.reload()
        })
        .catch(window.location.reload());
    },

    confirmDeleteUser() {
      this.dialog = false;
      this.deleteUser(this.uidToDelete);
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
.my-sticky-header-table::v-deep .q-table__top,
.my-sticky-header-table::v-deep .q-table__bottom,
.my-sticky-header-table::v-deep thead tr:first-child th {
  background-color: #222222;
  color: white;
}

.my-sticky-header-table::v-deep thead tr th {
  position: sticky;
  z-index: 1;
}

.my-sticky-header-table::v-deep thead tr:first-child th {
  top: 0;
}

.my-sticky-header-table::v-deep.q-table--loading thead tr:last-child th {
  top: 48px;
}

.my-sticky-header-table {
  scroll-margin-top: 48px;
  height: 470px;
  width: 100%;
}
</style>
