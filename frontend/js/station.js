//-------------------------------------後端服務-----------------------------------------------\\
const API_URL = "http://localhost:8080/api/stationInformation/all";

//-------------------------------------宣告全域變數-----------------------------------------------\\
const loadBtn = document.getElementById("loadBtn");
const tableBody = document.getElementById("stationTableBody");
const errorBox = document.getElementById("errorBox");


// 出發站按鈕
const setOffBtn = document.getElementById("setOffBtn");   
// 到達站按鈕
const arrivalBtn = document.getElementById("ArrivalBtn"); 
//交換btn
const roundTripBtn = document.getElementById("roundTripBtn");

//各縣市車站data
let stationData = [];
const cityMap = {}; 

//出發、抵達btn
const row1Btns = document.querySelectorAll(".row1Btn");

//顯示各縣市車站div
const popup = document.getElementById("popup");
//關閉-查詢車站btn
const closeBtn = document.querySelector(".close-btn");
//縣市
const cityListEl = document.getElementById("cityList");
//車站
const stationListEl = document.getElementById("stationList");

//時間btn
const timeBtn = document.getElementById("timeBtn");
const timeNowBtn = document.getElementById("timeNowBtn");

//"對號"、"非對號"btn
const vehicleTypeBtn = document.getElementById("vehicleTypeBtn");
//"出發"、"抵達"btn
const timeQueryBtn = document.getElementById("timeQueryBtn");
//"直達"、"轉乘"btn
const directOrConvertToBtn = document.getElementById("directOrConvertToBtn");
//-------------------------------------------------------------------------------------------\\

//------------------查詢全縣市車站------------------\\
window.addEventListener("DOMContentLoaded", () => {
  loadStations();
  fillNowTime();  // 預設載入現在時間
});

function loadStations() {
  console.log("開始載入車站資料…");

  fetch(API_URL)
    .then(response => {
      if (!response.ok) {
        throw new Error("HTTP error " + response.status);
      }
      return response.json();
    })
    .then(data => {
      stationData = data;   // <-- 儲存在全域變數
      console.log("資料載入成功，stationData =", stationData);
      stationData.forEach(item => {
        const city = item.cityName;
      
        // 若這個 cityName 尚未被建立，先給它一個空陣列
        if (!cityMap[city]) {
          cityMap[city] = [];
        }
      
        // 將車站塞入該縣市底下
        cityMap[city].push({
          stationName: item.stationName,
          stationId: item.stationId
        });
      });

    })
    .catch(err => {
      console.error("載入失敗：", err);
    });
}



//------------------查詢各縣市車站------------------\\
row1Btns.forEach(btn => {
  btn.addEventListener("click", showTable);
});

// 記錄「目前是誰打開 popup」：出發站 or 到達站
let currentTargetBtn = null;

// 顯示彈出視窗
function showTable() {
  popup.classList.remove("hidden");
  // 每次打開都重畫一次清單
  renderCityList();
}


// ===== 畫左邊：縣市清單 =====
function renderCityList() {
  cityListEl.innerHTML = "";      // 清空舊資料
  stationListEl.innerHTML = "";   // 清空右邊車站

  const cityNames = Object.keys(cityMap); // 取得所有縣市名稱

  cityNames.forEach((cityName, index) => {
    const btn = document.createElement("button");
    btn.className = "popup-item";
    btn.textContent = cityName;

    // 點某個縣市 → 右邊顯示該縣市車站
    btn.addEventListener("click", () => {
      // 先把其他縣市的 active 樣式移除
      document
        .querySelectorAll("#cityList .popup-item")
        .forEach(el => el.classList.remove("active"));

      // 標記目前這個為選中
      btn.classList.add("active");

      // 顯示該縣市所有車站
      renderStations(cityName);
    });

    cityListEl.appendChild(btn);

    // 預設選第一個縣市
    if (index === 0) {
      btn.classList.add("active");
      renderStations(cityName);
    }
  });
}

