# 🚀 Step-by-Step VS Code Setup Guide for EduStocks

This guide will walk you through building EduStocks in VS Code from scratch.

## 📋 Prerequisites Checklist

Before we start, make sure you have:

- [ ] VS Code installed ([Download](https://code.visualstudio.com/))
- [ ] Node.js v18+ installed ([Download](https://nodejs.org/))
- [ ] Java JDK 17+ installed ([Download](https://adoptium.net/))
- [ ] Maven installed ([Download](https://maven.apache.org/download.cgi))
- [ ] Git installed (optional, for version control)

## Step 1: Open Project in VS Code

### 1.1 Open VS Code
1. Launch Visual Studio Code
2. Click `File` → `Open Folder` (or press `Ctrl+K Ctrl+O`)
3. Navigate to your project folder: `C:\Users\agarw\stock-learning-app`
4. Click `Select Folder`

### 1.2 Install Recommended Extensions
VS Code will prompt you to install recommended extensions. Install these:

**Essential Extensions:**
- **Java Extension Pack** (by Microsoft) - For Java development
- **Spring Boot Extension Pack** (by VMware) - For Spring Boot
- **ES7+ React/Redux/React-Native snippets** - For React development
- **Tailwind CSS IntelliSense** - For Tailwind CSS autocomplete
- **Firebase** - For Firebase integration

**To install manually:**
1. Click the Extensions icon (or press `Ctrl+Shift+X`)
2. Search for each extension and click `Install`

## Step 2: Verify Prerequisites

### 2.1 Check Node.js Installation
1. Open terminal in VS Code: `Terminal` → `New Terminal` (or `` Ctrl+` ``)
2. Run:
```bash
node --version
```
Should show: `v18.x.x` or higher

3. Check npm:
```bash
npm --version
```
Should show: `9.x.x` or higher

### 2.2 Check Java Installation
```bash
java -version
```
Should show: `openjdk version "17"` or higher

### 2.3 Check Maven Installation
```bash
mvn -version
```
Should show: `Apache Maven 3.8.x` or higher

**If any are missing:** Install them before proceeding.

## Step 3: Set Up Firebase

### 3.1 Create Firebase Project
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click **"Add project"** (or **"Create a project"**)
3. Enter project name: `EduStocks` (or your preferred name)
4. Click **Continue**
5. **Disable Google Analytics** (optional, for simplicity) or enable it
6. Click **Create project**
7. Wait for project creation (takes ~30 seconds)
8. Click **Continue**

### 3.2 Enable Authentication
1. In Firebase Console, click **Authentication** in left sidebar
2. Click **Get started**
3. Click on **Sign-in method** tab
4. Click on **Email/Password**
5. Toggle **Enable** to ON
6. Click **Save**

### 3.3 Get Firebase Web App Configuration
1. In Firebase Console, click the **gear icon** (⚙️) next to "Project Overview"
2. Select **Project settings**
3. Scroll down to **"Your apps"** section
4. Click the **Web icon** (`</>`)
5. Register app:
   - App nickname: `EduStocks Web`
   - **Don't check** "Also set up Firebase Hosting"
   - Click **Register app**
6. **Copy the Firebase configuration object** - it looks like this:
```javascript
const firebaseConfig = {
  apiKey: "AIza...",
  authDomain: "edustocks.firebaseapp.com",
  projectId: "edustocks",
  storageBucket: "edustocks.appspot.com",
  messagingSenderId: "123456789",
  appId: "1:123456789:web:abc123"
};
```
**Keep this open - we'll need it in Step 4!**

### 3.4 Download Service Account Key (for Backend)
1. Still in **Project settings**, click **Service accounts** tab
2. Click **Generate new private key**
3. Click **Generate key** in the popup
4. A JSON file will download - **save it carefully!**
5. Rename it to: `firebase-service-account.json`
6. **Move this file** to: `backend/src/main/resources/firebase-service-account.json`

**Important:** This file contains sensitive credentials. Never commit it to git!

## Step 4: Configure Frontend

### 4.1 Install Frontend Dependencies
1. In VS Code terminal, navigate to frontend:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```
This will take 2-5 minutes. Wait for it to complete.

### 4.2 Create Environment File
1. In VS Code, right-click on `frontend` folder
2. Select **New File**
3. Name it: `.env`
4. Open the file and paste:
```env
REACT_APP_FIREBASE_API_KEY=your_api_key_here
REACT_APP_FIREBASE_AUTH_DOMAIN=your_project.firebaseapp.com
REACT_APP_FIREBASE_PROJECT_ID=your_project_id
REACT_APP_FIREBASE_STORAGE_BUCKET=your_project.appspot.com
REACT_APP_FIREBASE_MESSAGING_SENDER_ID=your_sender_id
REACT_APP_FIREBASE_APP_ID=your_app_id
REACT_APP_API_URL=http://localhost:8080/api
```

5. **Replace the values** with your Firebase config from Step 3.3:
   - `REACT_APP_FIREBASE_API_KEY` → `apiKey` value
   - `REACT_APP_FIREBASE_AUTH_DOMAIN` → `authDomain` value
   - `REACT_APP_FIREBASE_PROJECT_ID` → `projectId` value
   - `REACT_APP_FIREBASE_STORAGE_BUCKET` → `storageBucket` value
   - `REACT_APP_FIREBASE_MESSAGING_SENDER_ID` → `messagingSenderId` value
   - `REACT_APP_FIREBASE_APP_ID` → `appId` value
   - Keep `REACT_APP_API_URL=http://localhost:8080/api` as is

6. **Save the file** (`Ctrl+S`)

### 4.3 Verify Frontend Setup
Check that these files exist:
- ✅ `frontend/package.json`
- ✅ `frontend/.env` (with your Firebase config)
- ✅ `frontend/node_modules/` folder (created after npm install)

## Step 5: Configure Backend

### 5.1 Verify Service Account File
1. In VS Code, check that this file exists:
   - `backend/src/main/resources/firebase-service-account.json`
   
2. **If the file doesn't exist:**
   - Make sure you downloaded it in Step 3.4
   - Create the `resources` folder if it doesn't exist:
     - Right-click `backend/src/main/` → New Folder → `resources`
   - Copy your `firebase-service-account.json` file there

### 5.2 Update Application Properties
1. Open: `backend/src/main/resources/application.properties`
2. Update the Firebase project ID:
```properties
# Firebase Configuration
firebase.project-id=your-firebase-project-id
firebase.credentials.path=src/main/resources/firebase-service-account.json
```

3. **Replace `your-firebase-project-id`** with your actual Firebase project ID (from Step 3.3, it's the `projectId` value)

4. **Save the file** (`Ctrl+S`)

### 5.3 Build Backend (First Time)
1. In VS Code terminal, navigate to backend:
```bash
cd backend
```

2. Build the project:
```bash
mvn clean install
```
This will take 3-5 minutes the first time. Wait for it to complete.

**If you see "BUILD SUCCESS"** - you're good to go! ✅

**If you see errors:**
- Make sure Java JDK 17+ is installed
- Make sure Maven is installed
- Check that `firebase-service-account.json` exists

## Step 6: Run the Application

### 6.1 Start the Backend Server
1. In VS Code terminal (make sure you're in `backend` folder):
```bash
mvn spring-boot:run
```

2. Wait for it to start. You should see:
```
Started EduStocksApplication in X.XXX seconds
```

3. **Keep this terminal running!** The backend server is now running on `http://localhost:8080`

### 6.2 Start the Frontend (New Terminal)
1. Open a **new terminal** in VS Code:
   - Click `Terminal` → `New Terminal` (or press `` Ctrl+Shift+` ``)
   - Or click the `+` icon in the terminal panel

2. Navigate to frontend:
```bash
cd frontend
```

3. Start the frontend:
```bash
npm start
```

4. Wait for it to compile. Your browser should automatically open to `http://localhost:3000`

**If browser doesn't open automatically:**
- Manually go to: `http://localhost:3000`

## Step 7: Test the Application

### 7.1 Create an Account
1. You should see the **Login** page
2. Click **"Sign up"** link at the bottom
3. Enter:
   - Email: `test@example.com` (or any email)
   - Password: `password123` (must be 6+ characters)
   - Confirm Password: `password123`
4. Click **"Sign up"**
5. You should be redirected to the **Dashboard**

### 7.2 Explore the Dashboard
You should see:
- ✅ Welcome message
- ✅ Portfolio Value: $10,000.00
- ✅ Available Balance: $10,000.00
- ✅ Current Level: Beginner
- ✅ Experience Points: 0 XP
- ✅ Quick action buttons (Start Trading, Learn, AI Trainer)

### 7.3 Test Trading
1. Click **"Start Trading"** or navigate to **Trading** in the navbar
2. You should see a list of stocks (AAPL, MSFT, GOOGL, etc.)
3. Click on a stock (e.g., AAPL)
4. Enter quantity: `10`
5. Click **"Buy Stock"**
6. Check your portfolio - balance should decrease, holdings should appear

### 7.4 Test Lessons
1. Click **"Lessons"** in the navbar
2. You should see lesson cards
3. Click **"Start"** on a lesson
4. Read the content and answer questions
5. Complete the lesson to earn XP

### 7.5 Test AI Trainer
1. Click **"AI Trainer"** in the navbar
2. Type a question: "What is a stock?"
3. Click **"Ask AI"**
4. You should get a response (mock response if OpenAI key not configured)

## Step 8: Common Issues & Solutions

### Issue 1: "npm install" fails
**Solution:**
```bash
# Clear npm cache
npm cache clean --force

# Delete node_modules and package-lock.json
rm -rf node_modules package-lock.json

# Try again
npm install
```

### Issue 2: "mvn clean install" fails
**Solution:**
- Check Java version: `java -version` (must be 17+)
- Check Maven version: `mvn -version`
- Make sure `firebase-service-account.json` exists in `backend/src/main/resources/`

### Issue 3: Frontend can't connect to backend
**Solution:**
- Make sure backend is running on port 8080
- Check `REACT_APP_API_URL=http://localhost:8080/api` in `.env`
- Check backend terminal for errors
- Try accessing `http://localhost:8080/api/auth/health` in browser

### Issue 4: Firebase authentication errors
**Solution:**
- Double-check all Firebase config values in `.env`
- Make sure Email/Password is enabled in Firebase Console
- Check Firebase Console → Authentication → Users (your user should appear after signup)

### Issue 5: Port 8080 already in use
**Solution:**
- Change backend port in `application.properties`:
```properties
server.port=8081
```
- Update frontend `.env`:
```env
REACT_APP_API_URL=http://localhost:8081/api
```

### Issue 6: Port 3000 already in use
**Solution:**
- When running `npm start`, it will ask if you want to use a different port
- Type `Y` and press Enter
- Or set port manually:
```bash
set PORT=3001
npm start
```

## Step 9: Development Workflow

### Daily Development
1. **Start Backend:**
   ```bash
   cd backend
   mvn spring-boot:run
   ```

2. **Start Frontend (in new terminal):**
   ```bash
   cd frontend
   npm start
   ```

3. **Make changes:**
   - Frontend: Changes auto-reload (Hot Module Replacement)
   - Backend: Restart server after changes (or use Spring Boot DevTools)

### Useful VS Code Shortcuts
- `` Ctrl+` `` - Toggle terminal
- `Ctrl+P` - Quick file open
- `Ctrl+Shift+P` - Command palette
- `F5` - Start debugging
- `Ctrl+F5` - Run without debugging

### Debugging
1. **Frontend Debugging:**
   - Open browser DevTools (`F12`)
   - Check Console for errors
   - Check Network tab for API calls

2. **Backend Debugging:**
   - Set breakpoints in Java files
   - Press `F5` to start debugging
   - Use VS Code debugger

## Step 10: Next Steps

### Optional: Add Real Stock API
1. Get free API key from [Alpha Vantage](https://www.alphavantage.co/support/#api-key)
2. Update `backend/src/main/resources/application.properties`:
```properties
stock.api.key=your-alpha-vantage-key
```

### Optional: Add OpenAI for AI Trainer
1. Get API key from [OpenAI](https://platform.openai.com/)
2. Update `backend/src/main/resources/application.properties`:
```properties
openai.api.key=sk-your-openai-key
```

### Optional: Deploy to Production
- Frontend: Deploy to Vercel, Netlify, or Firebase Hosting
- Backend: Deploy to Heroku, AWS, or Google Cloud

## 🎉 Congratulations!

You've successfully set up EduStocks in VS Code! 

### What You've Built:
- ✅ Full-stack stock learning application
- ✅ Firebase authentication
- ✅ Real-time stock trading simulation
- ✅ Interactive lessons system
- ✅ AI-powered trainer
- ✅ Progress tracking

### Resources:
- [React Documentation](https://react.dev/)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Firebase Documentation](https://firebase.google.com/docs)
- [Tailwind CSS Documentation](https://tailwindcss.com/docs)

**Happy Coding! 🚀📚📈**

---

## Quick Reference Commands

```bash
# Frontend
cd frontend
npm install          # Install dependencies
npm start           # Start development server
npm run build       # Build for production

# Backend
cd backend
mvn clean install   # Build project
mvn spring-boot:run # Run server
mvn test           # Run tests
```

---

**Need Help?** Check the main `README.md` or `SETUP_GUIDE.md` for more details.



