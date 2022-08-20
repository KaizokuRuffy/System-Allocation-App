import * as Ctrl from "../Controller/C.js";
import * as U from "../Model/Util.js";
import { userLogin } from "./User.js";
import { firstTime } from "./User.js";

U.Admin.init();
var AdminController = new Ctrl.Admin();

let isPresent = () => {
  AdminController.isPresent();
};
isPresent();

let onEnter = () => {
  if (U.gEBI("emp_Password") !== null)
    U.gEBI("emp_Password").addEventListener("keyup", (e) => {
      // console.log(e.key);
      if (e.key === "Enter") U.gEBI("userLogin").click();
    });
  U.gEBI("admin_Password").addEventListener("keyup", (e) => {
    if (e.key === "Enter") U.gEBI("adminLogin").click();
  });
};

let login = document
  .getElementById("adminLogin")
  .addEventListener("click", () => {
    AdminController.login();
  });

let create = () => {
  let temp = U.gEBI("regAdmin");
  if (temp !== null)
    temp.addEventListener("click", () => {
      AdminController.create();
    });
};

document.addEventListener("DOMContentLoaded", () => {
  login;
  create();
  userLogin();
  onEnter();
  firstTime();

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
      id = U.gEBI(Admin.id).value;
      pass = U.gEBI(Admin.pass).value;

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
      id = U.gEBI(User.id).value;
      pass = U.gEBI(User.pass).value;

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
  //var user = U.gEBI("User");
  //console.log(user);
  //who = "User";
  //user.addEventListener("click", login);
});
