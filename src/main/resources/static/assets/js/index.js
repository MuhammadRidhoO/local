// ===== Check Token JWT User =====

function checkToken() {
  var tokenUser = getCookie("token");
  if (!tokenUser) {
    window.location.href = "/";
  }
}

$(document).ready(function () {
  checkToken();
});

// ===== Get By Token JWT =====

function getCookie(name) {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) {
    return parts.pop().split(";").shift();
  }
  return null;
}

function fetchUserData() {
  var token = getCookie("token");

  if (!token) {
    console.error("No token found.");
    return;
  }
  $.ajax({
    url: "/api/v1/todo/userlogin",
    method: "GET",
    headers: {
      Authorization: "Bearer " + token,
    },
    success: function (response) {
      $("#todoList").empty();

      response.todo_List_Entities.forEach((task) => {
        const taskElement = `
              <div class="col">
                <div class="card">
                  <div class="card-body d-flex justify-content-between align-items-center">
                    <div class="d-flex align-items-center">
                      <input
                        class="form-check-input me-2"
                        type="checkbox"
                        ${task.isComplete ? "checked" : ""}
                        onchange="toggleComplete(this)"
                        data-id="${task.id}"
                      />
                      <p class="mb-0 ${task.isComplete ? "completed" : ""}">${task.subTitle}</p>
                    </div>
                    <div class="d-flex gap-2">
                      <button type="button" class="btn btn-success btn-sm" onclick="openModal(${
                        task.id
                      })">
                        Detail
                      </button>
                      <button type="button" class="btn btn-primary btn-sm" onclick="openEditModal(${
                        task.id
                      })">
                        Edit
                      </button>
                      <button type="button" class="btn btn-danger btn-sm"
                        data-id="${task.id}"
                        onclick="showDeleteConfirmModal(this)"
                      })">
                        Delete
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            `;
        $("#todoList").append(taskElement);
      });
    },
    error: function () {
      alert("Failed to retrieve user data");
    },
  });
}

$(document).ready(function () {
  checkToken();
  fetchUserData();
});

// ===== Get By Id =====

function openModal(id) {
  $.ajax({
    url: "/api/v1/todo/" + id,
    method: "GET",
    success: function (data) {
      $("#modalSubTitle").text(data.subTitle);
      $("#modalDescraption").text(data.descraption);
      $("#modalDaysBetween").text(formatDate(data.finishAt));
      updateModalButtons(data.isComplete);
      $("#modal").modal("show");
    },
  });
}

// ===== POST =====

function createTask() {
  var tokenUser = getCookie("token");
  if (!tokenUser) {
    console.error("No token found");
    alert("You need to log in to create a task.");
    return;
  }
  const taskData = {
    subTitle: $("#taskSubTitle").val(),
    descraption: $("#taskDescraption").val(),
    finishAt: $("#taskFinishAt").val(),
  };
  $.ajax({
    type: "POST",
    url: "/api/v1/todo/",
    data: JSON.stringify(taskData),
    contentType: "application/json",
    headers: {
      Authorization: "Bearer " + tokenUser,
    },
    success: function (response) {
      alert("Task created successfully");
      location.reload();
    },
    error: function () {
      alert("Failed to create task");
    },
  });
}

// ===== PATCH =====

function openEditModal(id) {
  $.ajax({
    url: "/api/v1/todo/" + id,
    method: "GET",
    success: function (data) {
      $("#editTaskSubTitle").val(data.subTitle);
      $("#editTaskDescription").val(data.descraption);
      $("#editTaskFinishAt").val(data.finishAt);
      $("#editTaskIsComplete").prop("checked", data.isComplete);
      $("#editTaskForm").data("id", id);
      $("#editModal").modal("show");
    },
    error: function () {
      alert("Failed to fetch task details.");
    },
  });
}

$(document).ready(function () {
  $("#editTaskForm").submit(function (event) {
    event.preventDefault();
    const id = $(this).data("id");
    const subTitle = $("#editTaskSubTitle").val();
    const description = $("#editTaskDescription").val();
    const finishAt = $("#editTaskFinishAt").val();
    const isComplete = $("#editTaskIsComplete").is(":checked");

    $.ajax({
      url: "/api/v1/todo/" + id,
      method: "PATCH",
      contentType: "application/json",
      data: JSON.stringify({
        subTitle: subTitle,
        descraption: description,
        finishAt: finishAt,
        isComplete: isComplete,
      }),
      success: function (data) {
        $("#editModal").modal("hide");
        location.reload();
      },
      error: function () {
        alert("Failed to update task.");
      },
    });
  });
});

