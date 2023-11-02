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
              title="
รถเช่าที่ให้บริการในร้าน"
              :rows="rows"
              :columns="columns"
              :filter="filter"
              row-key="car_id"
              :rows-per-page-options="[10]"
            >
              <template v-slot:body-cell-Carimage="props">
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
import { ref } from "vue";
import CarService from "../../service/Carservice";
import axios from "axios";
import CryptoJS from "crypto-js";
const columns = ref([
  // { name: "ID", align: "left", label: "ID", field: "car_id", sortable: true },
  {
    name: "Carname",
    align: "left",
    label: "ชื่อรถ",
    field: "car_name",
    sortable: true,
  },
  {
    name: "Description",
    align: "left",
    label: "รายละเอียด",
    field: "description",
    sortable: true,
  },
  {
    name: "Price/Day",
    align: "left",
    label: "ราคาเช่า/วัน (บาท)",
    field: "price_per_day",
    sortable: true,
  },
  {
    name: "Color",
    align: "left",
    label: "สี",
    field: "color",
    sortable: true,
  },
  {
    name: "Platenumber",
    align: "left",
    label: "เลขทะเบียน",
    field: "plat_number",
    sortable: true,
  },
  {
    name: "Typemodel",
    align: "left",
    label: "ประเภท",
    field: (row) => row.car_type.type_model,
    sortable: true,
  },
  {
    name: "Carimage",
    align: "center",
    label: "รูปรถ",
    field: (row) => row.image_path,
    sortable: true,
  },
]);
const rows = ref([]);
const decrypt = (encryptedUrl) => {
  const decryptData = CryptoJS.AES.decrypt(encryptedUrl, "123#$%").toString(
    CryptoJS.enc.Utf8
  );
  return decryptData;
};
const fetchData = () => {
  CarService.getCar().then((response) => {
    const decryptedRows = response.data.map((row) => {
      return {
        ...row,
        image_path: decrypt(row.image_path),
      };
    });
    rows.value = decryptedRows;
  });
};

fetchData();

const myItem = localStorage.getItem("user-info");

// if (myItem !== null) {
//   console.log(myItem);
// } else {
//   console.log("Item not found");
// }

const deleteCar = (id) => {
  var myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");

  var raw = {
    car_id: id,
  };

  var requestOptions = {
    method: "DELETE",
    headers: myHeaders,
    body: raw,
    redirect: "follow",
  };

  fetch("http://localhost:8081/Car_rental_backend/cars/" + id, requestOptions)
    .then((response) => response.json())
    .then((result) => {
      alert("CarID: " + id + " (Delete Success)");
      fetchData();
    })
    .catch((error) => console.log("error", error));
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
  font-family: "customfont";
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
