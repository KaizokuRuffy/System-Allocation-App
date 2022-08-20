import * as U from "../Model/Util.js";
import * as Util from "./Util.js";

export class User {
  constructor() {
    this.fields = U.User;
  }

  firstLogin(response) {
    //console.log(response.body);
    // let store = null;
    //console.log(JSON.parse().includes("-1"));
    if (response.status === 200) {
      if (!response.body.includes("-1")) {
        if (!response.body.includes("Available - No"))
          sessionStorage.setItem(
            U.System.Id,
            response.body.replace("comp_Id :", "")
          );
        else sessionStorage.setItem("Available", "No");
      } else if (response.body.includes("-1")) {
        sessionStorage.setItem(U.System.Id, -1);
      }
    } else if (response.status === 404) {
      sessionStorage.setItem("Username", JSON.stringify(response.body));
    }
  }

  login(response) {
    if (response.status === 200) {
      window.location.replace("HTML/User.html");
    } else if (response.status === 403 || response.status === 404) {
      // console.log(response);
      let bool = true;
      try {
        U.gEBI("Umsg").remove();
        U.gEBI("Uerror").remove();
        bool = false;
      } catch (error) {}

      var msg = document.createElement("td");
      msg.setAttribute("id", "Umsg");
      msg.style.color = "Red";
      //msg.style.paddingLeft = "20px";
      msg.innerText = "Invlaid username / password";
      msg.style.paddingLeft = "70px";
      msg.style.paddingBottom = "20px";
      var ele = U.gEBI("btn");
      var tr = document.createElement("tr");
      tr.id = "Uerror";
      tr.appendChild(msg);
      ele.appendChild(tr);
      setTimeout(() => {
        if (bool) {
          U.gEBI("Umsg").remove();
          U.gEBI("Uerror").remove();
        }
      }, 3000);
    } else if (
      response.status === 400 ||
      response.status === 409 ||
      response.status === 500
    ) {
      //console.log("Duplicate entry");
      window.alert(JSON.stringify(response.body).replaceAll('"', ""));
    } else {
      window.alert(response.replaceAll('"', ""));
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
        "Employee"
      );
    } else if (response.status === 403) {
      window.alert("Session Timeout");
      window.location.replace("../index.html");
    } else {
      window.alert(JSON.parse(response.body));
    }
  }
  getPlus(response) {
    /* console.log("In V");
    console.log(response);
    console.log(response.body);
    console.log(response.status); */
    if (response.status === 200) {
      let display = U.gEBI("display");
      /* display.style.fontSize = "25px";
      display.innerText = response.body; */
      //console.log(JSON.parse(response.body));
      let data = JSON.parse(response.body);
      let emp = [];
      emp[0] = data[0];
      let session = data[1];

      Util.displayAsTable(this.fields, emp, display, "Employee");
      U.gEBI("Filter").remove();

      if (session.length != 0) {
        //  console.log(session.length != 0);
        let heading = document.createElement("h2");

        heading.innerText = "'" + emp[0][U.User.Name] + "'" + " - Session data";
        heading.style.paddingTop = "50px";
        heading.style.fontWeight = "500";
        display.appendChild(heading);

        let dsp = document.createElement("div");
        dsp.id = "display session";

        display.appendChild(dsp);
        Util.displayAsTable(U.Session, session, dsp, "Session");
      }
    } else if (response.status === 403) {
      window.alert("Session Timeout");
      window.location.replace("../index.html");
    } else if (response.status === 500) {
      window.alert(JSON.stringify(response.body).replaceAll('"', ""));
    }
  }
  getAll(response) {
    let display = U.gEBI("display");
    if (response.status === 200) {
      display.style.fontSize = "25px";
      Util.displayAsTable(
        this.fields,
        JSON.parse(response.body),
        display,
        "Employee"
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
    if (response.status === 201) window.location.replace("../HTML/Admin.html");
    else if (response.status === 403) {
      window.alert("Session Timeout");
      window.location.replace("../index.html");
    } else if (response.status === 500) {
      window.alert(JSON.stringify(response.body).replaceAll('"', ""));
      window.location.href = "";
    }
  }
}
