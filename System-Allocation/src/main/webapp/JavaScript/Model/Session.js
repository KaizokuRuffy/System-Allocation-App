import * as D from "./Data/D.js";
import * as U from "./Util.js";
import { GET, Accept } from "./M.js";

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

export class Session {
  constructor(data, request, response) {
    this.data = data;
    this.response = response;
    this.request = request;
  }
  getAll() {
    this.request = new U.ReqBuilder()
      .setMethod(GET)
      .setHeader(new U.HeaderBuilder().setAccept(Accept.json).getHeader())
      .setUrl(
        new U.UrlBuilder()
          .setContext(U.Context)
          .setServlet(U.Controller.Session)
          .setPath("getAllSession")
          .getUrl()
      )
      .getReq();
    this.response = new U.Res();
    this.XHR = new U.XHR(this.request, this.response);

    this.response = this.XHR.sendRequest();

    return this.response;
  }
  getEmpSession(emp_Id) {
    let params = "?" + U.User.Id + "=" + emp_Id;

    this.request = new U.ReqBuilder()
      .setMethod(GET)
      .setHeader(new U.HeaderBuilder().setAccept(Accept.json).getHeader())
      .setUrl(
        new U.UrlBuilder()
          .setContext(U.Context)
          .setServlet(U.Controller.Session)
          .setPath("getEmpSession")
          .setParams(params)
          .getUrl()
      )
      .getReq();
    this.response = new U.Res();
    this.XHR = new U.XHR(this.request, this.response);

    this.response = this.XHR.sendRequest();

    return this.response;
  }
}
