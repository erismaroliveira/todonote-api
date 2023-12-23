const url = "http://localhost:8080/api/v1/tasks/user";

function hideLoader() {
  document.getElementById("loading").style.display = "none";
}

function show(tasks) {
  let table = "";

  for (let task of tasks) {
    table +=
    `<tr>
      <td>${task.id}</td>
      <td>${task.description}</td>
      <td>${task.user.username}</td>
      <td>${task.user.id}</td>
    </tr>
    `;
  }

  document.getElementById("tasks").innerHTML = table;
}

async function getTasks(url) {
  let key = "Authorization";
  const response = await fetch(url, { 
    method: "GET",
    headers: new Headers({
      Authorization: localStorage.getItem(key),
    }),
  });

  const tasks = await response.json();

  if (tasks.length > 0)
    hideLoader();

  show(tasks);
}

document.addEventListener("DOMContentLoaded", function(event) {
  if (!localStorage.getItem("Authorization"))
    window.location = "/view/login.html";
});

getTasks(url);