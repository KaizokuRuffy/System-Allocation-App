export var swap_Options = (select) => {
  if (select.selectedIndex !== 0)
    select.insertBefore(select.options[1], select.options[0]);
};

export var togglePassword = () => {
  let icon = document.getElementsByTagName("i");
  Array.prototype.forEach.call(icon, (item) => {
    item.addEventListener("click", (e) => {
      if (e.target.previousElementSibling.type === "password") {
        e.target.previousElementSibling.type = "text";
        e.target.classList.toggle("bi-eye");
      } else if (e.target.previousElementSibling.type === "text") {
        e.target.previousElementSibling.type = "password";
        e.target.classList.toggle("bi-eye");
      }
    });
  });
};
