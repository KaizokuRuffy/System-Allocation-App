import * as Ctrl from "../Controller/C.js";
import * as U from "../Model/Util.js";

U.Admin.init();
var AdminController = new Ctrl.Admin();
let addAdmin = document
  .getElementById("addAdmin")
  .addEventListener("click", () => {
    AdminController.add();
  });

document.addEventListener("DOMContentLoaded", function () {
  addAdmin;
});
