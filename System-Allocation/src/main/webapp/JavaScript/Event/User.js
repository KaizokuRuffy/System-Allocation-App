import * as Ctrl from "../Controller/C.js";
import * as U from "../Model/Util.js";
import * as session from "./Session.js";

//U.initAll();
U.User.init();
var UserController = new Ctrl.User();

if (sessionStorage.getItem("who") === "user") UserController.get();

export let userLogin = () => {
  U.gEBI("userLogin").addEventListener("click", () => {
    let comp_Id = window.prompt("Please enter Computer Id");
    UserController.login(comp_Id);
  });
};

export let getAllUsers = () => {
  U.gEBI("getAllUsers").addEventListener("click", () => {
    UserController.getAll();
  });
};

export let userLogout = () => {
  U.gEBI("userLogout").addEventListener("click", () => UserController.logout());
};

export let getUser = () => {
  U.gEBI("getUser").addEventListener("click", () => UserController.get());
};

export let regUser = () => {
  U.gEBI("regUser").addEventListener("click", () => {
    window.location.href = "../HTML/User-Reg.html";
  });
};

document.addEventListener("DOMContentLoaded", function () {
  if (U.gEBI("getUser") !== null) getUser();
  if (U.gEBI("userLogout") !== null) userLogout();
  session.getAllSession;
});
