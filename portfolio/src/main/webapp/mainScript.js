
async function getDataFromServlet() {
  fetch('/data').then(response => response.json()).then((data) => {
      document.getElementById("data-container").innerText = data.comments[0];
  });
}
