<template>
  <q-layout view="hHh lpR fFf">
    <Navbar v-if="userInfo.user_type === 'USER'"></Navbar>
    <body v-if="userInfo.user_type === 'USER'">
      <q-page-container>
        <div class="clock-container">
          <div class="clock">
            <div class="date">{{ currentDate }}</div>
            <div class="time">{{ currentTime }}</div>
            <div class="inputChoice" style="margin-left: 270px">
              <q-form @submit="onSubmit" ref="form">
                <div class="row justify-between">
                  <div class="col" style="padding-top: 30px; width: 500px">
                    <div class="row justify-between">
                      <div class="col-6">
                        <q-input
                          color="purple"
                          bg-color="white"
                          filled
                          type="date"
                          v-model="rentStartdate"
                          label="เลือกวันที่เริ่มการเช่ารถ"
                          lazy-rules
                          :rules="[
                            (val) =>
                              /^(19|20)\d{2}-\d{2}-\d{2}$/.test(val) ||
                              'กรุณาเลือกวันที่เริ่มเช่ารถ',
                            validateDateStart,
                          ]"
                        ></q-input>
                      </div>
                      <div class="col-6">
                        <q-input
                          color="purple"
                          bg-color="white"
                          filled
                          type="date"
                          v-model="rentEnddate"
                          label="เลือกวันที่สิ้นสุดการเช่ารถ"
                          lazy-rules
                          :rules="[
                            (val) =>
                              /^(19|20)\d{2}-\d{2}-\d{2}$/.test(val) ||
                              'กรุณาเลือกวันที่สิ้นสุดการเช่ารถ',
                            validateDateRange,
                          ]"
                        ></q-input>
                      </div>
                    </div>
                  </div>

                  <q-btn
                    unelevated
                    rounded
                    color="positive"
                    icon="search"
                    class="q-ma-sm"
                    type="submit"
                    style="width: 50px; height: 50px; margin-top: 30px"
                  />
                </div>
              </q-form>
            </div>

            <div class="col" style="padding-top: 35px">
              <q-table
                class="my-sticky-header-table"
                flat
                bordered
                title="รถที่สามารถเช่าได้"
                :rows="rows"
                :columns="columns"
                :filter="filter"
                row-key="car_id"
                no-data-label="กรุณาเลือกวันที่เริ่มเช่าและวันที่สิ้นสุดการเช่า"
              >
                <template #body-cell-Carimage="props">
                  <div style="margin-top: 30px">
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
                  </div>
                </template>
                <template #body-cell-action="props">
                  <div style="margin-right: 37%">
                    <q-btn
                      label="เช่า"
                      color="orange"
                      style="
                        width: 100px;
                        height: 20px;
                        margin-right: 20px;
                        margin-top: 5px;
                        margin-left: 30%;

                        margin-bottom: 20px;
                      "
                      @click="
                        rentCarAlert(
                          props.row.car_id,
                          props.row.car_name,
                          props.row.plat_number,
                          props.row.car_type.type_model,
                          props.row.price_per_day,
                          props.row.image_path
                        )
                      "
                    />
                  </div>

                  <q-dialog v-model="dialog" persistent>
                    <q-card style="padding: 30px">
                      <q-card-section>
                        <q-card-title
                          class="text-h6 start-container text-center"
                          style="margin-bottom: 0"
                        >
                          <div
                            style="
                              font-size: 18px;
                              font-weight: bold;
                              margin-bottom: 10px;
                            "
                          >
                            {{ dialogMessage }}<br />
                            <img
                              :src="carShowimg"
                              alt="Car Image"
                              style="
                                width: 160px;
                                height: 120px;
                                display: block;
                                margin: 0 auto;
                                margin-top: 10px;
                              "
                            /><br />
                          </div>
                        </q-card-title>
                        <q-card-title
                          class="text-h6 start-container"
                          style="margin-bottom: 0"
                        >
                          <div style="font-size: 13px">
                            {{ nameCarInfo }}<br />
                            {{ typeCarInfo }}<br />
                            {{ platNumberInfo }}<br />
                            วันที่เริ่มเช่า-วันที่สิ้นสุดการเช่า:
                            {{ rentStartdate }} - {{ rentEnddate }}<br />
                            ราคาเช่า/วัน: {{ priceRentperday }} บาท<br />
                            จำนวนวันที่เช่า: {{ countDay }} วัน <br />
                            ราคาเช่าทั้งหมด: {{ rentPriceInfo }} บาท<br />
                          </div>
                        </q-card-title>
                        <q-card-title
                          class="text-h6 close-container"
                          style="margin-bottom: 0"
                        >
                          <div style="font-size: 13px; color: red">
                            *หากไม่มารับรถในวันเริ่มที่เริ่มการเช่ารถ
                            รายการจองนี้จะถูกยกเลิกทันที*
                          </div>
                        </q-card-title>
                      </q-card-section>
                      <q-card-actions align="center" style="padding-top: 10px">
                        <q-btn
                          label="ยกเลิก"
                          color="negative"
                          @click="dialog = false"
                        />
                        <q-btn
                          label="ยืนยัน"
                          color="positive"
                          @click="confirmRentCar"
                        />
                      </q-card-actions>
                    </q-card>
                  </q-dialog>
                  <q-dialog v-model="notFoundcar" persistent>
                    <q-card>
                      <q-card-section style="width: 500px; height: 50px">
                        <q-card-main>
                          ขออภัยในความไม่สะดวกเนื่องจากไม่มีรถที่พร้อมให้บริการเช่าในวันที่ระบุ
                          กรุณาเลือกวันที่ใหม่อีกครั้ง
                        </q-card-main>
                      </q-card-section>
                      <q-card-actions align="right">
                        <q-btn
                          label="ยืนยัน"
                          color="negative"
                          @click="notFoundcar = true"
                        />
                      </q-card-actions>
                    </q-card>
                  </q-dialog>
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
        </div>
      </q-page-container>
    </body>
    <div v-else>
      <p>Page Not Found</p>
    </div>
  </q-layout>
