import * as Ctrl from "../Controller/C.js";
import * as U from "../Model/Util.js";
import * as session from "./Session.js";

//U.initAll();
U.User.init();
U.System.init();
var UserController = new Ctrl.User();

if (
  sessionStorage.getItem("who") === "user" &&
  location.href.split("/").slice(-1)[0] === "User.html"
)
  UserController.get();

export let userLogin = () => {
  if (U.gEBI("userLogin") !== null)
    U.gEBI("userLogin").addEventListener("click", () => {
      //console.log("Username");
      //console.log(JSON.parse(sessionStorage.getItem("Username")));

      if (sessionStorage.getItem("Username") === null) {
        //console.log("comp_Id");
        //console.log(comp_Id);
        //console.log("Available");
        //console.log(sessionStorage.getItem("Available"));

        if (sessionStorage.getItem("Available") === "No") {
          window.alert("System not available");
        }
        //else if( comp_Id === null)
        else if (comp_Id === "-1" || comp_Id === null) {
          if (beforeLogin(true)) {
            let CID = window.prompt("Please enter Computer Id");
            UserController.login(CID);
          } else {
            window.alert(
              "System already in use by " + localStorage.getItem(U.User.Id)
            );
          }
        } else if (comp_Id !== "-1") {
          if (beforeLogin(false)) UserController.login(comp_Id);
          else window.alert("Cannot login to different system"); // *** //
        }
        //console.log("ID : " + comp_Id);
      } else {
        UserController.View.login(sessionStorage.getItem("Username"));
      }
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
  U.gEBI("getUser").addEventListener("click", () => {
    UserController.get();
  });
};

export let regUser = () => {
  U.gEBI("regUser").addEventListener("click", () => {
    window.open("../HTML/User-Reg.html", "_blank");
  });
};

//export let firstLogin = () => {};

let keypress = false;
let comp_Id = null;

export let firstTime = () => {
  //console.log("Keyup Listener");
  if (U.gEBI(U.User.Password) !== null) {
    U.gEBI(U.User.Email).addEventListener("keyup", () => {
      keypress = false;
      sessionStorage.removeItem(U.System.Id);
      sessionStorage.removeItem("Username");
      sessionStorage.removeItem("Available");
    });

    U.gEBI(U.User.Password).addEventListener("keyup", () => {
      if (!keypress) {
        keypress = true;
        UserController.firstLogin();
        comp_Id = sessionStorage.getItem(U.System.Id);
        //console.log(comp_Id);
      }
      //console.log(comp_Id);
    });
  }
};

document.addEventListener("DOMContentLoaded", function () {
  if (U.gEBI("getUser") !== null) getUser();
  if (U.gEBI("userLogout") !== null) userLogout();
  session.getAllSession;
});

let beforeLogin = (bool) => {
  if (bool) {
    if (localStorage.getItem(U.Session["Login Date"]) !== U.getDate()) {
      //console.log("");
      localStorage.clear();
      return true;
      //console.log("Session cleared");
    } else if (
      U.getShift(localStorage.getItem(U.Session["Login Time"])) !==
      U.getShift(U.getTime())
    ) {
      localStorage.clear();
      //console.log("Session cleared");
      return true;
    } else if (
      localStorage.getItem(U.User.Id) === sessionStorage.getItem(U.User.Id)
    )
      return true;
    else return false;
  } else {
    if (
      localStorage.getItem(U.User.Id) === sessionStorage.getItem(U.User.Id) &&
      localStorage.getItem(U.System.Id) === comp_Id
    )
      return true;
    return false;
  }
};
