// ===== Register POST =====

function registerUser() {
  const registerData = {
    fullName: $("#registerUsers").val(),
    email: $("#registerEmail").val(),
    password: $("#registerPassword").val(),
  };
  $.ajax({
    type: "POST",
    url: "/api/v1/todo/users/register",
    data: JSON.stringify(registerData),
    contentType: "application/json",
    success: function (response) {
      alert("Success to Register");
      $("#modalRegister").modal("hide");
      $("#modalLogin").modal("show");
    },
    error: function () {
      alert("Failed to Register try again");
    },
  });
}

// ===== Function to set a cookie =====

function setCookie(name, value, days) {
  var expires = "";
  if (days) {
    var date = new Date();
    date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
    expires = "; expires=" + date.toUTCString();
  }
  document.cookie = name + "=" + (value || "") + expires + "; path=/";
}

// ===== Function to get a cookie =====

function getCookie(name) {
  var nameEQ = name + "=";
  var ca = document.cookie.split(";");
  for (var i = 0; i < ca.length; i++) {
    var c = ca[i];
    while (c.charAt(0) == " ") c = c.substring(1, c.length);
    if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
  }
  console.log(name + " ini name pada login.html");
  return null;
}

// ===== Login POST =====

function loginUser() {
  const loginData = {
    email: $("#loginEmail").val(),
    password: $("#loginPassword").val(),
  };
  $.ajax({
    type: "POST",
    url: "/api/v1/todo/users/login",
    data: JSON.stringify(loginData),
    contentType: "application/json",
    success: function (response) {
      alert("Login Successful");
      setCookie("token", response.token, 7);
      $("#modalLogin").modal("hide");
      window.location.href = "/todo";
    },
    error: function () {
      alert("Failed to login. Please check your credentials and try again.");
    },
  });
}


// ===== Function for button Login and Register =====

function getCookie(name) {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) {
    return parts.pop().split(";").shift();
  }
  return null;
}

function checkTokenAndToggleButtons() {
  const token = getCookie("token");
  const registerButton = document.getElementById("registerButton");
  const loginButton = document.getElementById("loginButton");

  if (token) {
    registerButton.style.display = "none";
    loginButton.style.display = "none";
  } else {
    registerButton.style.display = "block";
    loginButton.style.display = "block";
  }
}

document.addEventListener("DOMContentLoaded", checkTokenAndToggleButtons);
