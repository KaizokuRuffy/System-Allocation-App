import * as Ctrl from "../Controller/C.js";
import * as U from "../Model/Util.js";

import { togglePassword } from "./Util.js";

U.System.init();
let SystemController = new Ctrl.System();

function add() {
  if (U.gEBI("comp_Password").value !== U.gEBI("confirm_Password").value)
    U.gEBI("confirm_Password").setCustomValidity("Password mismatch");
  else U.gEBI("confirm_Password").setCustomValidity("");

  let year = Number(U.gEBI("year").value);
  let max_year = new Date().getFullYear();
  let min_year = 2010;
  if (year < min_year || year > max_year) {
    if (Number(U.gEBI("year").value) < min_year)
      U.gEBI("year").setCustomValidity(
        "Year should be greater than " + min_year
      );
    if (Number(U.gEBI("year").value) > max_year)
      U.gEBI("year").setCustomValidity("Year should be less than " + max_year);
  } else U.gEBI("year").setCustomValidity("");

  if (
    U.gEBI("comp_Id").checkValidity() &&
    U.gEBI("MAC").checkValidity() &&
    U.gEBI("available").checkValidity() &&
    U.gEBI("working").checkValidity() &&
    U.gEBI("comp_Password").checkValidity() &&
    U.gEBI("comp_Loc").checkValidity() &&
    U.gEBI("model").checkValidity() &&
    U.gEBI("year").checkValidity() &&
    U.gEBI("confirm_Password").checkValidity()
  )
    SystemController.add();
  else console.log("Invalid data");
}

let addSystem = U.gEBI("addSystem").addEventListener("click", add);

document.addEventListener("DOMContentLoaded", function () {
  addSystem;
  togglePassword();
});
