import * as D from "../Model/Data/D.js";
import { User } from "../Controller/C.js";

export let displayAsList = function (fields, data, element, name) {
  removeAllChildNodes(element);
  element.setAttribute("align", "center");
  element.style.fontSize = "20px";
  element.style.paddingTop = "50px";

  let field_key = Object.keys(fields);
  let input = {};

  for (let i = 0; i < field_key.length - 1; i++) {
    let temp = document.createElement("input");
    temp.setAttribute("id", fields[field_key[i]]);
    temp.style.width = (data[fields[field_key[i]]] + "").length * 12 + "px";
    temp.disabled = true;
    temp.style.fontSize = "17px";
    temp.style.fontFamily = "Consolas";
    temp.value = data[fields[field_key[i]]];
    input[field_key[i]] = temp;
  }
  //console.log(input);
  let table = document.createElement("table");
  table.setAttribute("id", name + "Profile");
  let tbody = document.createElement("tbody");
  table.appendChild(tbody);

  for (let i = 0; i < field_key.length - 1; i++) {
    let tr = document.createElement("tr");
    tr.setAttribute("id", field_key[i]);
    tbody.appendChild(tr);

    for (let j = 0; j < 2; j++) {
      let td = document.createElement("td");
      if (j === 0) {
        td.setAttribute("class", "name");
        td.appendChild(document.createTextNode(field_key[i] + "  :-"));
        tr.appendChild(td);
      }
      if (j === 1) {
        td.setAttribute("class", "value");
        td.style.paddingLeft = "100px";
        td.style.paddingBottom = "10px";
        td.appendChild(input[field_key[i]]);
        tr.appendChild(td);
      }
    }
  }
  element.appendChild(table);
};

