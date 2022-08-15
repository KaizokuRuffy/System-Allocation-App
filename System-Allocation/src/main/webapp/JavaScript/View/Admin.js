import * as U from "../Model/Util.js";
import * as Util from "./Util.js";

export class Admin {
  constructor() {
    this.fields = U.Admin;
  }

  isPresent(response) {
    if (response.status === 200 && !response.body.includes("Yes")) {
      let div = U.gEBI("Register button");
      let button = document.createElement("button");
      button.setAttribute("type", "button");
      button.setAttribute("id", "regAdmin");
      button.style.width = "85px";
      button.innerText = "Register";
      div.appendChild(button);
      var tdTag = U.gEBI("admin");
      tdTag.style.paddingBottom = "35px";
    } else if (response.status === 200 && response.body.includes("Yes")) {
    } else if (response.status === 500) {
      window.alert("Something went wrong");
    } else {
      //window.location.href = "";
    }
  }
  create(response) {
    if (response.status !== 200) {
      window.location.replace("");
      window.alert("Something went wrong. Try again");
    }
  }
  login(response) {
    if (response.status === 200) {
      //window.location.replace("http://127.0.0.1:8887/HTML/Admin.html");
      window.location.replace("HTML/Admin.html");
    } else {
      //window.alert("Invalid username or password");
      /* function insertAfter(referenceNode, newNode) {
        insert(referenceNode, newNode);
      } */
      let bool = true;

      try {
        U.gEBI("Amsg").remove();
        U.gEBI("Aerror").remove();

        bool = false;
      } catch (error) {}

      var msg = document.createElement("td");
      msg.setAttribute("id", "Amsg");
      msg.style.color = "Red";
      //msg.style.paddingLeft = "20px";
      msg.innerText = "Invlaid username / password";
      msg.style.paddingLeft = "70px";
      var ele = U.gEBI("button");
      var tr = document.createElement("tr");
      tr.id = "Aerror";
      tr.appendChild(msg);
      ele.appendChild(tr);
      setTimeout(() => {
        if (bool) {
          U.gEBI("Amsg").remove();
          U.gEBI("Aerror").remove();
        }
      }, 3000);
    }
  }
  get(response) {
    /* console.log("In V");
    console.log(response);
    console.log(response.body);
    console.log(response.status); */
    if (response.status === 200) {
      let display = U.gEBI("display");
      /* display.style.fontSize = "25px";
      display.innerText = response.body; */
      Util.displayAsList(
        this.fields,
        JSON.parse(response.body),
        display,
        "Admin"
      );
    } else if (response.status === 403) {
      window.alert("Session Timeout");
      window.location.replace("../index.html");
    } else if (response.status === 500) {
      window.alert("Something went wrong");
    }
  }
  logout(response) {
    if (response.status === 200) {
      window.location.replace("../index.html");
    } else if (response.status === 403) {
      window.alert("Session Timeout");
      window.location.replace("../index.html");
    } else if (response.status === 500) {
      window.alert("Something went wrong");
    }
  }
  add(response) {
    if (response.status === 200) window.location.replace(response.redirect);
    else if (response.status === 403) {
      window.alert("Session Timeout");
      window.location.replace("../index.html");
    } else if (response.status === 500) {
      window.alert("Something went wrong");
    }
  }
}

// function insert(referenceNode, newNode) {
//     referenceNode.parentNode.insertBefore(newNode, referenceNode.nextSibling);
//   }
