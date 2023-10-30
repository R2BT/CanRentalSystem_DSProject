<template>
  <q-layout view="hHh lpR fFf">
    <Navbar v-if="userInfo.user_type === 'USER'"></Navbar>
    <body v-if="userInfo.user_type === 'USER'">
      <q-page-container>
        <div class="clock-container">
          <div class="clock">
            <q-table
              class="my-sticky-header-table"
              flat
              bordered
              title="รายการเช่ารถของฉัน"
              :rows="rows"
              :columns="columns"
              :filter="filter"
              row-key="reservation_id"
              :rows-per-page-options="[10]"
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
              <template #body-cell-action="props">
                <q-btn
                  icon="edit"
                  color="orange"
                  @click="editCar(props.row.car_id)"
                />
                <q-btn
                  icon="delete"
                  color="negative"
                  @click="deleteCar(props.row.car_id)"
                />
              </template>
              <template v-slot:top-right>
                <q-input
                  bg-color="white"
                  borderless
                  dense
                  debounce="300"
                  v-model="filter"
                  placeholder="  Search..."
                >
                  <template v-slot:append> </template>
                </q-input>
              </template>
            </q-table>
          </div>
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
import CryptoJS from "crypto-js";
import { ref } from "vue";
import axios from "axios";

const columns = ref([
  // {
  //   name: "ID",
  //   align: "left",
  //   label: "ID",
  //   field: "reservation_id",
  //   sortable: true,
  // },
  // {
  //   name: "Firstname",
  //   align: "left",
  //   label: "Firstname",
  //   field: (row) => row.user.user_firstname,
  //   sortable: true,
  // },
  // {
  //   name: "Lastname",
  //   align: "left",
  //   label: "Lastname",
  //   field: (row) => row.user.user_surname,
  //   sortable: true,
  // },
  // {
  //   name: "Phonenumber",
  //   align: "left",
  //   label: "Phonenumber",
  //   field: (row) => row.user.user_phonenumber,
  //   sortable: true,
  // },
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
    name: "Car",
    align: "left",
    label: "ชื่อรถ",
    field: (row) => row.car.car_name,
    sortable: true,
  },
  {
    name: "Platenumber",
    align: "left",
    label: "เลขทะเบียน",
    field: (row) => row.car.plat_number,
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
    field: (row) => row.car.car_type.type_model,
    sortable: true,
  },
  {
    name: "Costperday",
    align: "left",
    label: "ราคาเช่า/วัน (บาท)",
    field: (row) => row.cost_per_day,
    sortable: true,
  },
  {
    name: "TotalCost",
    align: "left",
    label: "รวมราคาเช่าทั้งหมด(บาท)",
    field: (row) => row.total_cost,
    sortable: true,
  },
  {
    name: "Carimage",
    align: "center",
    label: "รูปรถ",
    field: (row) => row.car.image_path,
  },
  // { name: 'action', align: 'center', field: 'reservation_id'  }
]);
const rows = ref([]);
const decrypt = (encryptedUrl) => {
  console.log(encryptedUrl);
  const decryptData = CryptoJS.AES.decrypt(encryptedUrl, "123#$%").toString(
    CryptoJS.enc.Utf8
  );
  return decryptData;
};
const fetchData = () => {
  const myItem = localStorage.getItem("user-info");
  const userInfo = JSON.parse(myItem);

  axios
    .get(
      "http://localhost:8081/Car_rental_backend/users/" +
        userInfo.user_id +
        "/reservations"
    )
    .then((response) => {
      const decryptedRows = response.data.map((row) => {
        return {
          ...row,
          image_path: decrypt(row.car.image_path),
        };
      });
      rows.value = decryptedRows;
    });
};

// fetchData();

const myItem = localStorage.getItem("user-info");

// if (myItem !== null) {
//   console.log(myItem);
// } else {
//   console.log("Item not found");
// }
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
      filter: ref(""),
      columns,
      rows,
      userInfo,
    };
  },
  components: {
    Navbar,
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
  background-image: url("../../assets/image/background3.svg");
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
  padding: 1.2rem;
  background: #ffffff22;
  border-radius: 1.4rem;
  box-shadow: 0 0 1.2rem rgba(0, 0, 0, 0.35);
  color: white;
  width: 1250px;
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
  height: 700px;
  width: 1200px;
}

.backButton {
  display: grid;
  flex-direction: column;
  justify-content: left;
  font-size: 1.5rem;
  margin-bottom: 10px;
}
</style>
