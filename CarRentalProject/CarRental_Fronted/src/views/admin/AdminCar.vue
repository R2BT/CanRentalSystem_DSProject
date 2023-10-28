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
            class="my-sticky-header-table"
            flat
            bordered
            :rows="rows"
            :columns="columns"
            row-key="car_id"
            :filter="filter"
            :rows-per-page-options="[10]"
            no-data-label="ไม่มีข้อมูลรถเช่า"
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
                      props.row.car_type,
                      props.row.image_path
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
                      props.row.car_type.type_model,
                      props.row.image_path
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
            {{ nameCarInfo }}<br />
            {{ typeCarInfo }}<br />
            {{ platNumberInfo }}<br />
            {{ colorShow }}<br />
            ราคาเช่าต่อวัน : {{ rentPriceInfo }} บาท<br />
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
                :rules="[
                  (value) => !!value || 'กรุณากรอกราคา',
                  (value) => value > 0 || 'ราคาต้องไม่ติดลบและไม่เท่ากับ0',
                ]"
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
              <q-input
                v-model="car_img"
                type="file"
                @change="handleFileChange"
                accept=".jpeg,.jpg,.png"
                hint="Upload รูปภาพรถ"
                required
                :rules="[(value) => !!value || 'กรุณา Upload รูปภาพรถ']"
              >
                <template v-slot:append>
                  <q-icon name="attachment" color="grey" />
                </template>
              </q-input>
              <div class="image-container" v-if="car_img">
                <img
                  v-if="car_img"
                  :src="car_show"
                  alt="Selected Image"
                  class="centered-image"
                />
              </div>
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
                :rules="[
                  (value) => !!value || 'กรุณากรอกราคา',
                  (value) => value > 0 || 'ราคาต้องไม่ติดลบและไม่เท่ากับ0',
                ]"
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
              <q-input
                v-model="car_imgEdit"
                type="file"
                @change="handleFileChangeEdit"
                accept=".jpeg,.jpg,.png"
                hint="Upload รูปภาพรถ"
                required
                :rules="[(value) => !!value || 'กรุณา Upload รูปภาพรถ']"
              >
                <template v-slot:append>
                  <q-icon name="attachment" color="grey" />
                </template>
              </q-input>
              <img
                :src="carShowimgEdit"
                alt="Car Image"
                style="
                  width: 160px;
                  height: 120px;
                  display: block;
                  margin: 0 auto;
                  margin-top: 10px;
                "
              /><br />
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
import CryptoJS from "crypto-js";
import router from "../../router";
const carname = ref("");
const description = ref("");
const pricePerday = ref("");
const color = ref("");
const platNumber = ref("");
const carType = ref([]);
const car_img = ref(null);
const car_imgEdit = ref(null);
const carShowimgEdit = ref(null);
const car_show = ref(null);
const car_img_send_add = ref(null);
const car_img_send_edit = ref(null);
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
    label: "ราคาเช่า/วัน",
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
    filter: false,
  },
  { name: "action", align: "center", field: "car_id" },
]);
const decrypt = (encryptedUrl) => {
  console.log(encryptedUrl);
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
    console.log(carTypeId.value);
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
      car_img,
      car_show,
      carShowimgEdit,
      car_imgEdit,
      car_img_send_add,
      car_img_send_edit,
      secret: "123#$%",
    };
  },

  data() {
    return {
      files: [],
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
    async handleFileChange(event) {
      const base64 = ref();
      const base64Reducesize = ref();
      console.log(this.car_img);
      console.log("worked");
      console.log(this.car_img[0].name);
      if (this.car_img != null) {
        this.car_show = URL.createObjectURL(this.car_img[0]);
        // const hashedURL = this.encrypt(this.car_show);
        base64.value = await this.convertBase64(this.car_img[0]);
        base64Reducesize.value = await this.reduceSizeBase64(base64.value);
        this.car_img_send_add = this.encrypt(base64Reducesize.value);
      }
      console.log("Selected image URL:", this.car_img_send_add);
    },
    async handleFileChangeEdit(event) {
      const base64 = ref();
      const base64Reducesize = ref();
      console.log(this.car_imgEdit);
      console.log(this.car_imgEdit[0].name);
      if (this.car_imgEdit != null) {
        this.carShowimgEdit = URL.createObjectURL(this.car_imgEdit[0]);
        // const hashedURL = this.encrypt(this.car_show);
        base64.value = await this.convertBase64(this.car_imgEdit[0]);
        base64Reducesize.value = await this.reduceSizeBase64(base64.value);
        this.car_img_send_edit = this.encrypt(base64Reducesize.value);
      }
      console.log("Selected image URL:", this.car_img_send_edit);
    },

    convertBase64(file) {
      return new Promise((resolve, reject) => {
        const fileReader = new FileReader();
        fileReader.readAsDataURL(file);

        fileReader.onload = () => {
          resolve(fileReader.result);
        };

        fileReader.onerror = (error) => {
          reject(error);
        };
      });
    },

    async reduceSizeBase64(base64) {
      const minImageSize = 300; // Set your minimum image size here (in KB)
      const maxImageWidth = 450; // Set your maximum image width here
      const maxImageHeight = 450; // Set your maximum image height here

      const oldSize = this.calc_image_size(base64);
      if (oldSize > minImageSize) {
        const resizedBase64 = await this.reduce_image_file_size(
          base64,
          maxImageWidth,
          maxImageHeight
        );
        const newSize = this.calc_image_size(resizedBase64);
        console.log("New Size: ", newSize, "KB");
        console.log("Old Size: ", oldSize, "KB");
        return resizedBase64;
      } else {
        console.log("Image already small enough");
        return base64;
      }
    },
    calc_image_size(image) {
      let y = 1;
      if (image.endsWith("==")) {
        y = 2;
      }
      const x_size = image.length * (3 / 4) - y;
      return Math.round(x_size / 1024);
    },

    async reduce_image_file_size(base64Str, MAX_WIDTH = 450, MAX_HEIGHT = 450) {
      return new Promise((resolve, reject) => {
        let img = new Image();
        img.src = base64Str;
        img.onload = () => {
          let canvas = document.createElement("canvas");
          let width = img.width;
          let height = img.height;

          if (width > height) {
            if (width > MAX_WIDTH) {
              height *= MAX_WIDTH / width;
              width = MAX_WIDTH;
            }
          } else {
            if (height > MAX_HEIGHT) {
              width *= MAX_HEIGHT / height;
              height = MAX_HEIGHT;
            }
          }

          canvas.width = width;
          canvas.height = height;
          let ctx = canvas.getContext("2d");
          ctx.drawImage(img, 0, 0, width, height);

          const resizedBase64 = canvas.toDataURL();

          resolve(resizedBase64);
        };

        img.onerror = (error) => {
          reject(error);
        };
      });
    },

    // decrypt(encryptedUrl) {
    //   console.log(encryptedUrl);
    //   const decryptData = CryptoJS.AES.decrypt(
    //     encryptedUrl,
    //     this.secret
    //   ).toString(CryptoJS.enc.Utf8);
    //   return decryptData;
    // },

    encrypt(url) {
      if (url.length) {
        // hash the name with any algorithm
        const data = CryptoJS.AES.encrypt(url, this.secret).toString();

        return data;
      }
    },
    AddcarModal() {
      carname.value = "";
      description.value = "";
      pricePerday.value = "";
      color.value = "";
      platNumber.value = "";
      selectedCarType.value = null;
      this.dialogAddcar = true;
      this.car_img = null;
    },

    deletecarAlert(id, carName, price, color, plateNum, type, img) {
      this.dialogMessage = "ยืนยันการลบรถเช่า ";
      this.nameCarInfo = `ชื่อรถ: ${carName}`;
      this.typeCarInfo = `ประเภท: ${type}`;
      this.platNumberInfo = `หมายเลขทะเบียน: ${plateNum}`;
      this.colorShow = `สี: ${color}`;
      this.rentPriceInfo = price;
      this.carShowimgDelete = img;
      this.dialog = true;
      this.cidToDelete = id;
    },
    editCarAlert(id, description, carName, price, color, plateNum, type, img) {
      this.carname = carName;
      this.description = description;
      this.pricePerday = price;
      this.color = color;
      this.platNumber = plateNum;
      this.selectedCarType = type;
      this.dialogEditcar = true;
      this.carShowimgEdit = img;
      this.car_img_send_edit = this.encrypt(img);
      this.cidToEdit = id;
    },
    confirmAddCar() {
      console.log(this.car_img[0]);
      if(carname.value!=''&&description.value !=''&&pricePerday.value !=0&&color.value !=''&&platNumber.value !=''&&this.car_img_send_add !=''&& pricePerday.value>=0&&this.car_img[0]!=null&&selectedCarType.value!=null){
        this.dialogAddcar = false;
        const carData = {
        car_name: carname.value,
        description: description.value,
        price_per_day: pricePerday.value,
        color: color.value,
        plat_number: platNumber.value,
        image_path: this.car_img_send_add,
      };
      carTypeId.value = selectedCarType.value;
      const requestOptions = {
        method: "POST",
        headers: {
          "Content-Type": "application/json; charset=UTF-8",
        },
        body: JSON.stringify(carData),
      };

      fetch(
        `http://localhost:8081/Car_rental_backend/cars/` +
          carTypeId.value.type_id,
        requestOptions
      )
        .then((response) => response.text())
        .then((result) => {})
        .catch();
    }
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

      fetch(
        "http://localhost:8081/Car_rental_backend/cars/" + id,
        requestOptions
      )
        .then((response) => response.json())
        .then((result) => {
          window.location.reload();
        })
        .catch(window.location.reload());
    },
    editCar(id) {
      console.log("work");
      var myHeaders = new Headers();
      if(carname.value!=''&&description.value !=''&&pricePerday.value !=0&&color.value !=''&&platNumber.value !=''&&this.car_img_send_add !=''&& pricePerday.value>0&&this.car_img_send_edit!=null){
        this.dialogEditcar = false;
        const carData = {
        car_name: carname.value,
        description: description.value,
        price_per_day: pricePerday.value,
        color: color.value,
        plat_number: platNumber.value,
        image_path: this.car_img_send_edit,
      };
      carTypeId.value = selectedCarType.value;
      console.log(carTypeId.value.type_id);
      console.log(id);
      // myHeaders.append("Content-Type", "application/json; charset=UTF-8");
      const requestOptions = {
        method: "PUT",
        headers: {
          "Content-Type": "application/json; charset=UTF-8",
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
      }
    },
    confirmDeleteCar() {
      this.dialog = false;
      this.deleteCar(this.cidToDelete);
    },
    confirmEditCar() {
      this.editCar(this.cidToEdit);
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
.topicModal {
  font-weight: bold;
}
.image-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 150px;
  height: 150px;
  overflow: hidden;
  margin-left: 36%;
  margin-right: 60%;
}

.centered-image {
  max-width: 100%;
  max-height: 100%;
  object-fit: cover; /* Optional, to control how the image fills the container */
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
