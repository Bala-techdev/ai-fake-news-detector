🧠 AI Fake News Detection System

An AI-powered web application that analyzes news content and determines whether it is Real, Fake, or Suspicious using advanced AI models.

---

🚀 Live Demo

(Add your deployed link here later)

---

📌 Features

✅ Analyze news using AI
✅ Detect Fake / Real / Suspicious content
✅ Confidence score with visualization
✅ AI-generated explanation
✅ Modern UI with dark theme
✅ Responsive design (mobile + desktop)
✅ Example news testing feature
✅ Fast API response

---

🛠️ Tech Stack

💻 Frontend

- React.js
- Tailwind CSS
- Axios

⚙️ Backend

- Spring Boot
- REST API

🗄️ Database

- MySQL

🤖 AI Integration

- Gemini API (Google AI)

---

🧩 Project Architecture

Frontend (React)
⬇
API Call
⬇
Spring Boot Backend
⬇
AI Processing (Gemini API)
⬇
Response (Verdict + Confidence + Explanation)
⬇
Frontend UI Display

---

📷 Screenshots

(Add your UI screenshot here)

Example:

![App Screenshot](./screenshots/home.png)

---

⚙️ Installation & Setup

1️⃣ Clone Repository

git clone https://github.com/YOUR_USERNAME/ai-fake-news-detector.git
cd ai-fake-news-detector

---

2️⃣ Backend Setup (Spring Boot)

cd backend
mvn clean install
mvn spring-boot:run

Configure "application.properties":

spring.datasource.url=jdbc:mysql://localhost:3306/ai_fake_news_db
spring.datasource.username=root
spring.datasource.password=yourpassword

app.ai.api-key=YOUR_GEMINI_API_KEY

---

3️⃣ Frontend Setup (React)

cd frontend
npm install
npm start

---

🔌 API Endpoint

POST /api/analyze

Request:

{
  "content": "News text here"
}

Response:

{
  "verdict": "FAKE",
  "confidence": 85,
  "explanation": "This claim lacks credible sources."
}

---

🎯 Use Cases

- Detect misinformation on social media
- Assist journalists in fact-checking
- Educational tool for students
- Government and public awareness systems

---

🚀 Future Enhancements

- User authentication (Login/Register)
- News history tracking
- Multi-language support
- AI keyword highlighting
- Browser extension integration

---

👨‍💻 Author

Bala S
Aspiring Software Engineer
Java Full Stack Developer

---

⭐ Support

If you like this project:

⭐ Star the repository
🍴 Fork it
📢 Share with others

---

📜 License

This project is open-source and available under the MIT License.
