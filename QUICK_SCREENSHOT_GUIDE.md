# 📸 Quick Start: Taking Real Screenshots

## Step 1: Start Your Application

Open **2 terminal windows**:

**Terminal 1 - Backend:**
```powershell
cd backend
mvn spring-boot:run
```

**Terminal 2 - Frontend:**
```powershell
cd frontend
npm start
```

Wait for both to start. You should see:
- Backend: `Started EduStocksApplication in X seconds`
- Frontend: Browser opens to `http://localhost:3000`

---

## Step 2: Create Sample Data

To make your screenshots look professional, you need realistic data:

### 2.1 Sign Up
1. Go to `http://localhost:3000/signup`
2. Create account: test@example.com / password123
3. Sign in

### 2.2 Add Portfolio Holdings
1. Go to Trading page
2. Search and buy these stocks:
   - **AAPL** (Apple) - Buy 10 shares
   - **MSFT** (Microsoft) - Buy 5 shares
   - **GOOGL** (Google) - Buy 3 shares
   - **TSLA** (Tesla) - Buy 2 shares
   - **AMZN** (Amazon) - Buy 4 shares

### 2.3 Complete Some Lessons
1. Go to Lessons page
2. Complete 2-3 lessons to earn XP

### 2.4 Chat with AI
1. Go to AI Trainer
2. Ask: "What is a dividend stock?"
3. Let AI respond (for screenshot)

---

## Step 3: Take Screenshots

### On Windows:
1. Press **Win + Shift + S** (Snipping Tool)
2. Click "Window Snip" or drag to select area
3. Screenshot is copied to clipboard
4. Open Paint (`mspaint`)
5. Paste (Ctrl + V)
6. Save to `screenshots/` folder with correct name

### Recommended: Use ShareX (Free)
1. Download: https://getsharex.com/
2. Install and run
3. Configure:
   - Hotkey: Print Screen
   - Auto-save to: `C:\Users\agarw\stock-learning-app\screenshots\`
4. Take screenshots with Print Screen key

---

## Step 4: Screenshot Checklist

Go to each URL and take a screenshot. Save with the exact filename shown:

### ✅ Required Screenshots

| # | Filename | URL | What to Capture |
|---|----------|-----|-----------------|
| 1 | `01-login.png` | `/login` | Login form, clean UI |
| 2 | `02-signup.png` | `/signup` | Registration form |
| 3 | `03-dashboard.png` | `/dashboard` | Portfolio overview with stocks |
| 4 | `04-trading.png` | `/trading` | Trading interface with search |
| 5 | `05-buy-modal.png` | `/trading` | Click Buy on any stock |
| 6 | `06-lessons.png` | `/lessons` | List of all lessons |
| 7 | `07-lesson-detail.png` | `/lesson/1` | Lesson content + quiz |
| 8 | `08-ai-trainer.png` | `/ai-trainer` | AI chat with conversation |
| 9 | `09-profile.png` | `/profile` | User stats and progress |
| 10 | `10-mobile-dashboard.png` | `/dashboard` | F12 → Device mode → iPhone |

---

## Step 5: Optimize Screenshots (Optional)

### Online Tools:
- **TinyPNG**: https://tinypng.com/ - Compress images
- **Squoosh**: https://squoosh.app/ - Resize and compress
- **Screely**: https://screely.com/ - Add browser frame

### Target:
- Size: < 500 KB per image
- Format: PNG
- Width: 1920px max

---

## Step 6: Update README

Run the update script:
```powershell
.\update-readme-screenshots.ps1
```

Or manually edit README.md and replace placeholder URLs with:
```markdown
![Dashboard Overview](./screenshots/03-dashboard.png)
```

---

## Step 7: Commit and Push

```powershell
git add screenshots/ README.md
git commit -m "docs: Add real application screenshots"
git push origin main
```

---

## 🎨 Pro Tips

### Make Screenshots Look Better:
1. **Full Screen**: Press F11 to hide browser UI
2. **Zoom 100%**: Press Ctrl + 0
3. **Hide Extensions**: Use incognito mode
4. **Clean Data**: Use realistic stock prices and clean numbers
5. **Consistency**: Same browser, same size, same theme

### For Dashboard Screenshot:
- Make sure you have 5-10 stocks
- Show green/red P/L colors
- Include transaction history

### For Trading Screenshot:
- Search for "AAPL" or popular stock
- Show current price
- Display your portfolio on the side

### For AI Trainer:
- Have 2-3 message exchanges
- Ask meaningful questions like:
  - "What is a dividend stock?"
  - "How do I calculate P/L ratio?"
  - "Should I diversify my portfolio?"

### For Lessons:
- Show different difficulty levels
- Include progress indicators
- Show completed lessons

---

## ❓ Troubleshooting

**Q: App won't start?**
- Check if ports 3000 and 8080 are available
- Make sure Firebase config is correct
- Check `npm install` and `mvn install` completed

**Q: No data showing?**
- Make sure backend API is running
- Check console for errors (F12)
- Verify Firebase connection

**Q: Screenshots too large?**
- Use TinyPNG or Squoosh to compress
- Save as PNG, not BMP
- Resize to 1920px width max

**Q: Can't find screenshots folder?**
- It's in the root: `c:\Users\agarw\stock-learning-app\screenshots\`
- Create it if missing: `mkdir screenshots`

---

## 📋 Final Checklist

Before committing:
- [ ] All 10 screenshots saved with correct filenames
- [ ] Each screenshot < 500 KB
- [ ] Screenshots show realistic data (not empty portfolios)
- [ ] Images are clear and high quality
- [ ] README.md updated with new paths
- [ ] Tested that images display in GitHub

---

**Estimated Time**: 20-30 minutes

**Need Help?** Check SCREENSHOT_GUIDE.md for detailed instructions.
