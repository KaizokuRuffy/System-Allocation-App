export var swap_Options = (select) => {
  // console.log(select);
  // console.log(select.options);
  if (select.selectedIndex !== 0)
    select.insertBefore(select.options[1], select.options[0]);
};

export var togglePassword = () => {
  let icon = document.getElementsByTagName("i");
  //console.log(icon);
  Array.prototype.forEach.call(icon, (item) => {
    //console.log("Event listener added");
    item.addEventListener("click", (e) => {
      if (e.target.previousElementSibling.type === "password") {
        e.target.previousElementSibling.type = "text";
        e.target.classList.toggle("bi-eye");
      } else if (e.target.previousElementSibling.type === "text") {
        e.target.previousElementSibling.type = "password";
        //console.log(e.target.classList);
        e.target.classList.toggle("bi-eye");
      }
    });
  });

  /* //console.log(document.querySelector("#togglePassword"));
  document.querySelector("#togglePassword").addEventListener("click", (e) => {
    //console.log(e.target.previousElementSibling);
    if (e.target.previousElementSibling.type === "password") {
      e.target.previousElementSibling.type = "text";
      e.target.classList.toggle("bi-eye");
    } else if (e.target.previousElementSibling.type === "text") {
      e.target.previousElementSibling.type = "password";
      //console.log(e.target.classList);
      e.target.classList.toggle("bi-eye");
    }
  }); */
};
