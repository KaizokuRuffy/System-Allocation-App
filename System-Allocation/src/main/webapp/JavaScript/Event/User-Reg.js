import * as Ctrl from "../Controller/C.js";
import * as U from "../Model/Util.js";

U.User.init();
let UserController = new Ctrl.User();

let addUser = U.gEBI("addUser").addEventListener("click", () => {
  UserController.add();
});
document.addEventListener("DOMContentLoaded", function () {
  addUser;
});
