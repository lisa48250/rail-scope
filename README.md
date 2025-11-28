# 🚆 RailScope 火車查詢系統
[RailScope 火車查詢系統url(後端尚未部屬)](https://lisa48250.github.io/rail-scope/)  
>RailScope 是一個 Spring Boot + MyBatis + SQL Server 建置的火車班次查詢系統。
>前端使用 原生 JavaScript + dataset 狀態管理 完成站點選擇、條件查詢與彈跳視窗顯示班次結果。  
- 進入頁面，選擇出發即抵達車站
<img width="878" height="375" alt="螢幕擷取畫面 2025-11-26 193530" src="https://github.com/user-attachments/assets/d564eb62-a08b-4bc4-af27-b18d5ad0f013" />

- 查詢各縣市車站
<img width="857" height="376" alt="image" src="https://github.com/user-attachments/assets/2e008dd6-37be-4051-be78-37b7d5c76e31" />

- 查詢火車班次
<img width="853" height="413" alt="image" src="https://github.com/user-attachments/assets/5929c7be-38ef-434e-997e-d0cdf53e320d" />


## 📌 專案功能
✔已完成
- 各縣市對應車站查詢
- 起點與終點站選擇（Popup 彈出視窗）
- 自動判斷方向（北上 = 0 / 南下 = 1）
- 火車班次查詢

🔧 開發中
- 增加查詢條件-直達 / 轉乘
- 增加查詢條件-車種查詢
- 增加查詢條件-出發時間 / 抵達時間查詢
- 增加查詢條件-顯示車廂擁擠程度
- 實作換日判斷（跨日車次）


## 🧱 系統架構


Frontend (HTML / CSS / JS)  
      │  fetch API  
      ▼  
Spring Boot Controller  
      │  呼叫 Service  
      ▼  
Service Layer（商業邏輯、校驗、direction 判斷、transaction）  
      │  Repository  
      ▼  
Repository（封裝資料存取）  
      │  MyBatis Mapper  
      ▼  
MyBatis Mapper（SQL）  
      │  JDBC  
      ▼  
SQL Server  



## 🔧 使用技術
Backend
- Java 17
- Spring Boot 3
- Spring Web
- Spring JDBC
- MyBatis
- SQL Server
- Slf4j（Logging）
- Validator（@Valid）
- Lombok
- Maven

Frontend
- HTML
- CSS
- JavaScript（fetch）

DB
- SQLServer

Tools
- Postman
- Eclipse
- VS Code

## 🚀 未來規劃（Future Improvements）
- 串接 TDC運輸資料流通 火車時刻表 API（實際資料來源）
- 後端部署至 Railway + 前端 GitHub Pages 完整串接
- 使用前端框架（React）優化 UI

## 📬 聯絡方式
E-mail: lisa48250@gmail.com  
Mobile: 0926-149-393  
如對本專案有任何建議，歡迎提出！
