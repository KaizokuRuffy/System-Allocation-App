import * as Ctrl from "../Controller/C.js";
import * as U from "../Model/Util.js";

U.System.init();
let SystemController = new Ctrl.System();

function add() {
  if (
    U.gEBI("comp_Id").checkValidity() &&
    U.gEBI("MAC").checkValidity() &&
    U.gEBI("available").checkValidity() &&
    U.gEBI("working").checkValidity() &&
    U.gEBI("comp_Password").checkValidity() &&
    U.gEBI("comp_Loc").checkValidity() &&
    U.gEBI("model").checkValidity() &&
    U.gEBI("year").checkValidity()
  )
    SystemController.add();
  else console.log("Invalid data");
}

let addSystem = U.gEBI("addSystem").addEventListener("click", add);

document.addEventListener("DOMContentLoaded", function () {
  addSystem;
});
