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

export class System {
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
          .setServlet(U.Controller.System)
          .setPath("getAllSystems")
          .getUrl()
      )
      .getReq();
    this.response = new U.Res();
    this.XHR = new U.XHR(this.request, this.response);

    this.response = this.XHR.sendRequest();

    return this.response;
  }
  add() {
    let id = U.gEBI(U.System.Id).value;

    this.data = new D.SystemBuilder()
      .setId(id === "" ? -1 : Number(id))
      .setMAC(U.gEBI(U.System.MAC).value)
      .setModel(U.gEBI(U.System.Model).value)
      .setPassword(U.gEBI(U.System.Password).value)
      .setWorking(U.gEBI(U.System.Working).value)
      .setAvailable(U.gEBI(U.System.Available).value)
      .setYear(U.gEBI(U.System.Year).value)
      .setLoc(U.gEBI(U.System.Location).value)
      .getSystem();

    this.request = new U.ReqBuilder()
      .setMethod(POST)
      .setHeader(new U.HeaderBuilder().setContent(Content_type.json))
      .setBody(JSON.stringify(this.data))
      .setUrl(
        new U.UrlBuilder()
          .setContext(U.Context)
          .setServlet(U.Controller.System)
          .setPath("addSystem")
          .getUrl()
      );

    //console.log(this.data);
    //console.log(this.request);
    this.response = new U.Res();
    this.XHR = new U.XHR(this.request, this.response);
    this.response = this.XHR.sendRequest();

    return this.response;
  }
  updateStatus(comp) {
    let params;

    if (comp[U.System.Working] != undefined)
      params =
        "?" +
        U.System.Id +
        "=" +
        comp[U.System.Id] +
        "&colName=" +
        U.System.Working +
        "&status=" +
        comp[U.System.Working];
    else if (comp[U.System.Available] != undefined)
      params =
        "?" +
        U.System.Id +
        "=" +
        comp[U.System.Id] +
        "&colName=" +
        U.System.Available +
        "&status=" +
        comp[U.System.Available];

    this.request = new U.ReqBuilder()
      .setMethod(PUT)
      .setHeader(
        new U.HeaderBuilder().setContent(Content_type.urlencoded).getHeader()
      )
      .setUrl(
        new U.UrlBuilder()
          .setContext(U.Context)
          .setServlet(U.Controller.System)
          .setPath("updateStatus")
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