</template>

<script>
import { ref, onMounted } from "vue";
import axios from "axios";
import CryptoJS from "crypto-js";
import router from "../../router";
import Navbar from "../../components/EmployeeHeader.vue";
// const carShowimg = ref("");
export default {
  components: {
    Navbar,
  },
  methods: {
    validateDateRange() {
      const startDate = new Date(this.rentStartdate);
      const endDate = new Date(this.rentEnddate);

      if (startDate > endDate) {
        return "วันที่สิ้นสุดการเช่ารถน้อยกว่าหรือเท่ากับวันเริ่มการเช่า";
      }
      return true;
    },
    validateDateStart() {
      const startDate = new Date(this.rentStartdate);
      const currentDate = new Date();
      if (startDate < currentDate) {
        return "วันที่เริ่มเช่ารถต้องไม่น้อยหรือเท่ากับวันที่ปัจจุบัน";
      }
      return true;
    },
    rentCarAlert(id, name, platNumber, type, price, img) {
      const pricePerDay = parseFloat(price);
      const startDate = new Date(this.rentStartdate);
      const endDate = new Date(this.rentEnddate);

      const dayDifference = (endDate - startDate) / (1000 * 60 * 60 * 24) + 1;
      this.rentPriceInfo = pricePerDay * dayDifference;
      this.priceRentperday = pricePerDay;
      this.countDay = dayDifference;
      this.dialogMessage = "ยืนยันรายการเช่ารถ ";
      this.nameCarInfo = `ชื่อรถ: ${name}`;
      this.typeCarInfo = `ประเภท: ${type}`;
      this.platNumberInfo = `หมายเลขทะเบียน: ${platNumber}`;
      this.pricePerdaysend = pricePerDay;
      this.dialog = true;
      this.actionColor = "negative";
      this.carShowimg = img;
      this.eidToDelete = id;
    },
    confirmRentCar() {
      this.dialog = false;
      this.rentCar(this.eidToDelete);
    },
  },
  setup() {
    const myItem = localStorage.getItem("user-info");
    const userInfo = JSON.parse(myItem);
    const currentTime = ref("");
    const currentDate = ref("");
    const rentStartdate = ref(null);
    const rentEnddate = ref(null);
    const pricePerdaysend = ref(null);
    const updateClock = () => {
      const now = new Date();
      const hours = now.getHours();
      const minutes = now.getMinutes().toString().padStart(2, "0");
      const seconds = now.getSeconds().toString().padStart(2, "0");
      const meridiem = hours >= 12 ? "PM" : "AM";
      const twelveHourFormat = hours > 12 ? hours - 12 : hours;
      const time = `${twelveHourFormat}:${minutes}:${seconds} ${meridiem}`;
      const month = now.toLocaleString("default", {
        month: "long",
      });
      const day = now.getDate();
      const year = now.getFullYear();
      const date = `${month} ${day}, ${year}`;

      currentTime.value = time;
      currentDate.value = date;
    };

    onMounted(() => {
      updateClock();
      setInterval(updateClock, 1000);
    });
    const decrypt = (encryptedUrl) => {
      const decryptData = CryptoJS.AES.decrypt(encryptedUrl, "123#$%").toString(
        CryptoJS.enc.Utf8
      );
      return decryptData;
    };
    const onSubmit = async () => {
      if (rentStartdate.value != null && rentEnddate.value != null) {
        // Create Date objects from the provided date strings
        const startDate = new Date(rentStartdate.value);
        const endDate = new Date(rentEnddate.value);

        // Format the dates in the desired format
        const formattedStartDate = startDate.toISOString();
        const formattedEndDate = endDate.toISOString();

        try {
          axios
            .get(
              `http://localhost:8081/Car_rental_backend/cars/available?startDate=${formattedStartDate}&endDate=${formattedEndDate}`
            )
            .then((response) => {
              const decryptedRows = response.data.map((row) => {
                return {
                  ...row,
                  image_path: decrypt(row.image_path),
                };
              });
              rows.value = decryptedRows;
              if (decryptedRows.length === 0) {
                alert(
                  " ขออภัยในความไม่สะดวกเนื่องจากไม่มีรถที่พร้อมให้บริการเช่าในวันที่ระบุ กรุณาเลือกวันที่ใหม่อีกครั้ง"
                );
              }
            });
        } catch (error) {
          console.error("Find car rent failed:", error);
          // Handle the error here
        }
      }
    };
    const columns = ref([
      // {
      //   name: 'name',
      //   required: true,
      //   label: 'Dessert (100g serving)',
      //   align: 'left',
      //   field: row => row.name,
      //   sortable: true
      // },
      // {
      //   name: "ID",
      //   align: "left",
      //   label: "ID",
      //   field: "car_id",
      //   sortable: true,
      // },
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
      },
      { name: "action", align: "center", field: "car_id" },
    ]);
    const rows = ref([]);

    const rentCar = (id) => {
      var myHeaders = new Headers();
      myHeaders.append("Content-Type", "application/json");

      const startDate = new Date(rentStartdate.value);
      const endDate = new Date(rentEnddate.value);

      const formattedStartDate = startDate.toISOString();
      const formattedEndDate = endDate.toISOString();

      var raw = JSON.stringify({
        start_date: formattedStartDate,
        end_date: formattedEndDate,
        cost_per_day: pricePerdaysend.value,
        status: "จองเช่ารถล่วงหน้า",
      });

      var requestOptions = {
        method: "POST",
        headers: myHeaders,
        body: raw,
        redirect: "follow",
      };

      const myItem = localStorage.getItem("user-info");
      const userInfo = JSON.parse(myItem);

      fetch(
        "http://localhost:8081/Car_rental_backend/reservations/user/" +
          userInfo.user_id +
          "/car/" +
          id,
        requestOptions
      )
        .then((response) => response.text())
        .then((result) => {
          // alert(
          //   "UserID( " +
          //     userInfo.user_id +
          //     " ) RentCarID(" +
          //     id +
          //     " ) Successful"
          // );
          router.push("/rentcar/success");
          // window.location.reload();
        })
        .catch((error) => console.log("error", error));
    };

    return {
      currentTime,
      currentDate,
      rentStartdate,
      rentEnddate,
      onSubmit,
      columns,
      rows,
      rentCar,
      pricePerdaysend,
      userInfo,
    };
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
  height: 91vh;
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
.inputChoice {
  justify-content: center;
  width: 700px;
}
.time {
  display: grid;
  flex-direction: column;
  justify-content: center;
  font-size: 4rem;
  font-weight: bold;
}

.date {
  display: grid;
  flex-direction: column;
  justify-content: center;
  font-size: 1.5rem;
}
.row {
  margin-left: 20px;
  margin-right: 20px;
}
.historyButton {
  display: grid;
  flex-direction: column;
  justify-content: right;
  font-size: 1.5rem;
  margin-right: 40px;
}
.col-margin {
  margin-left: 105px;
  margin-right: 105px;
}

.my-sticky-header-table::v-deep .q-table__top,
.my-sticky-header-table::v-deep .q-table__bottom,
.my-sticky-header-table::v-deep thead tr:first-child th {
  background-color: #222222;
  color: white;
}

.my-sticky-header-table::v-deep thead tr th {
  /* position: sticky;
  z-index: 1; */
}

.my-sticky-header-table::v-deep thead tr:first-child th {
  /* top: 0; */
}

.my-sticky-header-table::v-deep.q-table--loading thead tr:last-child th {
  /* top: 48px; */
}

.my-sticky-header-table {
  scroll-margin-top: 70px;
  height: 450px;
  width: 1200px;
}
.close-container {
  display: flex;
  justify-content: flex-end;
}
</style>
