import * as Ctrl from "../Controller/C.js";
import * as U from "../Model/Util.js";
import * as user from "./User.js";
import * as system from "./System.js";
import * as session from "./Session.js";

//U.initAll();
U.Admin.init();
var AdminController = new Ctrl.Admin();
//if (sessionStorage.getItem("who") === "admin")
AdminController.get();

let getAdmin = U.gEBI("getAdmin").addEventListener("click", () => {
  AdminController.get();
});

let adminLogout = U.gEBI("adminLogout").addEventListener("click", () =>
  AdminController.logout()
);

let regAdmin = U.gEBI("regAdmin").addEventListener("click", () => {
  window.location.href = "../HTML/Admin-Reg.html";
});

document.addEventListener("DOMContentLoaded", () => {
  getAdmin;
  regAdmin;
  adminLogout;

  user.regUser();
  user.getAllUsers();

  system.getAllSystems;
  system.regSystem;

  session.getAllSession;
});
