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
              <h2>รายการเช่ารถ</h2>
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
            <router-link
              :to="{ name: 'adminhistoryservation' }"
              class="custom-link"
            >
              <q-btn
                label="ประวัติรายการเช่ารถทั้งหมด"
                color="grey"
                icon-right="history"
                @click="AddcarModal"
                class="add-button"
              />
            </router-link>
          </div>
          <q-table
            class="my-sticky-header-table"
            flat
            bordered
            :rows="rows"
            :columns="columns"
            row-key="reservation_id"
            :filter="filter"
            no-data-label="ไม่มีข้อมูลรายการเช่ารถที่รอดำเนินการ"
          >
            <template #body-cell-Status="props">
              <q-td key="Status" :props="props">
                <q-badge
                  :color="getStatusColor(props.row.status)"
                  :label="props.row.status"
                />
              </q-td>
            </template>
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

            <template v-slot:body-cell-action="props">
              <q-td :props="props">
                <q-btn
                  icon="check"
                  color="positive"
                  @click="
                    historyreservationAlert(
                      props.row.reservation_id,
                      props.row.user.user_firstname,
                      props.row.user.user_surname,
                      props.row.user.user_phonenumber,
                      props.row.car.car_name,
                      props.row.car.plat_number,
                      props.row.car.car_type.type_model,
                      props.row.start_date,
                      props.row.end_date,
                      props.row.total_cost,
                      props.row.image_path,
                      props.row.cost_per_day,
                      props.row.status,
                      props.row.discount
                    )
                  "
                ></q-btn>
                <q-btn
                  icon="key"
                  color="blue"
                  @click="
                    changeStatusAlert(
                      props.row.reservation_id,
                      props.row.user.user_id,
                      props.row.car.car_id,
                      props.row.user.user_firstname,
                      props.row.user.user_surname,
                      props.row.user.user_phonenumber,
                      props.row.car.car_name,
                      props.row.car.plat_number,
                      props.row.car.car_type.type_model,
                      props.row.start_date,
                      props.row.end_date,
                      props.row.total_cost,
                      props.row.image_path,
                      props.row.cost_per_day,
                      props.row.status,
                      props.row.discount
                    )
                  "
                ></q-btn>
                <q-btn
                  icon="delete"
                  color="negative"
                  @click="
                    reservationAlert(
                      props.row.reservation_id,
                      props.row.user.user_firstname,
                      props.row.user.user_surname,
                      props.row.user.user_phonenumber,
                      props.row.car.car_name,
                      props.row.car.plat_number,
                      props.row.car.car_type.type_model,
                      props.row.start_date,
                      props.row.end_date,
                      props.row.total_cost,
                      props.row.image_path,
                      props.row.cost_per_day,
                      props.row.status,
                      props.row.discount
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
            <img
              :src="carShowimgDelete"
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
        <q-card-title class="text-h6 start-container" style="margin-bottom: 0">
          <div style="font-size: 13px">
            {{ name }}<br />
            {{ phoneNumberInfo }}<br />
            {{ nameCarInfo }}<br />
            {{ typeCarInfo }}<br />
            {{ platNumberInfo }}<br />
            สถานะ: {{ statusCheck }}<br />
            วันที่เริ่มเช่า-วันที่สิ้นสุดการเช่า: {{ rentStartdate }} -
            {{ rentEnddate }}<br />
            ราคาเช่าต่อวัน: {{ pricePerday }} บาท<br />
            จำนวนวันที่เช่า: {{ countDay }} วัน <br />
            ส่วนลด: {{ saleprice }} <br />
            <div style="font-weight: bold">    ราคาเช่าทั้งหมด: {{ rentPriceInfo }} บาท </div> <br />
          </div>
        </q-card-title>
        <q-card-title class="text-h6 close-container" style="margin-bottom: 0">
          <div style="font-size: 13px; color: rgb(255, 0, 0)">
            *หากกดยืนยันรถเช่าคนนี้จะถูกปรับสถานะให้พร้อมเช่าอีกครั้ง*
          </div>
        </q-card-title>
      </q-card-section>
      <q-card-actions align="center" style="padding-top: 10px">
        <q-btn label="ยกเลิก" color="negative" @click="dialog = false" />
        <q-btn
          label="ยืนยัน"
          color="positive"
          @click="confirmDeleteReservation"
        />
      </q-card-actions>
    </q-card>
  </q-dialog>
  <q-dialog v-model="dialogHistory" persistent>
    <q-card style="padding: 30px">
      <q-card-section>
        <q-card-title
          class="text-h6 start-container text-center"
          style="margin-bottom: 0"
        >
          <div style="font-size: 18px font-weight: bold; margin-bottom: 10px;">
            {{ dialogMessage }}<br />
            <img
              :src="carShowimgDelete"
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
        <q-card-title class="text-h6 start-container" style="margin-bottom: 0">
          <div style="font-size: 13px">
            {{ name }}<br />
            {{ phoneNumberInfo }}<br />
            {{ nameCarInfo }}<br />
            {{ typeCarInfo }}<br />
            {{ platNumberInfo }}<br />
            สถานะ: {{ statusCheck }}<br />
            วันที่เริ่มเช่า-วันที่สิ้นสุดการเช่า: {{ rentStartdate }} -
            {{ rentEnddate }}<br />
            ราคาเช่าต่อวัน: {{ pricePerday }} บาท<br />
            จำนวนวันที่เช่า: {{ countDay }} วัน <br />
            ส่วนลด: {{ saleprice }} <br />
            <div style="font-weight: bold">  ราคาเช่าทั้งหมด: {{ rentPriceInfo }} บาท </div> <br /> 
          </div>
        </q-card-title>
        <q-card-title class="text-h6 close-container" style="margin-bottom: 0">
          <div style="font-size: 13px; color: rgb(3, 120, 3)">
            *เมื่อกดยืนยันรายการนี้จะถูกส่งไปที่ประวัติรายการเช่ารถทั้งหมด*
          </div>
          <div style="font-size: 13px; color: red"></div>
        </q-card-title>
      </q-card-section>
      <q-card-actions align="center" style="padding-top: 10px">
        <q-btn label="ยกเลิก" color="negative" @click="dialogHistory = false" />
        <q-btn
          label="ยืนยัน"
          color="positive"
          @click="confirmHistoryReservation"
        />
      </q-card-actions>
    </q-card>
  </q-dialog>
  <q-dialog v-model="dialogChangeStatus" persistent>
    <q-card style="padding: 30px">
      <q-card-section>
        <q-card-title
          class="text-h6 start-container text-center"
          style="margin-bottom: 0"
        >
          <div style="font-size: 18px font-weight: bold; margin-bottom: 10px;">
            {{ dialogMessage }}<br />
            <img
              :src="carShowimgDelete"
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
        <q-card-title class="text-h6 start-container" style="margin-bottom: 0">
          <div style="font-size: 13px">
            {{ name }}<br />
            {{ phoneNumberInfo }}<br />
            {{ nameCarInfo }}<br />
            {{ typeCarInfo }}<br />
            {{ platNumberInfo }}<br />
            สถานะ: {{ statusCheck }}<br />
            วันที่เริ่มเช่า-วันที่สิ้นสุดการเช่า: {{ rentStartdate }} -
            {{ rentEnddate }}<br />
            ราคาเช่าต่อวัน: {{ pricePerday }} บาท<br />
            จำนวนวันที่เช่า: {{ countDay }} วัน <br />
            ส่วนลด: {{ saleprice }} <br />
            <div style="font-weight: bold">ราคาเช่าทั้งหมด: {{ rentPriceInfo }} บาท </div> <br />
          </div>
        </q-card-title>
        <q-card-title class="text-h6 close-container" style="margin-bottom: 0">
          <div style="font-size: 13px; color: blue; margin-right: 20px">
            *เมื่อกดยืนยันรายการนี้เปลี่ยนสถานะการจองเป็นอยู่ในระหว่างการเช่ารถ*
          </div>
          <div style="font-size: 13px; color: red"></div>
        </q-card-title>
      </q-card-section>
      <q-card-actions align="center" style="padding-top: 10px">
        <q-btn
          label="ยกเลิก"
          color="negative"
          @click="dialogChangeStatus = false"
        />
        <q-btn label="ยืนยัน" color="positive" @click="confirmChangeStatus" />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script>
import Navbar from "../../components/EmployeeHeader.vue";
import axios from "axios";
import CryptoJS from "crypto-js";
import ReservationService from "../../service/Reservationservice.js";
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
    name: "Status",
    align: "left",
    label: "สถานะ",
    field: (row) => row.status,
    sortable: true,
  },
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
    field: (row) => row.user.user_firstname,
    sortable: true,
  },
  {
    name: "Lastname",
    align: "left",
    label: "นามสกุล",
    field: (row) => row.user.user_surname,
    sortable: true,
  },
  {
    name: "Phonenumber",
    align: "left",
    label: "เบอร์โทรศัพท์",
    field: (row) => row.user.user_phonenumber,
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
    name: "Discount",
    align: "left",
    label: "ส่วนลด",
    field: (row) => row.discount,
    sortable: true,
  },
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
    label: "รูปรถ",
    field: (row) => row.car.image_path,
  },
  { name: "action", align: "center", field: "reservation_id" },
]);
const decrypt = (encryptedUrl) => {
  const decryptData = CryptoJS.AES.decrypt(encryptedUrl, "123#$%").toString(
    CryptoJS.enc.Utf8
  );
  return decryptData;
};
const fetchData = () => {
  ReservationService.getReservation().then((response) => {
    const decryptedRows = response.data.map((row) => {
      return {
        ...row,
        image_path: decrypt(row.car.image_path),
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
      dialogChangeStatus: false,
      searchText: "",
      dialogMessage: "",
      eidChangeRoles: "",
    };
  },
  components: {
    Navbar,
  },
  methods: {
    encrypt(url) {
      if (url.length) {
        const data = CryptoJS.AES.encrypt(url, this.secret).toString();
        return data;
      }
    },
    getStatusColor(status) {
      if (status === "จองเช่ารถล่วงหน้า") {
        return "blue";
      } else if (status === "อยู่ในระหว่างดำเนินการเช่ารถ") {
        return "green";
      } else {
        return "default-color";
      }
    },
    historyreservationAlert(
      id,
      firstname,
      lastname,
      phonenumber,
      carname,
      platNumber,
      type,
      startdate,
      enddate,
      price,
      img,
      priceperday,
      status,discount
    ) {
      this.dialogMessage = "สิ้นสุดการเช่ารถ";
      this.name = `ชื่อผู้เช่า : ${firstname} ${lastname}`;
      this.phoneNumberInfo = `เบอร์โทรศัพท์ : ${phonenumber}`;
      this.nameCarInfo = `ชื่อรถ: ${carname}`;
      this.typeCarInfo = `ประเภท: ${type}`;
      this.platNumberInfo = `หมายเลขทะเบียน: ${platNumber}`;
      this.rentStartdate = formatDate(`${startdate}`);
      this.rentEnddate = formatDate(`${enddate}`);
      const startDate = new Date(startdate);
      const endDate = new Date(enddate);
      const dayDifference = (endDate - startDate) / (1000 * 60 * 60 * 24) + 1;
      this.countDay = dayDifference;
      this.rentPriceInfo = price;
      this.dialogHistory = true;
      this.carShowimgDelete = img;
      this.actionColor = "negative";
      //sendHistory
      this.ridToDelete = id;
      this.firstname = firstname;
      this.lastname = lastname;
      this.phoneNumber = phonenumber;
      this.carname = carname;
      this.type = type;
      this.platNumber = platNumber;
      this.startDate = startDate;
      this.endDate = endDate;
      this.totalcost = price;
      this.priceperday = priceperday;
      this.imgpath = this.encrypt(img);
      this.pricePerday = priceperday;
      this.statusCheck = status;
      this.saleprice = discount;
    },
    changeStatusAlert(
      id,
      uid,
      cid,
      firstname,
      lastname,
      phonenumber,
      carname,
      platNumber,
      type,
      startdate,
      enddate,
      price,
      img,
      priceperday,
      status,discount
    ) {
      this.dialogMessage = "เปลี่ยนสถานะเป็นอยู่ในระหว่างดำเนินการเช่ารถ";
      this.name = `ชื่อผู้เช่า : ${firstname} ${lastname}`;
      this.phoneNumberInfo = `เบอร์โทรศัพท์ : ${phonenumber}`;
      this.nameCarInfo = `ชื่อรถ: ${carname}`;
      this.typeCarInfo = `ประเภท: ${type}`;
      this.platNumberInfo = `หมายเลขทะเบียน: ${platNumber}`;
      this.rentStartdate = formatDate(`${startdate}`);
      this.rentEnddate = formatDate(`${enddate}`);
      const startDate = new Date(startdate);
      const endDate = new Date(enddate);
      const dayDifference = (endDate - startDate) / (1000 * 60 * 60 * 24) + 1;
      this.countDay = dayDifference;
      this.rentPriceInfo = price;
      this.dialogChangeStatus = true;
      this.carShowimgDelete = img;
      this.actionColor = "negative";
      //sendHistory
      this.ridToDelete = id;
      this.firstname = firstname;
      this.lastname = lastname;
      this.phoneNumber = phonenumber;
      this.carname = carname;
      this.type = type;
      this.platNumber = platNumber;
      this.startDate = startDate;
      this.endDate = endDate;
      this.totalcost = price;
      this.pricePerday = priceperday;
      this.priceperday = priceperday;
      this.imgpath = this.encrypt(img);
      this.saleprice = discount;
      this.statusCheck = status;
      this.uid = uid;
      this.cid = cid;
    },
    reservationAlert(
      id,
      firstname,
      lastname,
      phonenumber,
      carname,
      platNumber,
      type,
      startdate,
      enddate,
      price,
      img,
      priceperday,
      status,discount
    ) {
      this.dialogMessage = "ยกเลิกรายการเช่ารถ ";
      this.name = `ชื่อผู้เช่า : ${firstname} ${lastname}`;
      this.phoneNumberInfo = `เบอร์โทรศัพท์ : ${phonenumber}`;
      this.nameCarInfo = `ชื่อรถ: ${carname}`;
      this.typeCarInfo = `ประเภท: ${type}`;
      this.platNumberInfo = `หมายเลขทะเบียน: ${platNumber}`;
      this.rentStartdate = formatDate(`${startdate}`);
      this.rentEnddate = formatDate(`${enddate}`);
      const startDate = new Date(startdate);
      const endDate = new Date(enddate);
      const dayDifference = (endDate - startDate) / (1000 * 60 * 60 * 24) + 1;
      this.countDay = dayDifference;
      this.rentPriceInfo = price;
      this.dialog = true;
      this.carShowimgDelete = img;
      this.actionColor = "negative";
      this.ridToDelete = id;
      this.pricePerday = priceperday;
      this.statusCheck = status;
      this.saleprice = discount;
    },
    deleteReservation(id) {
      var myHeaders = new Headers();
      myHeaders.append("Content-Type", "application/json");

      var raw = {
        reservation_id: id,
      };

      var requestOptions = {
        method: "DELETE",
        headers: myHeaders,
        body: JSON.stringify(raw),
        redirect: "follow",
      };

      fetch(
        "http://localhost:8081/Car_rental_backend/reservations/" + id,
        requestOptions
      )
        .then((response) => response.json())
        .then((result) => {
          window.location.reload();
        })
        .catch(window.location.reload());
    },
    deleteReservation(id) {
      var myHeaders = new Headers();
      myHeaders.append("Content-Type", "application/json");

      var raw = {
        reservation_id: id,
      };

      var requestOptions = {
        method: "DELETE",
        headers: myHeaders,
        body: JSON.stringify(raw),
        redirect: "follow",
      };

      fetch(
        "http://localhost:8081/Car_rental_backend/reservations/" + id,
        requestOptions
      )
        .then((response) => response.json())
        .then((result) => {
          window.location.reload();
        })
        .catch(window.location.reload());
    },

    saveHistoryReservation(id) {
      var myHeaders = new Headers();
      myHeaders.append("Content-Type", "application/json; charset=UTF-8");

      var raw = {
        user_firstname: this.firstname,
        user_lastname: this.lastname,
        user_phone: this.phoneNumber,
        car_name: this.carname,
        car_typemodel: this.type,
        car_platenumber: this.platNumber,
        start_date: this.startDate,
        end_date: this.endDate,
        total_cost: this.totalcost,
        cost_per_day: this.priceperday,
        image_path: this.imgpath,
        discount: this.saleprice
      };

      var requestOptions = {
        method: "POST",
        headers: myHeaders,
        body: JSON.stringify(raw),
        redirect: "follow",
      };

      fetch(
        "http://localhost:8081/Car_rental_backend/historyreservations",
        requestOptions
      )
        .then((response) => response.json())
        .then((result) => {
          this.deleteReservation(id);
        });
    },
    changeStatus(id, uid, cid) {
      var myHeaders = new Headers();
      myHeaders.append("Content-Type", "application/json; charset=UTF-8");

      var raw = {
        start_date: this.startDate,
        end_date: this.endDate,
        total_cost: this.totalcost,
        cost_per_day: this.priceperday,
        discount: this.saleprice,
        status: "อยู่ในระหว่างดำเนินการเช่ารถ",
      };

      var requestOptions = {
        method: "PUT",
        headers: myHeaders,
        body: JSON.stringify(raw),
        redirect: "follow",
      };

      fetch(
        "http://localhost:8081/Car_rental_backend/reservations/" +
          id +
          "/user/" +
          uid +
          "/car/" +
          cid,
        requestOptions
      )
        .then((response) => response.json())
        .then((result) => {
          window.location.reload();
        })
        .catch(window.location.reload());
    },
    confirmDeleteReservation() {
      this.dialog = false;
      this.deleteReservation(this.ridToDelete);
    },
    confirmHistoryReservation() {
      if (this.statusCheck == "จองเช่ารถล่วงหน้า") {
        alert(
          "สถานะของรายการนี้อยู่ในการจองเช่ารถล่วงหน้า ไม่สามารถสิ้นสุดรายการเช่านี้ได้"
        );
        return;
      } else {
        this.dialogHistory = false;
        this.saveHistoryReservation(this.ridToDelete);
      }
    },
    confirmChangeStatus() {
      if (this.statusCheck == "อยู่ในระหว่างดำเนินการเช่ารถ") {
        alert("สถานะของรายการนี้อยู่ในระหว่างดำเนินการเช่ารถอยู่แล้ว");
        return;
      } else {
        this.dialogChangeStatus = false;
        this.changeStatus(this.ridToDelete, this.uid, this.cid);
      }
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
