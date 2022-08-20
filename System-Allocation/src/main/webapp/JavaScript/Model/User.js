import * as D from "./Data/D.js";
import * as U from "./Util.js";

import { GET, Accept, POST, Content_type, PUT } from "./M.js";

/* export const GET = "GET";
export const POST = "POST";
export const PUT = "PUT";
const DELETE = "DELETE";
export const Content_type = {
  urlencoded: "application/x-www-form-urlencoded",
  json: "application/json",
};
export const Accept = {
  text: "text/plain",
  json: "application/json",
}; */

export class User {
  constructor(data, request, response) {
    this.data = data;
    this.response = response;
    this.request = request;
  }
  login(comp_Id) {
    let params =
      "?" +
      U.User.Id +
      "=" +
      U.gEBI(U.User.Id).value +
      "&" +
      U.User.Password +
      "=" +
      U.gEBI(U.User.Password).value;

    this.data = new D.SessionBuilder()
      .setEmp_Id(Number(U.gEBI(U.User.Id).value))
      .setComp_Id(Number(comp_Id))
      .setLogIn_Date(U.getDate())
      .setLogIn_Time(U.getTime())
      .getSession();

    this.request = new U.ReqBuilder()
      .setMethod(POST)
      .setHeader(
        new U.HeaderBuilder().setContent(Content_type.json).getHeader()
      )
      .setUrl(
        new U.UrlBuilder()
          .setContext(U.Context)
          .setServlet(U.Controller.User)
          .setPath("userLogin")
          .setParams(params)
          .getUrl()
      )
      .setBody(JSON.stringify(this.data))
      .getReq();

    this.response = new U.Res();

    this.XHR = new U.XHR(this.request, this.response);
    this.response = this.XHR.sendRequest();

    if (
      this.response.status === 200 &&
      !this.response.body.includes("comp_Id")
    ) {
      afterLogin(comp_Id);
      sessionStorage.setItem("session", JSON.stringify(this.data));
      sessionStorage.setItem("who", "user");
    }
    //sessionStorage.clear();

    return this.response;
  }
  logout() {
    this.data = JSON.parse(sessionStorage.getItem("session"));

    this.data[U.Session["Logout Date"]] = U.getDate();
    this.data[U.Session["Logout Time"]] = U.getTime();
    //console.log(this.data);

    this.request = new U.ReqBuilder()
      .setMethod(POST)
      .setHeader(
        new U.HeaderBuilder().setContent(Content_type.json).getHeader()
      )
      .setUrl(
        new U.UrlBuilder()
          .setContext(U.Context)
          .setServlet(U.Controller.User)
          .setPath("userLogout")
          .getUrl()
      )
      .setBody(JSON.stringify(this.data))
      .getReq();

    this.response = new U.Res();

    this.XHR = new U.XHR(this.request, this.response);
    this.response = this.XHR.sendRequest();

    if (this.response.status === 200) sessionStorage.clear();

    return this.response;
  }
  getAll() {
    this.request = new U.ReqBuilder()
      .setMethod(GET)
      .setHeader(new U.HeaderBuilder().setAccept(Accept.json).getHeader())
      .setUrl(
        new U.UrlBuilder()
          .setContext(U.Context)
          .setServlet(U.Controller.User)
          .setPath("getAllUsers")
          .getUrl()
      )
      .getReq();
    this.response = new U.Res();
    this.XHR = new U.XHR(this.request, this.response);

    this.response = this.XHR.sendRequest();

    return this.response;
  }
  get() {
    //console.log(JSON.parse(sessionStorage.getItem("session")));
    let params =
      "?" +
      U.User.Id +
      "=" +
      JSON.parse(sessionStorage.getItem("session"))[U.Session.emp_Id];
    //let params = "?" + U.Admin.Id + "=" + 1;
    this.request = new U.ReqBuilder()
      .setMethod(GET)
      .setHeader(
        new U.HeaderBuilder()
          .setContent(Content_type.urlencoded)
          .setAccept(Content_type.json)
          .getHeader()
      )
      .setUrl(
        new U.UrlBuilder()
          .setContext(U.Context)
          .setServlet(U.Controller.User)
          .setPath("getUser")
          .setParams(params)
          .getUrl()
      )
      .getReq();

    //console.log(this.request.header);
    this.response = new U.Res();
    this.XHR = new U.XHR(this.request, this.response);
    //console.log(JSON.stringify(this.XHR));
    this.response = this.XHR.sendRequest();
    //console.log(this.response);
    //console.log(this.response.body);
    return this.response;
  }
  getPlus(emp_Id) {
    let params = "?" + U.User.Id + "=" + emp_Id;
    //let params = "?" + U.Admin.Id + "=" + 1;
    this.request = new U.ReqBuilder()
      .setMethod(GET)
      .setHeader(
        new U.HeaderBuilder()
          .setContent(Content_type.urlencoded)
          .setAccept(Content_type.json)
          .getHeader()
      )
      .setUrl(
        new U.UrlBuilder()
          .setContext(U.Context)
          .setServlet(U.Controller.User)
          .setPath("getUser")
          .setParams(params)
          .getUrl()
      )
      .getReq();

    //console.log(this.request.header);
    this.response = new U.Res();
    this.XHR = new U.XHR(this.request, this.response);
    //console.log(JSON.stringify(this.XHR));
    this.response = this.XHR.sendRequest();
    //console.log(this.response);
    //console.log(this.response.body);
    return this.response;
  }
  add() {
    let id = U.gEBI(U.User.Id).value;

    this.data = new D.UserBuilder()
      .setId(id === "" ? -1 : Number(id))
      .setName(U.gEBI(U.User.Name).value)
      .setAdhaarId(U.gEBI(U.User.AdhaarId).value)
      .setEmail(U.gEBI(U.User.Email).value)
      .setMobileNo(U.gEBI(U.User["Mobile No"]).value)
      .setPassword(U.gEBI(U.User.Password).value)
      .setRole(U.gEBI(U.User.Role).value)
      .setWorkLoc(U.gEBI(U.User.WorkLoc).value)
      .setDept(U.gEBI(U.User.Dept).value)
      .getUser();

    this.request = new U.ReqBuilder()
      .setMethod(POST)
      .setHeader(new U.HeaderBuilder().setContent(Content_type.json))
      .setBody(JSON.stringify(this.data))
      .setUrl(
        new U.UrlBuilder()
          .setContext(U.Context)
          .setServlet(U.Controller.User)
          .setPath("addUser")
          .getUrl()
      );
    //console.log(this.data);
    //console.log(this.request);
    this.response = new U.Res();
    this.XHR = new U.XHR(this.request, this.response);
    this.response = this.XHR.sendRequest();

    return this.response;
  }
}

let afterLogin = (CID) => {
  sessionStorage.removeItem(U.System.Id);
  sessionStorage.removeItem("Username");
  sessionStorage.removeItem("Available");

  localStorage.setItem(U.User.Id, U.gEBI(U.User.Id).value);
  localStorage.setItem(U.Session.comp_Id, CID);
  localStorage.setItem(U.Session["Login Time"], U.getTime());
  localStorage.setItem(U.Session["Login Date"], U.getDate());
};
