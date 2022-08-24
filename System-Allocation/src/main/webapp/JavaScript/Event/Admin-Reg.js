import * as Ctrl from "../Controller/C.js";
import * as U from "../Model/Util.js";

U.Admin.init();
var AdminController = new Ctrl.Admin();

function add() {
  if (
    U.gEBI("admin_Name").checkValidity() &&
    U.gEBI("admin_Email").checkValidity() &&
    U.gEBI("admin_ContactNo").checkValidity() &&
    U.gEBI("admin_Password").checkValidity()
  )
    AdminController.add();
  else console.log("Invalid data");
}
//var valid = false;

let addAdmin = document
  .getElementById("addAdmin")
  .addEventListener("click", add);

document.addEventListener("DOMContentLoaded", function () {
  addAdmin;
});
