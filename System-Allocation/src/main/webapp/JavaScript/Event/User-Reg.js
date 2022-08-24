import * as Ctrl from "../Controller/C.js";
import * as U from "../Model/Util.js";

U.User.init();
let UserController = new Ctrl.User();

function add() {
  if (
    U.gEBI("emp_Name").checkValidity() &&
    U.gEBI("emp_AdhaarId").checkValidity() &&
    U.gEBI("emp_Email").checkValidity() &&
    U.gEBI("emp_MobileNo").checkValidity() &&
    U.gEBI("emp_Password").checkValidity() &&
    U.gEBI("emp_Role").checkValidity() &&
    U.gEBI("emp_Dept").checkValidity() &&
    U.gEBI("emp_WorkLoc").checkValidity()
  )
    UserController.add();
  else console.log("Invalid data");
}

let addUser = U.gEBI("addUser").addEventListener("click", add);
document.addEventListener("DOMContentLoaded", function () {
  addUser;
});
