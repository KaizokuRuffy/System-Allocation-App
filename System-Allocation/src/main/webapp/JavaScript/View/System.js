import * as U from "../Model/Util.js";
import * as Util from "./Util.js";

export class System {
  constructor() {
    this.fields = U.System;
  }
  getAll(response) {
    let display = U.gEBI("display");
    if (response.status === 200) {
      display.style.fontSize = "25px";
      Util.displayAsTable(
        this.fields,
        JSON.parse(response.body),
        display,
        "System"
      );
    } else if (response.status === 404 || response.status === 500) {
      Util.removeAllChildNodes(display);
      window.alert(JSON.stringify(response.body).replaceAll('"', ""));
    } else if (response.status === 403) {
      window.alert("Session Timeout");
      window.location.replace("../index.html");
    }
  }
  add(response) {
    if (response.status === 201) window.alert("System added successfully");
    else if (response.status === 403) {
      window.alert("Session Timeout");
      window.close();
    } else if (response.status === 500) {
      window.alert(JSON.stringify(response.body).replaceAll('"', ""));
      window.close();
    }
  }
  updateStatus(response) {
    if (response.status === 200) {
      window.alert("Updated successfully");
      return true;
<<<<<<< HEAD
=======
      //updateStatus();
>>>>>>> refs/remotes/origin/Form-Validation
    } else if (response.status === 403) {
      window.alert("Session Timeout");
      window.location.replace("../index.html");
    } else if (response.status === 500) {
      window.alert(JSON.stringify(response.body).replaceAll('"', ""));
    }
  }
}
