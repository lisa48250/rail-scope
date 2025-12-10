//-------------------------------------後端服務-----------------------------------------------\\
const API_URL = "http://localhost:8080/api/stationInformation/all";
const API_URL_QUERY = "http://localhost:8080/api/queryTrainTimetable/query";

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
const dateBtn = document.getElementById("dateBtn");
const datePicker = document.getElementById("datePicker");

const timeBtn = document.getElementById("timeBtn");
const pickerOverlay = document.getElementById("customTimePicker");
const confirmBtn = document.getElementById("tpConfirmBtn");
const hourWheel = document.getElementById("tpHourWheel");
const minuteWheel = document.getElementById("tpMinuteWheel");
const timeNowBtn = document.getElementById("timeNowBtn");

//查詢按鈕
const queryBtn = document.getElementById("queryBtn");

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
  fillNowTime(); // 預設載入現在時間
});

function loadStations() {
  console.log("開始載入車站資料…");

  fetch(API_URL)
    .then((response) => {
      if (!response.ok) {
        throw new Error("HTTP error " + response.status);
      }
      return response.json();
    })
    .then((data) => {
      stationData = data.data; // <-- 儲存在全域變數
      console.log("資料載入成功，stationData =", stationData);
      stationData.forEach((item) => {
        const city = item.cityName;

        // 若這個 cityName 尚未被建立，先給它一個空陣列
        if (!cityMap[city]) {
          cityMap[city] = [];
        }
        // 將車站塞入該縣市底下
        cityMap[city].push({
          stationName: item.stationName,
          stationId: item.stationId,
          cityId: item.cityId,
        });
      });
    })
    .catch((err) => {
      console.error("載入失敗：", err);
    });
}

//------------------查詢各縣市車站------------------\\
// 記錄「目前是誰打開 popup」：出發站 or 到達站
let currentTargetBtn = null;

// 顯示彈出視窗
function showTable() {
  let selectedCityId = null;
  if (currentTargetBtn.dataset && currentTargetBtn.dataset.cityId) {
    selectedCityId = parseInt(currentTargetBtn.dataset.cityId, 10);
  }

  let selectedStationId = null;
  if (currentTargetBtn.dataset && currentTargetBtn.dataset.stationId) {
    selectedStationId = parseInt(currentTargetBtn.dataset.stationId, 10);
  }

  console.log(
    "selectedCityId =",
    selectedCityId,
    "selectedStationId =",
    selectedStationId
  );

  popup.classList.remove("hidden");
  renderCityList(selectedCityId, selectedStationId);
}

// ===== 畫左邊：縣市清單 =====
// selectedCityId: 目前按鈕上的 cityId（用來標記選中縣市）
// selectedStationId: 目前按鈕上的 stationId（之後傳給右邊用）
function renderCityList(selectedCityId, selectedStationId) {
  cityListEl.innerHTML = ""; // 清空舊資料
  stationListEl.innerHTML = ""; // 清空右邊車站

  const cityNames = Object.keys(cityMap); // 取得所有縣市名稱

  let hasActiveCity = false; // 用來判斷有沒有按到「指定的 cityId」

  cityNames.forEach((cityName, index) => {
    const btn = document.createElement("button");
    btn.className = "popup-item";
    btn.textContent = cityName;

    const stations = cityMap[cityName] || [];
    // 假設同一縣市的 station.cityId 都相同，取第一筆就好
    const cityIdOfThisButton = stations.length > 0 ? stations[0].cityId : null;

    // 點某個縣市 → 右邊顯示該縣市車站
    btn.addEventListener("click", () => {
      // 先把其他縣市的 active 樣式移除
      document
        .querySelectorAll("#cityList .popup-item")
        .forEach((el) => el.classList.remove("active"));

      // 標記目前這個為選中
      btn.classList.add("active");

      // 顯示該縣市所有車站（此時不需要特別標 station，selectedStationId 傳 null）
      renderStations(cityName, null);
    });

    // 判斷要不要預設標記「選中的城市」
    if (selectedCityId != null && cityIdOfThisButton === selectedCityId) {
      // 若 cityId 跟目前按鈕上的 cityId 相符 → 設為 active
      btn.classList.add("active");
      hasActiveCity = true;
      // 右邊車站用 selectedStationId 來標記「目前站」
      renderStations(cityName, selectedStationId);
    }

    cityListEl.appendChild(btn);

    // 如果完全沒有 selectedCityId（例如一開始沒有設定）
    // 就沿用原本「預設選第一個縣市」
    if (selectedCityId == null && index === 0) {
      btn.classList.add("active");
      renderStations(cityName, selectedStationId ?? null);
    }
  });

  // 如果有 selectedCityId 卻沒對應任何城市（資料不一致）
  // 就 fallback 到第一個城市
  if (selectedCityId != null && !hasActiveCity && cityNames.length > 0) {
    const firstBtn = cityListEl.querySelector(".popup-item");
    if (firstBtn) {
      firstBtn.classList.add("active");
      renderStations(cityNames[0], selectedStationId ?? null);
    }
  }
}