// ===== 畫右邊：某縣市的所有車站 =====
function renderStations(cityName) {
  stationListEl.innerHTML = ""; // 清空右邊

  const stations = cityMap[cityName] || [];

  stations.forEach(st => {
    const btn = document.createElement("button");
    btn.className = "popup-item";
    btn.textContent = st.stationName;

    // 點某個車站 → 把站名帶回出發/到達按鈕 + 關閉 popup
    btn.addEventListener("click", () => {
      if (currentTargetBtn) {
        currentTargetBtn.textContent = st.stationName; // 把站名塞回原本按鈕
        currentTargetBtn.dataset.stationId = st.stationId;
        console.log("st.stationName:"+st.stationName);
        console.log("st.stationId:"+st.stationId);
      }
      popup.classList.add("hidden"); // 關閉 popup
    });

    stationListEl.appendChild(btn);
  });
}

// ===== 綁定：點「出發站」按鈕時，打開 popup =====
setOffBtn.addEventListener("click", () => {
  currentTargetBtn = setOffBtn;  // 代表這次是要選「出發站」
  showTable();
});

// ===== 綁定：點「到達站」按鈕時，打開 popup =====
arrivalBtn.addEventListener("click", () => {
  currentTargetBtn = arrivalBtn; // 代表這次是要選「到達站」
  showTable();
});

// 關閉彈出視窗
closeBtn.addEventListener("click", () => {
  popup.classList.add("hidden");
});

// 點遮罩背景也能關閉（可選）
popup.addEventListener("click", (e) => {
  if (e.target === popup) {
    popup.classList.add("hidden");
  }
});


//------------------交換btn------------------\\
roundTripBtn.addEventListener("click", () => {
  // 1. 暫存 setOff 按鈕的資料
  const tempText = setOffBtn.textContent;
  const tempStationId = setOffBtn.dataset.stationId;
  const tempCityId = setOffBtn.dataset.cityId; // 若有 cityId 也交換

  // 2. setOffBtn ← ArrivalBtn
  setOffBtn.textContent = arrivalBtn.textContent;
  setOffBtn.dataset.stationId = arrivalBtn.dataset.stationId;
  if (arrivalBtn.dataset.cityId) {
    setOffBtn.dataset.cityId = arrivalBtn.dataset.cityId;
  }

  // 3. arrivalBtn ← 原本 setOffBtn
  arrivalBtn.textContent = tempText;
  arrivalBtn.dataset.stationId = tempStationId;
  if (tempCityId) {
    arrivalBtn.dataset.cityId = tempCityId;
  }
});



//------------------設定現在時間------------------\\

timeNowBtn.addEventListener("click", fillNowTime);


function fillNowTime() {

  // 取得現在時間
  const now = new Date();

  // 補零函式，例如 9 → "09"
  const pad = (n) => n.toString().padStart(2, "0");

  // 轉換成你要的格式：YYYY/MM/DD HH:mm
  const formattedTime =
    now.getFullYear() + "/" +
    pad(now.getMonth() + 1) + "/" +
    pad(now.getDate()) + " " +
    pad(now.getHours()) + " : " +
    pad(now.getMinutes());

  // 將字串顯示在按鈕上
  timeBtn.textContent = formattedTime;

  // 若你需要之後給後端 → 存在 value 或 dataset 裡（任選）
  timeBtn.dataset.datetime = formattedTime;        // 若你想用 dataset（推薦）
};

//------------------設定"對號"or"非對號"車種------------------\\
const vehicleOptions = [
  { text: "全部", value: null },
  { text: "對號", value: 1 },
  { text: "非對號", value: 0 }
];

// 記錄目前索引（從 0 = 全部 開始）
let vehicleIndex = 0;

