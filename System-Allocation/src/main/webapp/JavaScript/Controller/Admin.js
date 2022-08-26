import * as M from "../Model/M.js";
import * as V from "../View/V.js";

export class Admin {
  constructor() {
    this.Model = new M.Admin();
    this.View = new V.Admin();
  }
  isPresent() {
    this.View.isPresent(this.Model.isPresent());
  }
  create() {
    this.View.create(this.Model.create());
  }
  login() {
    this.View.login(this.Model.login());
  }
  get() {
    this.View.get(this.Model.get());
  }

  logout() {
    this.View.logout(this.Model.logout());
  }
  add() {
<<<<<<< HEAD
=======
    // let isPresentResp = this.Model.isPresent();
    // let addResp = ;

    // if (isPresentResp.body.includes("No")) addResp.redirect = "../index.html";
    // else addResp.redirect = "../HTML/Admin.html";

>>>>>>> refs/remotes/origin/Form-Validation
    this.View.add(this.Model.add());
  }
}
