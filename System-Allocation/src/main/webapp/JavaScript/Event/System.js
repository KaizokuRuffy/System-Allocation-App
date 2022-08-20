import * as Ctrl from "../Controller/C.js";
import { SystemBuilder } from "../Model/Data/D.js";
import * as U from "../Model/Util.js";

U.System.init();
var SystemController = new Ctrl.System();

export let getAllSystems = U.gEBI("getAllSystems").addEventListener(
  "click",
  () => {
    SystemController.getAll();
    updateStatus();
  }
);

export let regSystem = U.gEBI("regSystem").addEventListener("click", () => {
  window.location.href = "../HTML/System-Reg.html";
});

export let updateStatus = function () {
  let comp = new SystemBuilder();

  let w_status = document.getElementsByClassName("working");
  let a_status = document.getElementsByClassName("available");

  Array.prototype.forEach.call(w_status, (item) => {
    item.addEventListener("change", (e) => {
      comp
        .setId(item.parentElement.parentElement.getAttribute("id"))
        .setWorking(e.target.value)
        .getSystem();

      SystemController.updateStatus(comp);
      item.parentElement.previousSibling.firstChild.firstChild.value =
        e.target.value;
      item.parentElement.previousSibling.firstChild.firstChild.innerText =
        e.target.value;
    });
  });

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
