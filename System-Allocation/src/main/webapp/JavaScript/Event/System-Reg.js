import * as Ctrl from "../Controller/C.js";
import * as U from "../Model/Util.js";

U.System.init();
let SystemController = new Ctrl.System();

let addSystem = document
  .getElementById("addSystem")
  .addEventListener("click", () => {
    SystemController.add();
  });

document.addEventListener("DOMContentLoaded", function () {
  addSystem;
});