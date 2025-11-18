const API_URL = "http://localhost:8080/api/stationInformation/all";

const loadBtn = document.getElementById("loadBtn");
const tableBody = document.getElementById("stationTableBody");
const errorBox = document.getElementById("errorBox");

loadBtn.addEventListener("click", () => {
  errorBox.classList.add("hidden");
  errorBox.textContent = "";
  tableBody.innerHTML = "";

  fetch(API_URL)
    .then(response => {
      if (!response.ok) {
        throw new Error("HTTP error " + response.status);
      }
      return response.json();
    })
    .then(data => {
      if (!Array.isArray(data)) {
        throw new Error("回傳格式不是陣列");
      }

      data.forEach(item => {
        const tr = document.createElement("tr");
        tr.className = "border-b";

        const tdCityName = document.createElement("td");
        tdCityName.className = "px-4 py-2";
        tdCityName.textContent = item.cityName;

        const tdStationName = document.createElement("td");
        tdStationName.className = "px-4 py-2";
        tdStationName.textContent = item.stationName;

        const tdCityId = document.createElement("td");
        tdCityId.className = "px-4 py-2";
        tdCityId.textContent = item.cityId;

        const tdStationId = document.createElement("td");
        tdStationId.className = "px-4 py-2";
        tdStationId.textContent = item.stationId;

        tr.appendChild(tdCityName);
        tr.appendChild(tdStationName);
        tr.appendChild(tdCityId);
        tr.appendChild(tdStationId);

        tableBody.appendChild(tr);
      });
    })
    .catch(err => {
      console.error(err);
      errorBox.textContent = "載入失敗：" + err.message;
      errorBox.classList.remove("hidden");
    });
});
