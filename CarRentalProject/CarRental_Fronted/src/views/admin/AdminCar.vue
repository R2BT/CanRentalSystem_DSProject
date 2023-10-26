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
            <div class="block-background" style="width: 40%; height: 60%">
              <h6>จัดการข้อมูล</h6>
              <h2>ข้อมูลรถเช่า</h2>
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

            <q-btn
              label="เพิ่มรถเช่า"
              color="positive"
              icon-right="add"
              @click="AddcarModal"
              class="add-button"
            />
          </div>
          <q-table
            flat
            bordered
            :rows="rows"
            :columns="columns"
            row-key="car_id"
            :filter="filter"
          >
            <template v-slot:body-cell-action="props">
              <q-td :props="props">
                <q-btn
                  style="margin-right: 5px"
                  icon="edit"
                  color="orange"
                  @click="
                    editCarAlert(
                      props.row.car_id,
                      props.row.description,
                      props.row.car_name,
                      props.row.price_per_day,
                      props.row.color,
                      props.row.plat_number,
                      props.row.car_type
                    )
                  "
                />
                <q-btn
                  icon="delete"
                  color="negative"
                  @click="
                    deletecarAlert(
                      props.row.car_id,
                      props.row.car_name,
                      props.row.price_per_day,
                      props.row.color,
                      props.row.plat_number,
                      props.row.car_type.type_model
                      
                    )
                  "
                />
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
    <q-card style="padding: 30px; width: 20%">
      <q-card-section>
        <q-card-title
          class="text-h6 start-container text-center"
          style="margin-bottom: 0"
        >
          <div style="font-size: 18px; font-weight: bold; margin-bottom: 10px">
            {{ dialogMessage }}<br />
          </div>
        </q-card-title>
        <q-card-title class="text-h6 start-container" style="margin-bottom: 0">
          <div style="font-size: 13px">
            {{ nameCarInfo }}<br />
            {{ typeCarInfo }}<br />
            {{ platNumberInfo }}<br />
            {{ colorShow }}<br />
            ราคาเช่าต่อวัน :  {{ rentPriceInfo }} บาท<br /> 
          </div>
        </q-card-title>
        <q-card-title class="text-h6 close-container" style="margin-bottom: 0">
          <div style="font-size: 13px; color: red"></div>
        </q-card-title>
      </q-card-section>
      <q-card-actions align="center" style="padding-top: 10px">
        <q-btn label="ยกเลิก" color="negative" @click="dialog = false" />
        <q-btn label="ยืนยัน" color="positive" @click="confirmDeleteCar" />
      </q-card-actions>
    </q-card>
  </q-dialog>
  <q-dialog v-model="dialogAddcar" persistent>
    <q-card style="padding: 30px; width: 100%">
      <q-card-section>
        <q-card-title
          class="text-h6 start-container text-center"
          style="margin-bottom: 0"
        >
          <div style="font-size: 18px font-weight: bold; margin-bottom: 10px;">
            เพิ่มรถเช่าในระบบ<br />
          </div>
        </q-card-title>
        <q-card-title class="text-h6 start-container" style="margin-bottom: 0">
          <div style="font-size: 13px">
            <q-form @submit="onSubmit" class="q-gutter-md">
              <q-input v-model="carname" label="ชื่อรถ" required />
              <q-input v-model="description" label="รายละเอียด" required />
              <q-input
                v-model="pricePerday"
                label="ราคาเช่าต่อวัน"
                type="number"
                step="0.01"
                required
              />
              <q-input v-model="color" label="สี" required />
              <q-input
                v-model="platNumber"
                label="เลขทะเบียน"
                maxlength="6"
                required
              />
              <q-select
                v-model="selectedCarType"
                :options="carType"
                label="ประเภทของรถ"
                placeholder="เลือกประเภทรถ"
                emit-value
                map-options
                option-label="type_model"
                required
                :rules="[(value) => !!value || 'กรุณาเลือกประเภทของรถ']"
              >
                <template v-slot:item="props">
                  <q-item-label>{{ props.item.type_id }}</q-item-label>
                </template>
              </q-select>
              <q-card-actions align="center" style="padding-top: 10px">
                <q-btn
                  label="ยกเลิก"
                  color="negative"
                  @click="dialogAddcar = false"
                />
                <q-btn
                  label="ยืนยัน"
                  type="submit"
                  color="green"
                  @click="confirmAddCar"
                />
              </q-card-actions>
            </q-form>
          </div>
        </q-card-title>
        <q-card-title class="text-h6 close-container" style="margin-bottom: 0">
          <div style="font-size: 13px; color: red"></div>
        </q-card-title>
      </q-card-section>
    </q-card>
  </q-dialog>
  <q-dialog v-model="dialogEditcar" persistent>
    <q-card style="padding: 30px; width: 100%">
      <q-card-section>
        <q-card-title
          class="text-h6 start-container text-center"
          style="margin-bottom: 0"
        >
          <div style="font-size: 18px font-weight: bold; margin-bottom: 10px;">
            แก้ไขรถเช่าในระบบ<br />
          </div>
        </q-card-title>
        <q-card-title class="text-h6 start-container" style="margin-bottom: 0">
          <div style="font-size: 13px">
            <q-form @submit="onSubmit" class="q-gutter-md">
              <q-input v-model="carname" label="ชื่อรถ" required />
              <q-input v-model="description" label="รายละเอียด" required />
              <q-input
                v-model="pricePerday"
                label="ราคาเช่าต่อวัน"
                type="number"
                step="0.01"
                required
              />
              <q-input v-model="color" label="สี" required />
              <q-input
                v-model="platNumber"
                label="เลขทะเบียน"
                maxlength="6"
                required
              />
              <q-select
                v-model="selectedCarType"
                :options="carType"
                label="ประเภทของรถ"
                placeholder="เลือกประเภทรถ"
                emit-value
                map-options
                option-label="type_model"
                required
                :rules="[(value) => !!value || 'กรุณาเลือกประเภทของรถ']"
              >
                <template v-slot:item="props">
                  <q-item-label>{{ props.item.type_id }}</q-item-label>
                </template>
              </q-select>
              <q-card-actions align="center" style="padding-top: 10px">
                <q-btn
                  label="ยกเลิก"
                  color="negative"
                  @click="dialogEditcar = false"
                />
                <q-btn
                  label="ยืนยัน"
                  type="submit"
                  color="green"
                  @click="confirmEditCar"
                />
              </q-card-actions>
            </q-form>
          </div>
        </q-card-title>
        <q-card-title class="text-h6 close-container" style="margin-bottom: 0">
          <div style="font-size: 13px; color: red"></div>
        </q-card-title>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script>
