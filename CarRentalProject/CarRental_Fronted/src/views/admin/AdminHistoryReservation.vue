<template>
    <q-layout view="hHh lpR fFf">
      <Navbar v-if="userInfo.user_type === 'ADMIN'"></Navbar>
      <body v-if="userInfo.user_type === 'ADMIN'">
        <q-page-container>
          <div class="background">
            <div>
              <router-link :to="{ name: 'adminreservation' }">
                <q-btn class="backtomenu" icon="arrow_back" />
              </router-link>
            </div>
  
            <div class="background-container">
              <div class="block-background" style="width: 35%; height: 60%">
                <h6>ดูประวัติ</h6>
                <h4>รายการเช่ารถทั้งหมด</h4>
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
              row-key="reservation_id"
              :filter="filter"
              no-data-label="ไม่มีข้อมูลประวัติรายการเช่ารถ"
            >
              <template #body-cell-Carimage="props">
                <img
                  v-if="props.row.image_path"
                  :src="props.row.image_path"
                  alt="Car Image"
                  style="
                    width: 160px;
                    height: 120px;
                    display: block;
                    margin: 0 auto;
                  "
                />
              </template>
            </q-table>
          </div>
        </q-page-container>
      </body>
      <div v-else>
        <p>Page Not Found</p>
      </div>
    </q-layout>
  </template>
  
  <script>
  import Navbar from "../../components/EmployeeHeader.vue";
  import axios from "axios";
  import CryptoJS from "crypto-js";
  import HistoryReservationService from "../../service/HistoryReservation.js";
  import { ref } from "vue";
  import router from "../../router";
  
  const rows = ref([]);
  const columns = ref([
    //   {
    //     name: "ID",
    //     align: "left",
    //     label: "ID",
    //     field: "reservation_id",
    //     sortable: true,
    //   },
    {
      name: "StarDate",
      align: "left",
      label: "วันที่เริ่มเช่า",
      field: (row) => formatDate(row.start_date),
      sortable: true,
    },
    {
      name: "EndDate",
      align: "left",
      label: "วันที่สิ้นสุดการเช่า",
      field: (row) => formatDate(row.end_date),
      sortable: true,
    },
    {
      name: "Firstname",
      align: "left",
      label: "ชื่อจริง",
      field: (row) => row.user_firstname,
      sortable: true,
    },
    {
      name: "Lastname",
      align: "left",
      label: "นามสกุล",
      field: (row) => row.user_lastname,
      sortable: true,
    },
    {
      name: "Phonenumber",
      align: "left",
      label: "เบอร์โทรศัพท์",
      field: (row) => row.user_phone,
      sortable: true,
    },
    {
      name: "Car",
      align: "left",
      label: "ชื่อรถ",
      field: (row) => row.car_name,
      sortable: true,
    },
    {
      name: "Platenumber",
      align: "left",
      label: "เลขทะเบียน",
      field: (row) => row.car_platenumber,
      sortable: true,
    },
    // {
    //   name: "Price/Day",
    //   align: "left",
    //   label: "Price/Day",
    //   field: (row) => row.car.price_per_day,
    //   sortable: true,
    // },
    {
      name: "CarType",
      align: "left",
      label: "ประเภท",
      field: (row) => row.car_typemodel,
      sortable: true,
    },
    // {
    //   name: "TotalCost",
    //   align: "left",
    //   label: "ราคาเช่า(บาท)/วัน",
    //   field: (row) => row.price_per_day,
    //   sortable: true,
    // },
    {
      name: "TotalCost",
      align: "left",
      label: "ราคาเช่ารวมทั้งหมด(บาท)",
      field: (row) => row.total_cost,
      sortable: true,
    },
    {
      name: "Carimage",
      align: "center",
      label: "รูปรถเช่า",
      field: (row) => row.image_path,
    },
    { name: "action", align: "center", field: "reservation_id" },
  ]);
  const decrypt = (encryptedUrl) => {
    console.log(encryptedUrl);
    const decryptData = CryptoJS.AES.decrypt(encryptedUrl, "123#$%").toString(
      CryptoJS.enc.Utf8
    );
    return decryptData;
  };
  const fetchData = () => {
    HistoryReservationService.getHistoryReservation().then((response) => {
      const decryptedRows = response.data.map((row) => {
        return {
          ...row,
          image_path: decrypt(row.image_path),
        };
      });
      rows.value = decryptedRows;
    });
  };
  const formatDate = (dateString) => {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    return `${day}/${month}/${year}`;
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
        secret: "123#$%",
      };
    },
  
    data() {
      return {
        pagination: {
          sortBy: "name",
        },
        dialog: false,
        dialogHistory: false,
        searchText: "",
        dialogMessage: "",
        eidChangeRoles: "",
      };
    },
    components: {
      Navbar,
    },
    methods: {
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
    background-image: url("../../assets/image/hrleavebackground.png");
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
  