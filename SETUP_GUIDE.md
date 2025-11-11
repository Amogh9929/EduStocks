# 🚀 EduStocks Setup Guide

This guide will walk you through setting up EduStocks step by step.

## Prerequisites Checklist

- [ ] Node.js (v18+) installed
- [ ] Java JDK (17+) installed
- [ ] Maven installed
- [ ] Firebase account created
- [ ] Code editor (VS Code recommended)

## Step-by-Step Setup

### 1. Clone or Download the Project

```bash
# If using git
git clone <repository-url>
cd stock-learning-app
```

### 2. Firebase Project Setup

1. **Create Firebase Project**
   - Go to https://console.firebase.google.com
   - Click "Add project"
   - Name it "EduStocks" (or your preferred name)
   - Follow the setup wizard

2. **Enable Authentication**
   - In Firebase Console, go to "Authentication"
   - Click "Get started"
   - Go to "Sign-in method" tab
   - Enable "Email/Password" provider
   - Save

3. **Get Firebase Configuration**
   - Go to Project Settings (gear icon)
   - Scroll down to "Your apps"
   - Click on Web icon (</>)
   - Register app with nickname "EduStocks Web"
   - Copy the Firebase configuration object

4. **Get Service Account Key**
   - In Project Settings, go to "Service accounts" tab
   - Click "Generate new private key"
   - Save the JSON file as `backend/src/main/resources/firebase-service-account.json`
   - **Important**: Add this file to `.gitignore` (already done)

### 3. Frontend Setup

1. **Navigate to frontend directory**
```bash
cd frontend
```

2. **Install dependencies**
```bash
npm install
```

3. **Create `.env` file**
   - Copy `.env.example` to `.env`
   - Fill in your Firebase configuration:
```env
REACT_APP_FIREBASE_API_KEY=AIza...
REACT_APP_FIREBASE_AUTH_DOMAIN=edustocks.firebaseapp.com
REACT_APP_FIREBASE_PROJECT_ID=edustocks
REACT_APP_FIREBASE_STORAGE_BUCKET=edustocks.appspot.com
REACT_APP_FIREBASE_MESSAGING_SENDER_ID=123456789
REACT_APP_FIREBASE_APP_ID=1:123456789:web:abc123
REACT_APP_API_URL=http://localhost:8080/api
```

4. **Start frontend**
```bash
npm start
```

Frontend will run on http://localhost:3000

### 4. Backend Setup

1. **Navigate to backend directory**
```bash
cd backend
```

2. **Update application.properties**
   - Open `src/main/resources/application.properties`
   - Update Firebase project ID:
```properties
firebase.project-id=edustocks
firebase.credentials.path=src/main/resources/firebase-service-account.json
```

3. **Verify firebase-service-account.json exists**
   - Make sure the file from Step 2.4 is in `backend/src/main/resources/`

4. **Build the project**
```bash
mvn clean install
```

5. **Run the backend**
```bash
mvn spring-boot:run
```

Backend will run on http://localhost:8080

### 5. Verify Installation

1. Open http://localhost:3000 in your browser
2. You should see the login page
3. Click "Sign up" to create an account
4. After signing up, you should see the dashboard
5. Check that you have $10,000 starting balance

## Optional: Stock API Integration

### Option 1: Yahoo Finance (Free, No API Key)

The backend already uses Yahoo Finance API by default. No setup needed!

### Option 2: Alpha Vantage (Free API Key)

1. Go to https://www.alphavantage.co/support/#api-key
2. Get a free API key
3. Update `backend/src/main/resources/application.properties`:
```properties
stock.api.key=your-alpha-vantage-key
```

## Optional: OpenAI API (For AI Trainer)

1. Go to https://platform.openai.com/
2. Create an account and get an API key
3. Update `backend/src/main/resources/application.properties`:
```properties
openai.api.key=sk-your-openai-api-key
```

**Note**: Without OpenAI API key, AI Trainer will show mock responses.

## Troubleshooting

### Frontend Issues

**Problem**: `npm install` fails
- **Solution**: Make sure Node.js v18+ is installed
- Try: `npm cache clean --force` then `npm install`

**Problem**: Firebase config errors
- **Solution**: Double-check your `.env` file values
- Make sure all Firebase config values are correct

### Backend Issues

**Problem**: Maven build fails
- **Solution**: Make sure Java JDK 17+ is installed
- Check: `java -version` and `mvn -version`

**Problem**: Firebase service account not found
- **Solution**: Make sure `firebase-service-account.json` is in `backend/src/main/resources/`
- Check the file path in `application.properties`

**Problem**: Port 8080 already in use
- **Solution**: Change port in `application.properties`:
```properties
server.port=8081
```
- Update frontend `.env`: `REACT_APP_API_URL=http://localhost:8081/api`

### Connection Issues

**Problem**: Frontend can't connect to backend
- **Solution**: 
  1. Make sure backend is running on port 8080
  2. Check CORS settings in `backend/src/main/resources/application.properties`
  3. Verify `REACT_APP_API_URL` in frontend `.env`

## Next Steps

1. ✅ Create your account
2. ✅ Explore the dashboard
3. ✅ Try the trading interface
4. ✅ Complete a lesson
5. ✅ Ask the AI trainer a question
6. ✅ Check your profile and progress

## Need Help?

- Check the main README.md for more details
- Review Firebase documentation: https://firebase.google.com/docs
- Spring Boot docs: https://spring.io/projects/spring-boot

Happy learning! 🎓📈