import Navbar from "../../components/EmployeeHeader.vue";
import CarService from "../../service/Carservice.js";
import CartypeService from "../../service/Cartypeservice.js";
import { ref } from "vue";
import router from "../../router";
const carname = ref("");
const description = ref("");
const pricePerday = ref("");
const color = ref("");
const platNumber = ref("");
const carType = ref([]);
const selectedCarType = ref(null);
const carTypeId = ref("");
const rows = ref([]);
const columns = ref([
  // {
  //   name: 'name',
  //   required: true,
  //   label: 'Dessert (100g serving)',
  //   align: 'left',
  //   field: row => row.name,
  //   sortable: true
  // },
  // { name: 'ID', align: 'left', label: 'ID', field: 'car_id' , sortable: true },
  {
    name: "Carname",
    align: "left",
    label: "Carname",
    field: "car_name",
    sortable: true,
  },
  {
    name: "Description",
    align: "left",
    label: "Description",
    field: "description",
    sortable: true,
  },
  {
    name: "Price/Day",
    align: "left",
    label: "Price/Day",
    field: "price_per_day",
    sortable: true,
  },
  {
    name: "Color",
    align: "left",
    label: "Color",
    field: "color",
    sortable: true,
  },
  {
    name: "Platenumber",
    align: "left",
    label: "Platenumber",
    field: "plat_number",
    sortable: true,
  },
  {
    name: "Typemodel",
    align: "left",
    label: "Typemodel",
    field: (row) => row.car_type.type_model,
    sortable: true,
  },
  { name: "action", align: "center", field: "car_id" },
]);

