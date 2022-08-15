import * as Ctrl from "../Controller/C.js";
import { SystemBuilder } from "../Model/Data/D.js";
import * as U from "../Model/Util.js";

U.System.init();
var SystemController = new Ctrl.System();

export let getAllSystems = document
  .getElementById("getAllSystems")
  .addEventListener("click", () => {
    SystemController.getAll();
    updateStatus();
  });

export let regSystem = document
  .getElementById("regSystem")
  .addEventListener("click", () => {
    window.location.href = "../HTML/System-Reg.html";
  });

export let updateStatus = function () {
  let comp = new SystemBuilder();

  let w_status = document.getElementsByClassName("working");

  Array.prototype.forEach.call(w_status, (item) => {
    item.addEventListener("change", (e) => {
      comp
        .setId(item.parentElement.parentElement.getAttribute("id"))
        .setWorking(e.target.value)
        .getSystem();

      SystemController.updateStatus(comp);
    });
  });

  let a_status = document.getElementsByClassName("available");

  Array.prototype.forEach.call(a_status, (item) =>
    item.addEventListener("change", (event) => {
      comp
        .setId(item.parentElement.parentElement.getAttribute("id"))
        .setAvailable(event.target.value)
        .getSystem();

      SystemController.updateStatus(comp);
    })
  );
};

document.addEventListener("DOMContentLoaded", function () {
  getAllSystems;
});