// 點擊按鈕時切換文字 & data-value
vehicleTypeBtn.addEventListener("click", () => {
  // 往下一個選項
  vehicleIndex++;

  // 如果超過最後一項 → 從 0 重新開始
  if (vehicleIndex >= vehicleOptions.length) {
    vehicleIndex = 0;
  }

  // 取出新選項
  const option = vehicleOptions[vehicleIndex];

  // 更新按鈕顯示文字
  vehicleTypeBtn.textContent = option.text;

  // 更新 data-value（null 也能設定）
  vehicleTypeBtn.dataset.type = option.value;
});

//------------------設定"出發時間"or"抵達時間"------------------\\
timeQueryBtn.addEventListener("click", () => {
  const current = timeQueryBtn.dataset.timeType;
  
  if (current === "0") {
    // 切換成「抵達時間」
    timeQueryBtn.innerText = "抵達時間";
    timeQueryBtn.dataset.timeType = "1";
  } else {
    // 切換回「出發時間」
    timeQueryBtn.innerText = "出發時間";
    timeQueryBtn.dataset.timeType = "0";
  }
});

//------------------設定"直達"or"轉乘"------------------\\
directOrConvertToBtn.addEventListener("click", () => {
  const current = directOrConvertToBtn.dataset.mode;

  if (current === "0") {
    // 切換成「轉乘」
    directOrConvertToBtn.innerText = "轉乘";
    directOrConvertToBtn.dataset.mode = "1";
  } else {
    // 切換回「直達」
    directOrConvertToBtn.innerText = "直達";
    directOrConvertToBtn.dataset.mode = "0";
  }
});


//------------------查詢火車班次------------------\\
function test(){
  const setOffStationId = setOffBtn.dataset.stationId;
  const arrivalBtnStationId = arrivalBtn.dataset.stationId;
  console.log("------------------------");
  console.log("setOffStationId:"+setOffStationId);
  console.log("arrivalBtnStationId:"+arrivalBtnStationId);
}






//觸發站點查詢table
// row1Btns.forEach((btn) => {
//   btn.addEventListener("click", showTable);
// });

// function showTable() {
//   console.log("showTable");

//   // 1. 建立 div
//   const newDiv = document.createElement("div");
//   // 2. 設定內容（你可以放中文或 HTML）
//   newDiv.textContent = "這是 showTable 新增的內容";

//   // 3. 設定 style（可選）
//   newDiv.style.padding = "10px";
//   newDiv.style.backgroundColor = "#e5e7eb"; // Tailwind: bg-gray-200
//   newDiv.style.marginTop = "10px";
//   // 4. 加到畫面中（body 最後）
//   document.body.appendChild(newDiv);
// }

// loadBtn.addEventListener("click", loadStations);

// async function loadStations() {
//   // 清空 UI
//   errorBox.classList.add("hidden");
//   errorBox.textContent = "";
//   tableBody.innerHTML = "";

//   try {
//     // 呼叫後端 API
//     const response = await axios.get(API_URL);
//     const data = response.data;

//     console.log("後端回傳資料:", data);

//     // 檢查資料格式
//     if (!Array.isArray(data)) {
//       throw new Error("後端回傳格式不是陣列");
//     }

//     // 渲染表格
//     data.forEach((item) => {
//       const tr = document.createElement("tr");
//       tr.className = "border-b";

//       tr.innerHTML = `
//         <td class="px-4 py-2">${item.cityName}</td>
//         <td class="px-4 py-2">${item.stationName}</td>
//         <td class="px-4 py-2">${item.cityId}</td>
//         <td class="px-4 py-2">${item.stationId}</td>
//       `;

//       tableBody.appendChild(tr);
//     });
//   } catch (err) {
//     console.error("API 錯誤:", err);

//     // 錯誤訊息顯示在畫面上
//     const msg = err.response
//       ? `後端錯誤：HTTP ${err.response.status}`
//       : `連線錯誤：${err.message}`;

//     errorBox.textContent = msg;
//     errorBox.classList.remove("hidden");
//   }
// }