// ===== 畫右邊：某縣市的所有車站 =====
// cityName: 左邊選中的縣市
// selectedStationId: 要標記為選中的車站 ID（可能是出發或抵達按鈕上的）
function renderStations(cityName, selectedStationId) {
  stationListEl.innerHTML = ""; // 清空右邊

  const stations = cityMap[cityName] || [];

  stations.forEach((st) => {
    const btn = document.createElement("button");
    btn.className = "popup-item";
    btn.textContent = st.stationName;

    // 如果有指定 selectedStationId，且 stationId 相同 → 標記為選中
    if (selectedStationId != null && st.stationId === selectedStationId) {
      btn.classList.add("active");
    }

    // 點某個車站 → 把站名 & id 帶回出發/到達按鈕 + 關閉 popup
    btn.addEventListener("click", () => {
      if (currentTargetBtn) {
        // 把車站名稱塞回 setOffBtn / ArrivalBtn 的文字
        currentTargetBtn.textContent = st.stationName;
        // 更新按鈕身上的 cityId & stationId
        currentTargetBtn.dataset.cityId = st.cityId;
        currentTargetBtn.dataset.stationId = st.stationId;
      }
      popup.classList.add("hidden"); // 關閉 popup
    });

    stationListEl.appendChild(btn);
  });
}
// ===== 綁定：點「出發站」按鈕時，打開 popup =====
setOffBtn.addEventListener("click", () => {
  currentTargetBtn = setOffBtn; // 代表這次是要選「出發站」
  console.log(" 代表這次是要選「出發站」");
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

//--------------------------------處理時間--------------------------------\\

//設定現在時間
timeNowBtn.addEventListener("click", fillNowTime);

function fillNowTime() {
  // 取得現在時間
  const now = new Date();

  // 補零函式，例如 9 → "09"
  const pad = (n) => n.toString().padStart(2, "0");

  // 轉換格式：YYYY/MM/DD
  const formattedDate =
    now.getFullYear() +
    "/" +
    pad(now.getMonth() + 1) +
    "/" +
    pad(now.getDate());

  // 轉換格式：HH:mm
  const formattedTime = pad(now.getHours()) + " : " + pad(now.getMinutes());

  // 將字串顯示在按鈕上
  dateBtn.textContent = formattedDate;
  timeBtn.textContent = formattedTime;

  // 若你需要之後給後端 → 存在 value 或 dataset 裡（任選）
  dateBtn.dataset.datetime = formattedDate;
  timeBtn.dataset.datetime = formattedTime;
}

//=====設定點選日期btn=====
document.addEventListener("DOMContentLoaded", function () {
  if (!dateBtn || !datePicker) {
    console.error("dateBtn 或 datePicker 找不到");
    return;
  }

  const today = new Date();
  const maxDate = new Date();
  maxDate.setDate(today.getDate() + 30);

  const fp = flatpickr("#datePicker", {
    minDate: today,
    maxDate: maxDate,
    dateFormat: "Y/m/d",
    clickOpens: false, // 不用點 input，自行 open()
    position: "below right", // 往右下
  });

  // 點按鈕打開 datepicker
  dateBtn.addEventListener("click", () => {
    fp.open();
  });

  // 選完日期後把值顯示在按鈕上
  datePicker.addEventListener("change", (e) => {
    dateBtn.textContent = e.target.value; // ex: 2025/12/01
    dateBtn.dataset.datetime = e.target.value; // ex: 2025/12/01
  });
});

///=====設定點選時間btn=====
document.addEventListener("DOMContentLoaded", () => {
  // 配置
  const itemHeight = 40; // 每個選項的高度 (需配合 CSS)

  // 1. 初始化產生選項函數
  function initWheel(container, start, end) {
    // 清空除了 padding 以外的內容
    const paddings = container.querySelectorAll(".tp-padding");
    container.innerHTML = "";
    container.appendChild(paddings[0]); // 加回頂部 padding

    for (let i = start; i <= end; i++) {
      const div = document.createElement("div");
      div.className = "tp-item";
      div.textContent = i.toString().padStart(2, "0"); // 補零: 9 -> 09
      div.dataset.value = div.textContent;

      // 點擊該數字直接滾動到該位置
      div.addEventListener("click", (e) => {
        scrollToValue(container, e.target.dataset.value);
      });

      container.appendChild(div);
    }

    container.appendChild(paddings[1] || paddings[0].cloneNode(true)); // 加回底部 padding
  }

  // 2. 滾動到指定值的函數
  function scrollToValue(container, value) {
    const items = Array.from(container.querySelectorAll(".tp-item"));
    const targetIndex = items.findIndex((item) => item.dataset.value === value);
    if (targetIndex !== -1) {
      container.scrollTo({
        top: targetIndex * itemHeight,
        behavior: "smooth",
      });
    }
  }

  // 3. 獲取目前選中的值 (根據滾動高度計算)
  function getSelectedValue(container) {
    const scrollTop = container.scrollTop;
    const index = Math.round(scrollTop / itemHeight);
    const items = container.querySelectorAll(".tp-item");
    // 防止滑動過快超出範圍
    const safeIndex = Math.min(Math.max(index, 0), items.length - 1);
    return items[safeIndex].dataset.value;
  }

  // --- 初始化 ---
  initWheel(hourWheel, 0, 23); // 24小時
  initWheel(minuteWheel, 0, 59); // 60分

  // 4. 開啟選擇器事件
  timeBtn.addEventListener("click", (e) => {
    e.preventDefault(); // 防止 submit
    pickerOverlay.style.display = "flex";

    // 讀取目前按鈕上的值，如果有的話，滾動到該位置
    // 預設格式 HH:mm
    let currentVal = timeBtn.dataset.datetime;
    if (!currentVal) {
      const now = new Date();
      const h = now.getHours().toString().padStart(2, "0");
      const m = now.getMinutes().toString().padStart(2, "0");
      currentVal = `${h}:${m}`;
    }

    const [h, m] = currentVal.split(":");
    // setTimeout 確保 display:flex 後才滾動，否則 scrollTop 無效
    setTimeout(() => {
      scrollToValue(hourWheel, h);
      scrollToValue(minuteWheel, m);
    }, 10);
  });

  // 5. 點擊遮罩關閉 (可選)
  pickerOverlay.addEventListener("click", (e) => {
    if (e.target === pickerOverlay) {
      pickerOverlay.style.display = "none";
    }
  });

  // 6. 確認按鈕事件 (核心需求)
  confirmBtn.addEventListener("click", () => {
    const selectedHour = getSelectedValue(hourWheel);
    const selectedMinute = getSelectedValue(minuteWheel);

    const finalTime = `${selectedHour}:${selectedMinute}`;

    // 1. 顯示在 timeBtn 的 text 中
    timeBtn.innerText = finalTime;

    // 2. 值放入 timeBtn.dataset.datetime 中
    timeBtn.dataset.datetime = finalTime;

    // 關閉視窗
    pickerOverlay.style.display = "none";

    console.log(
      `已設定時間: ${finalTime}, Dataset: ${timeBtn.dataset.datetime}`
    );
  });

  // 視覺優化：當滾動停止時，自動高亮文字顏色 (這裡用简单的 css hover/active 模擬，若要精準變白字需配合 IntersectionObserver，為保持代碼輕量，此處主要依賴中間綠色條覆蓋)
  // 由於綠色條在中間，文字在上面，白色的字體效果可以透過 mix-blend-mode 或是讓綠色條透明度調整來達成。
  // 在此範例中，文字維持灰色，綠色條在下層，看起來就像圖片中的樣子。

  // 如果想要完全模仿圖中「選中字體變白」，可以在 scroll 事件中動態加入 active class，
  // 但考慮到效能與簡潔，目前的半透明綠色條效果已經非常接近。
});

//------------------設定"對號"or"非對號"車種------------------\\
const vehicleOptions = [
  { text: "全部", value: 0 },
  { text: "對號", value: 1 },
  { text: "非對號", value: 2 },
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
function test() {
  const setOffStationId = setOffBtn.dataset.stationId;
  const arrivalBtnStationId = arrivalBtn.dataset.stationId;
  console.log("------------------------");
  console.log("setOffStationId:" + setOffStationId);
  console.log("arrivalBtnStationId:" + arrivalBtnStationId);
}

//----------------------查詢豁車時刻表--------------------------------
queryBtn.addEventListener("click", () => {
  let stationStart = setOffBtn.dataset.stationId;
  let stationEnd = arrivalBtn.dataset.stationId;

  if (stationStart == stationEnd) {
    window.alert("請選擇不同起點及終點站");
    return;
  }
  // 送出查詢
  queryTrain(stationStart, stationEnd);
});

function queryTrain(stationStart, stationEnd) {
  const direction = Number(stationStart) > Number(stationEnd) ? "0" : "1";

  const queryTime = timeBtn.dataset.datetime || null; // 例如 "09:00"

  const reqVo = {
    stationStart: stationStart,
    stationEnd: stationEnd,
    direction: direction,
    timeType: timeQueryBtn.dataset.timeType,
    vehicleType: vehicleTypeBtn.dataset.type,
    date: dateBtn.dataset.datetime,
    routeType: directOrConvertToBtn.dataset.mode,
  };

  console.log("要傳給後端的 JSON:", reqVo);

  fetch(API_URL_QUERY, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(reqVo),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("HTTP 狀態碼：" + response.status);
      }
      return response.json();
    })
    .then((data) => {
      console.log("後端回傳:", data);

      const trains = Array.isArray(data)
        ? data
        : Array.isArray(data.data)
        ? data.data
        : [];

      if (!trains || trains.length === 0) {
        window.alert("查無班次");
        return;
      }

      // 把查詢時間一起丟給 modal
      openResultModal(trains, queryTime);
    })
    .catch((err) => {
      console.error("API 發生錯誤:", err);
      window.alert("查詢發生錯誤，請稍後再試");
    });
}

// 時間 "05:23:00" -> "05:23"
function formatTime(t) {
  if (!t) return "";
  return t.slice(0, 5);
}

// 時間差 "01:02:00" -> "1時2分"
function formatDuration(diff) {
  if (!diff) return "";
  const [h, m] = diff.split(":").map((v) => parseInt(v, 10));
  let s = "";
  if (h > 0) s += h + "時";
  if (m > 0) s += m + "分";
  return s || "0分";
}

// "HH:mm" 或 "HH:mm:ss" -> 轉成從 0:00 起算的總分鐘數
function timeToMinutes(t) {
  if (!t) return null;
  const parts = t.split(":").map((v) => parseInt(v, 10));
  const h = parts[0] || 0;
  const m = parts[1] || 0;
  return h * 60 + m;
}

// 產生一列班次 DOM
// 產生一列班次 DOM，第二個參數 isPast 控制是否為已過班次
function createTrainRow(item, isPast) {
  const row = document.createElement("div");
  row.className = "flex px-4 py-3 items-center justify-between";

  if (isPast) {
    row.classList.add("bg-gray-100", "text-gray-400");
  }

  // 車種顏色設定
  const yellowTypes = ["區間車", "區間快"];
  const isYellow = yellowTypes.includes(item.typeName);

  const typeColor = isYellow ? "text-blue-400" : "text-blue-700";
  const trainNoColor = isYellow ? "text-blue-400" : "text-blue-700";

  const statusHTML = isPast
    ? `<div class="text-red-500">已過站</div>`
    : `<div class="text-gray-500">準點</div>`;

  row.innerHTML = `
    <!-- 左邊：車種 + 車次 -->
    <div class="w-16 text-center leading-tight">
      <div class="text-xs font-bold ${typeColor}">${item.typeName}</div>
      <div class="text-xs ${trainNoColor}">${item.trainNo}</div>
    </div>

    <!-- 中間：時間 + 所需時間 -->
    <div class="flex-1 px-2">
      <div class="flex items-baseline gap-2">
        <div class="text-xl font-semibold">${formatTime(
          item.arrivalTimeStart
        )}</div>
        <span class="text-gray-400 text-sm">→</span>
        <div class="text-xl font-semibold">${formatTime(
          item.arrivalTimeEnd
        )}</div>
      </div>
      <div class="text-xs text-gray-500 mt-1">
        ${formatDuration(item.timeDiff)}
      </div>
    </div>

    <!-- 右邊：狀態 -->
    <div class="text-right text-xs leading-tight">
      ${statusHTML}
    </div>
  `;

  return row;
}

function openResultModal(trains, queryTimeStr) {
  const modal = document.getElementById("resultModal");
  const titleEl = document.getElementById("resultTitle");
  const listEl = document.getElementById("resultList");

  if (!Array.isArray(trains) || trains.length === 0) {
    alert("查無班次");
    return;
  }

  const first = trains[0];
  titleEl.textContent = `${first.stationNameStart} → ${first.stationNameEnd}`;

  listEl.innerHTML = "";

  // 查詢時間轉成分鐘，例如 "09:00" -> 540
  const queryMinutes =
    queryTimeStr && queryTimeStr.length >= 4
      ? timeToMinutes(queryTimeStr)
      : null;

  let firstFutureRow = null; // 第一個「尚未到站」的 row DOM

  trains.forEach((item) => {
    // 依 timeType 決定用哪一個時間欄位
    const timeRaw = timeQueryBtn.dataset.timeType === "0" ? item.arrivalTimeStart : item.arrivalTimeEnd;

    // 這裡用選到的時間作比較
    const timeStr = formatTime(timeRaw); // "HH:mm"
    const timeMinutes = timeToMinutes(timeStr);

    const isPast =
      queryMinutes != null && timeMinutes != null && timeMinutes < queryMinutes;

    const row = createTrainRow(item, isPast);
    listEl.appendChild(row);

    // 記錄第一個「未過」班次
    if (!isPast && !firstFutureRow) {
      firstFutureRow = row;
    }
  });

  // 顯示視窗
  modal.classList.remove("hidden");
  modal.classList.add("flex");

  // 自動捲到「第一個未過班次」
  // 若全部都已經過了，就不捲（保持在列表最上方）
  if (firstFutureRow) {
    // 確保 resultList 本身是 scroll 容器：例如有 max-h + overflow-y-auto
    firstFutureRow.scrollIntoView({
      block: "start",
      behavior: "auto",
    });
  }
}

// 點背景關閉（不需要 X）
document.getElementById("resultModal").addEventListener("click", (e) => {
  if (e.target.id === "resultModal") {
    e.currentTarget.classList.add("hidden");
    e.currentTarget.classList.remove("flex");
  }
});