// ===== PATCH Check Box =====

function toggleComplete(checkbox) {
  const id = checkbox.getAttribute("data-id");
  const isComplete = checkbox.checked;

  $.ajax({
    url: "/api/v1/todo/checkbox/" + id,
    method: "PATCH",
    contentType: "application/json",
    data: JSON.stringify({
      isComplete: isComplete,
    }),
    success: function (data) {
      console.log("Task status updated.");
      showAlert("successAlert");

      const taskElement = checkbox.closest(".card-body").querySelector(".mb-0");
      if (isComplete) {
        taskElement.classList.add("completed");
      } else {
        taskElement.classList.remove("completed");
      }
    },
    error: function () {
      alert("Failed to update task status.");
    },
  });
}
// ===== Fungsi Done dan Undone =====

function updateModalButtons(isComplete) {
  const undoneButton = document.getElementById("undoneButton");
  const doneButton = document.getElementById("doneButton");

  if (isComplete) {
    doneButton.style.display = "block";
    undoneButton.style.display = "none";
  } else {
    doneButton.style.display = "none";
    undoneButton.style.display = "block";
  }
}

// ===== DELETE =====

function deleteTask(id) {
  if (confirm("Are you sure you want to delete this task?")) {
    $.ajax({
      url: "/api/v1/todo/" + id,
      method: "DELETE",
      success: function () {
        showAlertDelete("successDeleteAlert");

        const taskElement = document.querySelector(`.task[data-id='${id}']`);
        if (taskElement) {
          taskElement.remove();
        }
      },
      error: function () {
        alert("Failed to delete task");
      },
    });
  }
}
// ===== Show Alert Bootstrap 5 =====

function showAlert(alertId) {
  const alertElement = document.getElementById(alertId);
  alertElement.classList.remove("d-none");
  alertElement.classList.add("show");

  setTimeout(function () {
    alertElement.classList.remove("show");
    alertElement.classList.add("d-none");
  }, 3000);
}

function showAlertDelete(alertIdDelete) {
  const alertElement = document.getElementById(alertIdDelete);
  alertElement.classList.remove("d-none");
  alertElement.classList.add("show");

  setTimeout(function () {
    alertElement.classList.remove("show");
    alertElement.classList.add("d-none");
  }, 3000);
}

// ===== Format Tanggal =====

function getOrdinalSuffix(n) {
  let s = ["th", "st", "nd", "rd"],
    v = n % 100;
  return n + (s[(v - 20) % 10] || s[v] || s[0]);
}

function formatDate(dateStr) {
  const date = new Date(dateStr);
  const day = getOrdinalSuffix(date.getDate());
  const month = date.toLocaleString("default", { month: "short" });
  const year = date.getFullYear();
  return `${day} ${month} ${year}`;
}

// ===== Notice Confirm Delete =====

let taskIdToDelete = null;

function showDeleteConfirmModal(button) {
  taskIdToDelete = button.getAttribute("data-id");
  const deleteConfirmModal = new bootstrap.Modal(
    document.getElementById("deleteConfirmModal")
  );
  deleteConfirmModal.show();
}

document
  .getElementById("confirmDeleteButton")
  .addEventListener("click", function () {
    $.ajax({
      url: "/api/v1/todo/" + taskIdToDelete,
      method: "DELETE",
      success: function () {
        showAlertDelete("successDeleteAlert");

        const taskElement = document.querySelector(
          `.task[data-id='${taskIdToDelete}']`
        );
        if (taskElement) {
          taskElement.remove();
        }
      },
      error: function () {
        alert("Failed to delete task");
      },
    });

    const deleteConfirmModal = bootstrap.Modal.getInstance(
      document.getElementById("deleteConfirmModal")
    );
    deleteConfirmModal.hide();
  });

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

// ===== Function to logout =====

function logoutUser() {
  setCookie("token", "", -1);

  window.location.href = "/";
}
