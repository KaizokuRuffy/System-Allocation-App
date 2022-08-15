import * as Ctrl from "../Controller/C.js";
import * as U from "../Model/Util.js";
import { userLogin } from "./User.js";

U.Admin.init();
var AdminController = new Ctrl.Admin();

let isPresent = () => {
  AdminController.isPresent();
};

let login = document
  .getElementById("adminLogin")
  .addEventListener("click", () => {
    AdminController.login();
  });

let create = () => {
  let temp = document.getElementById("regAdmin");
  if (temp !== null)
    temp.addEventListener("click", () => {
      window.location.href = "HTML/Admin-Reg.html";
      AdminController.create();
    });
};

document.addEventListener("DOMContentLoaded", () => {
  isPresent();
  login;
  create();
  userLogin();

  /*
  function login() {
    let Admin = {
      id: "admin_Id",
      pass: "admin_Password",
    };
    let User = {
      id: "emp_Id",
      pass: "emp_Password",
    };

    let url;
    let id;
    let pass;
    let query_params;

    let xhr = new XMLHttpRequest();
    if (who === "Admin") {
      id = document.getElementById(Admin.id).value;
      pass = document.getElementById(Admin.pass).value;

      url =
        "http://localhost:8080/System-Allocation/" +
        who +
        "Controller/" +
        who.toLowerCase() +
        "Login";

      query_params = Admin.id + "=" + id + "&" + Admin.pass + "=" + pass;
      url = url + "?" + query_params;
    }
    if (who === "User") {
      id = document.getElementById(User.id).value;
      pass = document.getElementById(User.pass).value;

      url =
        "http://localhost:8080/System-Allocation/" +
        who +
        "Controller/" +
        who.toLowerCase() +
        "Login";

      query_params = User.id + "=" + id + "&" + User.pass + "=" + pass;

      url += "?" + query_params;
    }

    console.log(id);
    console.log(pass);
    console.log(url);

    xhr.onreadystatechange = function () {
      if (this.readyState == 4 && this.status == 200) {
        console.log("Body : " + this.responseText);
        console.log("Status code : " + this.status);
      }
    };
    xhr.open("POST", url);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr.responseType = "text";
    xhr.send();
  }
  */
  //var user = document.getElementById("User");
  //console.log(user);
  //who = "User";
  //user.addEventListener("click", login);
});
