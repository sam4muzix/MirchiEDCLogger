# 📱 Mirchi EDCLogger (Internal App)

🎙️ **Mirchi EDCLogger** is an internal Android application developed for **Radio Mirchi** as part of the **EDC Automation System**.  
It is used by RJs and backend systems to log, classify, and analyze real-time audience engagement via **calls and WhatsApp messages**.

---

## 🚀 Key Features

- 📞 **Automatic logging** of phone calls and WhatsApp messages
- 🧠 **Smart tagging** (gender, age group, location) via AI backend (Mistral 7B)
- 📊 Real-time **analytics dashboard** with swipeable views (Today / Week / Month)
- 📈 Live charts and **RJ leaderboard** with engagement counts
- 🌗 Custom **Mirchi-themed Jetpack Compose UI**
- 🔔 Notification service for background logging
- ☁️ Syncs logs to **Google Sheets** via Apps Script webhook

---

## 📁 Project Structure

```plaintext
app/
 ┣ src/
 ┃ ┗ main/
 ┃   ┣ java/com/mirchi/edclogger/
 ┃   ┃ ┣ ui/screens/     ← Compose screens: Home, Logs, Analytics, etc.
 ┃   ┃ ┣ services/       ← Notification logger, API logger, backend hooks
 ┃   ┃ ┣ utils/          ← Helper utilities (e.g., RJ timing, chart helpers)
 ┃   ┣ res/              ← Assets, icons, themes, styles
 ┣ build.gradle.kts      ← Kotlin DSL build configuration
 ┣ settings.gradle.kts
```

---

## 🧠 AI Auto-Tagger (In Development)

Integrated LLM-based classification using **Mistral 7B**, deployed via FastAPI:
- Inputs: `contact name`, `message text`
- Outputs: `gender`, `location`, `age_group`

Results will be logged automatically per call/message for better insights and reporting.

---

## 🧪 Usage Notes

- 📱 Designed for internal use within **Radio Mirchi** workflows
- 🔐 Not for public distribution or third-party use
- 🌐 Requires internal Google Sheet endpoint access (Apps Script ID secured)
- 🧩 Customizable per RJ schedule and station location

---

## 👤 Developer

**Sam L Raj (Shyam L Raj)**  
AI Systems • Audio Tech • Internal Tools  
GitHub: [@sam4muzix](https://github.com/sam4muzix)  
Contact: shyam4muzix@gmail.com

---

## ⚠️ Confidentiality Notice

This application and its source code are the **intellectual property of Radio Mirchi (ENIL)** and its authorized developers.  
Unauthorized distribution, reproduction, or reverse-engineering is strictly prohibited.
