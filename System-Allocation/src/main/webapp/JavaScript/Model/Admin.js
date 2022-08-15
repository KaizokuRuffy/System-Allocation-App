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

export class Admin {
  constructor(data, request, response, XHR) {
    this.data = data;
    this.response = response;
    this.request = request;
    this.XHR = XHR;
  }

  isPresent() {
    this.request = new U.ReqBuilder()
      .setMethod(GET)
      .setHeader(new U.HeaderBuilder().setAccept(Accept.text).getHeader())
      .setUrl(
        new U.UrlBuilder()
          .setContext(U.Context)
          .setServlet(U.Controller.Admin)
          .setPath("adminPresent")
          .getUrl()
      )
      .getReq();
    this.response = new U.Res();
    this.XHR = new U.XHR(this.request, this.response);

    this.response = this.XHR.sendRequest();

    return this.response;
  }
  create() {
    this.request = new U.ReqBuilder()
      .setMethod(GET)
      .setUrl(
        new U.UrlBuilder()
          .setContext(U.Context)
          .setServlet(U.Controller.Admin)
          .setPath("create")
          .getUrl()
      )
      .getReq();

    this.response = new U.Res();

    this.XHR = new U.XHR(this.request, this.response);
    this.response = this.XHR.sendRequest();

    return this.response();
  }
  login() {
    this.data = new D.AdminBuilder()
      .setId(Number(U.gEBI(U.Admin.Id).value))
      .setPassword(U.gEBI(U.Admin.Password).value)
      .getAdmin();

    //console.log(sessionStorage.getItem(U.Admin.Id));
    /* this.data = new D.AdminBuilder()
      .setId(U.gEBI(U.Admin.Id).value)
      .setPassword(U.gEBI(U.Admin.Pass).value)
      .getAdmin(); */
    //console.log("Before removing " + JSON.stringify(this.data));
    //this.data.remove();
    //console.log("After removing" + JSON.stringify(this.data));
    let params =
      "?" +
      U.Admin.Id +
      "=" +
      this.data[U.Admin.Id] +
      "&" +
      U.Admin.Password +
      "=" +
      this.data[U.Admin.Password];

    //console.log(params);
    this.request = new U.ReqBuilder()
      .setMethod(POST)
      .setHeader(
        new U.HeaderBuilder()
          .setContent(Content_type.urlencoded)
          .setAccept(Accept.text)
          .getHeader()
      )
      .setUrl(
        new U.UrlBuilder()
          .setContext(U.Context)
          .setServlet(U.Controller.Admin)
          .setPath("adminLogin")
          .setParams(params)
          .getUrl()
      )
      .getReq();

    this.response = new U.Res();
    this.XHR = new U.XHR(this.request, this.response);
    //console.log(JSON.stringify(this.XHR));
    this.response = this.XHR.sendRequest();
    //console.log(this.response);
    if (this.response.status === 200) {
      sessionStorage.setItem(U.Admin.Id, this.data.admin_Id);
      sessionStorage.setItem("who", "admin");
    }

    return this.response;
  }

  get() {
    let params = "?" + U.Admin.Id + "=" + sessionStorage.getItem(U.Admin.Id);
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
          .setServlet(U.Controller.Admin)
          .setPath("getAdmin")
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
    return this.response;
  }

  logout() {
    this.request = new U.ReqBuilder()
      .setMethod(GET)
      .setUrl(
        new U.UrlBuilder()
          .setContext(U.Context)
          .setServlet(U.Controller.Admin)
          .setPath("adminLogout")
          .getUrl()
      )
      .getReq();
    this.response = new U.Res();
    this.XHR = new U.XHR(this.request, this.response);

    this.response = this.XHR.sendRequest();

    if (this.response.status === 200) sessionStorage.clear();

    return this.response;
  }
  add() {
    this.data = new D.AdminBuilder()
      .setId(Number(U.gEBI(U.Admin.Id).value))
      .setName(U.gEBI(U.Admin.Name).value)
      .setEmail(U.gEBI(U.Admin.Email).value)
      .setContactNo(U.gEBI(U.Admin["Mobile No"]).value)
      .setPassword(U.gEBI(U.Admin.Password).value)
      .getAdmin();

    this.request = new U.ReqBuilder()
      .setMethod(POST)
      .setHeader(new U.HeaderBuilder().setContent(Content_type.json))
      .setUrl(
        new U.UrlBuilder()
          .setContext(U.Context)
          .setServlet(U.Controller.Admin)
          .setPath("addAdmin")
          .getUrl()
      )
      .setBody(JSON.stringify(this.data))
      .getReq();

    this.response = new U.Res();

    this.XHR = new U.XHR(this.request, this.response);
    this.response = this.XHR.sendRequest();
    return this.response;
  }
}