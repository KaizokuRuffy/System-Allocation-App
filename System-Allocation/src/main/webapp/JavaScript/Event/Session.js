import * as Ctrl from "../Controller/C.js";
import * as U from "../Model/Util.js";

U.Session.init();
var SessionController = new Ctrl.Session();

//if (sessionStorage.getItem("who") === "user") UserController.get();

export let getAllSession = () => {
  U.gEBI("getAllSession").addEventListener("click", () => {
    SessionController.getAll();
  });
};

document.addEventListener("DOMContentLoaded", function () {
  if (U.gEBI("getAllSession") !== null) getAllSession();
});