const fetchData = () => {
  CarService.getCar().then((response) => {
    rows.value = response.data;
    //  console.log(rows.value[0].car_type.type_model)
  });
};
const fetchCarTypes = async () => {
  try {
    const response = await CartypeService.getCar();
    carType.value = response.data;
  } catch (error) {
    console.error(error);
  }
};

export default {
  setup() {
    fetchData();
    fetchCarTypes();
    carTypeId.value = selectedCarType.value;
    console.log(carTypeId.value)
    const myItem = localStorage.getItem("user-info");
    const userInfo = JSON.parse(myItem);
    return {
      userInfo,
      filter: ref(""),
      columns,
      rows,
      carType,
      selectedCarType,
      carTypeId,
      carname,
      color,
      platNumber,
      pricePerday,
      description,
    };
  },

  data() {
    return {
      pagination: {
        sortBy: "name",
      },
      dialog: false,
      dialogAddcar: false,
      dialogEditcar: false,
      searchText: "",
      dialogMessage: "",
      eidChangeRoles: "",
    };
  },
  components: {
    Navbar,
  },
  methods: {

    AddcarModal() {
      carname.value = "";
      description.value = "";
      pricePerday.value = "";
      color.value = "";
      platNumber.value = "";
      selectedCarType.value = null;
      this.dialogAddcar = true;
    },

    deletecarAlert(id, carName, price, color, plateNum, type) {
      this.dialogMessage = "ยืนยันการลบรถเช่า ";
      this.nameCarInfo = `ชื่อรถ: ${carName}`;
      this.typeCarInfo = `ประเภท: ${type}`;
      this.platNumberInfo = `หมายเลขทะเบียน: ${plateNum}`;
      this.colorShow = `สี: ${color}`;
      this.rentPriceInfo = price;
      this.dialog = true;
      this.cidToDelete = id;
    },
    editCarAlert(id, description, carName, price, color, plateNum, type) {
      this.carname = carName;
      this.description = description;
      this.pricePerday = price;
      this.color = color;
      this.platNumber = plateNum;
      this.selectedCarType = type;
      this.dialogEditcar = true;
      this.cidToEdit = id;
    },
    confirmAddCar() {
      const carData = {
        car_name: carname.value,
        description: description.value,
        price_per_day: pricePerday.value,
        color: color.value,
        plat_number: platNumber.value,
      };
      carTypeId.value = selectedCarType.value;
      const requestOptions = {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(carData),
      };

      fetch(
        `http://localhost:8081/Car_rental_backend/cars/` + carTypeId.value.type_id,
        requestOptions
      )
        .then((response) => response.text())
        .then((result) => {
          window.location.reload();
        })
        .catch(window.location.reload());
    },
    deleteCar(id) {
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
          window.location.reload();
        })
        .catch(window.location.reload());
    },
    editCar(id) {
      
        console.log("work");
      var myHeaders = new Headers();

      const carData = {
        car_name: carname.value,
        description: description.value,
        price_per_day: pricePerday.value,
        color: color.value,
        plat_number: platNumber.value,
      };
      carTypeId.value = selectedCarType.value;
      console.log(carTypeId.value.type_id);
      console.log(id);
      myHeaders.append("Content-Type", "application/json");
      const requestOptions = {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(carData),
      };

      fetch(
        "http://localhost:8081/Car_rental_backend/cars/" +
          id +
          "/type/" +
          carTypeId.value.type_id,
        requestOptions
      )
        .then((response) => response.text())
        .then((result) => {
         
          window.location.reload();
        })
        .catch((error) => console.log("error", error));
    },
    confirmDeleteCar() {
      this.dialog = false;
      this.deleteCar(this.cidToDelete);
    },
    confirmEditCar() {
      this.dialogEditcar = false;
      this.editCar(this.cidToEdit);
    },
    //   onEdit(eid) {
    //     router.push("/update/" + eid);
    //   },
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
  background-image: url("../../assets/image/background.jpg");
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
.topicModal{
  font-weight: bold;
}
</style>