export let displayAsTable = function (fields, data, element, name) {
  //tb[name] = data;

  removeAllChildNodes(element);
  element.setAttribute("align", "center");
  element.style.fontFamily = "consolas";
  element.style.fontSize = "15px";
  element.style.paddingTop = "50px";

  let field_key = Object.keys(fields);

  let table = document.createElement("table");
  table.setAttribute("id", name + " data");

  let thead = document.createElement("thead");
  table.appendChild(thead);

  let fltr = document.createElement("tr");
  fltr.id = "Filter";

  let tbody = document.createElement("tbody");
  table.appendChild(tbody);

  let len = Object.create(data[0]);

  //console.log(len);

  for (let i = 0; i < field_key.length - 1; i++) {
    let th = document.createElement("th");
    th.setAttribute("id", field_key[i]);
    th.setAttribute("class", "header");
    th.style.fontFamily = "Times new Roman";
    th.appendChild(document.createTextNode(field_key[i]));
    thead.appendChild(th);

    len[fields[field_key[i]]] = field_key[i].length;

    if (field_key[i] === "Available" || field_key[i] === "Working") {
      let cell = document.createElement("td");
      let select = document.createElement("select");
      select.className = "Filter";
      select.id = fields[field_key[i]];

      let def = document.createElement("option");
      def.value = "";
      def.innerText = "-";
      def.selected = true;
      select.appendChild(def);

      let opt1 = document.createElement("option");
      opt1.value = "Yes";
      opt1.innerText = "Yes";
      select.appendChild(opt1);
      let opt2 = document.createElement("option");
      opt2.value = "No";
      opt2.innerText = "No";
      select.appendChild(opt2);

      select.addEventListener("change", (e) => filter(e));

      cell.appendChild(select);
      fltr.appendChild(cell);
    } else {
      let cell = document.createElement("td");
      let ip = document.createElement("input");
      ip.className = "Filter";
      ip.style.fontFamily = "Times New Roman";
      ip.id = fields[field_key[i]];
      ip.placeholder = field_key[i];

      if (name === "Employee" && fields[field_key[i]] === "emp_Id")
        ip.addEventListener("keyup", (e) => {
          if (e.key === "Enter") new User().getPlus(e.target.value);
        });
      else ip.addEventListener("keyup", (e) => filter(e));

      cell.appendChild(ip);
      fltr.appendChild(cell);
    }
  }
  thead.appendChild(fltr);
  // console.log("th :");
  // console.log(len);
  //console.log(name);
  // console.log(
  //   sessionStorage.getItem("who") + sessionStorage.getItem("session")
  // );

  for (let i = 0; i < data.length; i++) {
    if (name == "Session" && sessionStorage.getItem("who") === "user") {
      //console.log(i + " " + JSON.stringify(data[i]));
      let temp = JSON.parse(sessionStorage.getItem("session"));

      if (data[i][fields["emp_Id"]] !== temp[fields["emp_Id"]]) continue;
    }

    let tr = document.createElement("tr");

    if (name !== "Session") tr.setAttribute("id", data[i][fields["Id"]]);
    else
      tr.setAttribute(
        "id",
        data[i][fields["emp_Id"]] +
          data[i][fields["comp_Id"]] +
          data[i][fields["Login Date"]] +
          data[i][fields["Login Time"]]
      );
    tbody.appendChild(tr);

    //tr.setAttribute("id", field_key[i]);
    //tr.appendChild(document.createTextNode(fields[field_key[i]]));

    for (let j = 0; j < field_key.length - 1; j++) {
      let obj = data[i];
      let td = document.createElement("td");

      if (
        obj[fields[field_key[j]]] !== undefined &&
        len[fields[field_key[j]]] < obj[fields[field_key[j]]].toString().length
      )
        len[fields[field_key[j]]] = obj[fields[field_key[j]]].toString().length;

      if (
        fields[field_key[j]] === "working" ||
        fields[field_key[j]] === "available"
      ) {
        let select = document.createElement("select");
        select.className = fields[field_key[j]];
        let opt1 = document.createElement("option");
        opt1.value = obj[fields[field_key[j]]];
        opt1.innerText = obj[fields[field_key[j]]];
        select.appendChild(opt1);
        let opt2 = document.createElement("option");
        opt2.value = obj[fields[field_key[j]]] === "Yes" ? "No" : "Yes";
        opt2.innerText = obj[fields[field_key[j]]] === "Yes" ? "No" : "Yes";
        select.appendChild(opt2);
        td.appendChild(select);
        tr.appendChild(td);
      } else {
        let ip = document.createElement("input");
        ip.className = fields[field_key[j]];
        ip.type = "text";
        ip.disabled = true;
        ip.style.height = "22px";
        ip.style.width = (obj[fields[field_key[j]]] + "").length * 9 + "px";
        ip.style.fontSize = "15px";
        ip.style.fontFamily = "Consolas";
        ip.value =
          obj[fields[field_key[j]]] == undefined ||
          obj[fields[field_key[j]]] == null
            ? ""
            : obj[fields[field_key[j]]];

        td.appendChild(ip);
        tr.appendChild(td);
      }
    }
    // console.log("td");
    // console.log(len);
  }
  //console.log(len);
  element.appendChild(table);
  for (let i = 0; i < field_key.length; i++) {
    let els = document.getElementsByClassName(fields[field_key[i]]);

    Array.prototype.forEach.call(els, (el) => {
      let factor = Number(el.style.fontSize.slice(0, -2));
      el.style.width = len[fields[field_key[i]]] * 9 + "px";
      // console.log("before");
      // console.log(el.style.width);
      // //el.getAttribute("style").width = len[fields[field_key[i]]];
      // console.log("after");
      // console.log(el.style.width);
    });
  }

  let els = document.getElementsByClassName("Filter");
  Array.prototype.forEach.call(els, (el) => {
    el.style.width = len[el.id] * 9 + "px";
  });
};

/* export let getObject = (Class) => {
  switch (Class) {
    case "Admin":
      return new D.Admin();
    case "Employee":
      return new D.User();
    case "System":
      return new D.System();
    case "Session":
      return new D.Session();
  }
}; */

function filter(event) {
  let filter_value = event.target.value;
  let column = event.target.id;
  let rows = document.getElementsByClassName(column);

  if (event.key === "Enter") {
    Array.prototype.forEach.call(rows, (row) => {
      if (filter_value === "") row.parentElement.parentElement.hidden = false;

      if (filter_value !== "" && row.value !== filter_value)
        row.parentElement.parentElement.hidden = true;
    });
  } else {
    Array.prototype.forEach.call(rows, (row) => {
      if (filter_value === "") row.parentElement.parentElement.hidden = false;
      else if (filter_value !== "" && !row.value.includes(filter_value))
        row.parentElement.parentElement.hidden = true;
      else if (filter_value !== "" && row.value.includes(filter_value))
        row.parentElement.parentElement.hidden = false;
    });
  }
}

export function removeAllChildNodes(parent) {
  while (parent.firstChild) {
    parent.removeChild(parent.firstChild);
  }
}
