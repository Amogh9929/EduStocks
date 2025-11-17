# Screenshot Guide for EduStocks

## 📸 How to Capture Screenshots

### Prerequisites
1. Ensure both frontend and backend are running:
   ```bash
   # Terminal 1 - Backend
   cd backend
   mvn spring-boot:run
   
   # Terminal 2 - Frontend
   cd frontend
   npm start
   ```

2. Open browser to `http://localhost:3000`
3. Sign up/login to access all features

### Required Screenshots

#### 1. **Landing/Login Page** (`screenshots/01-login.png`)
- URL: `http://localhost:3000/login`
- **What to capture**: Full page showing login form
- **Notes**: Clean UI, show branding
- **Recommended size**: 1920x1080 or 1440x900

#### 2. **Signup Page** (`screenshots/02-signup.png`)
- URL: `http://localhost:3000/signup`
- **What to capture**: Registration form
- **Notes**: Show form validation if possible

#### 3. **Dashboard Overview** (`screenshots/03-dashboard.png`)
- URL: `http://localhost:3000/dashboard`
- **What to capture**: 
  - Portfolio summary card (total value, P/L)
  - Recent transactions list
  - Quick stats
- **Notes**: Make sure you have some stock holdings to show interesting data
- **Recommended size**: 1920x1080 (full page)

#### 4. **Trading Interface** (`screenshots/04-trading.png`)
- URL: `http://localhost:3000/trading`
- **What to capture**:
  - Search bar with stock results
  - Stock price display
  - Buy/Sell form
  - Portfolio holdings list
- **Tip**: Search for a popular stock like "AAPL" or "TSLA"
- **Recommended size**: 1920x1080

#### 5. **Trading - Buy Modal** (`screenshots/05-buy-modal.png`)
- **What to capture**: Buy stock modal/dialog in action
- **Tip**: Click "Buy" button on a stock
- **Recommended size**: Can be smaller, focus on modal

#### 6. **Lessons Page** (`screenshots/06-lessons.png`)
- URL: `http://localhost:3000/lessons`
- **What to capture**:
  - List of lessons with difficulty levels
  - Progress indicators
  - Lesson categories
- **Recommended size**: 1920x1080

#### 7. **Lesson Detail** (`screenshots/07-lesson-detail.png`)
- URL: `http://localhost:3000/lesson/1` (or any lesson ID)
- **What to capture**:
  - Lesson content
  - Quiz questions
  - Progress tracker
- **Recommended size**: 1920x1080

#### 8. **AI Trainer Interface** (`screenshots/08-ai-trainer.png`)
- URL: `http://localhost:3000/ai-trainer`
- **What to capture**:
  - Chat interface
  - Sample conversation with AI
  - Input box
- **Tip**: Ask a question like "What is a stock?" to show AI response
- **Recommended size**: 1920x1080

#### 9. **Profile/Progress Page** (`screenshots/09-profile.png`)
- URL: `http://localhost:3000/profile`
- **What to capture**:
  - User statistics
  - XP and level
  - Completed lessons
  - Rank/badge
- **Recommended size**: 1920x1080

#### 10. **Mobile Responsive** (`screenshots/10-mobile-dashboard.png`)
- **What to capture**: Dashboard on mobile viewport
- **How**: 
  1. Open browser DevTools (F12)
  2. Click device toolbar icon
  3. Select "iPhone 12 Pro" or similar
  4. Capture screenshot
- **Recommended size**: 375x812 (iPhone size)

### Screenshot Best Practices

#### Before Taking Screenshots:
1. ✅ Clear browser cache and reload
2. ✅ Use a clean browser profile (no extensions visible)
3. ✅ Set zoom to 100%
4. ✅ Hide browser bookmarks bar
5. ✅ Use full screen mode (F11) for cleaner screenshots
6. ✅ Add sample data:
   - Create a portfolio with 5-10 stocks
   - Complete 2-3 lessons
   - Have some transactions history
   - Accumulate some XP points

#### Tools for Screenshots:

**Windows:**
- **Snipping Tool**: Win + Shift + S
- **Game Bar**: Win + G → Capture screenshot
- **ShareX** (recommended): Free, powerful screenshot tool
  - Download: https://getsharex.com/
  - Features: Auto-save, annotations, uploading

**Browser Extensions:**
- **Awesome Screenshot**: Full page capture
- **Fireshot**: Capture entire scrolling page

**Online Tools:**
- **screely.com**: Add browser frame around screenshots
- **shots.so**: Beautiful mockup generator

#### Image Optimization:
```bash
# Install ImageMagick (optional)
# Resize to 1920x1080 and compress
magick screenshot.png -resize 1920x1080 -quality 85 optimized.png

# Or use online: tinypng.com, squoosh.app
```

### File Naming Convention
```
screenshots/
  01-login.png
  02-signup.png
  03-dashboard.png
  04-trading.png
  05-buy-modal.png
  06-lessons.png
  07-lesson-detail.png
  08-ai-trainer.png
  09-profile.png
  10-mobile-dashboard.png
  11-mobile-trading.png (optional)
```

### Quick Steps

1. **Start the app**:
   ```bash
   # Backend
   cd backend && mvn spring-boot:run
   
   # Frontend (new terminal)
   cd frontend && npm start
   ```

2. **Create test data**:
   - Sign up with a test account
   - Buy several different stocks
   - Complete a few lessons
   - Chat with AI trainer

3. **Take screenshots**:
   - Follow the list above
   - Save in `screenshots/` folder
   - Use PNG format for quality

4. **Optimize images**:
   - Use TinyPNG or Squoosh
   - Target < 500KB per image
   - Keep resolution at 1920px width max

5. **Update README**:
   - Replace placeholder URLs
   - Add captions
   - Commit and push

### After Screenshots

Update README.md:
```markdown
### Dashboard
![Dashboard Overview](./screenshots/03-dashboard.png)
*Real-time portfolio tracking with P/L visualization*
```

Commit:
```bash
git add screenshots/
git commit -m "docs: Add real application screenshots"
git push origin main
```

---

## 🎨 Making Screenshots Look Professional

### Add Browser Chrome
Use **screely.com** or **shots.so** to add:
- Browser window frame
- Gradient backgrounds
- Shadows and effects

### Annotations (Optional)
- Highlight key features with arrows
- Add callout boxes for important elements
- Use tools like Snagit or Skitch

### Consistency
- Use same browser for all screenshots
- Same viewport size (1920x1080)
- Same zoom level (100%)
- Consistent theme/color scheme

---

**Need Help?** Open DevTools → Elements → Delete unwanted elements before screenshot (e.g., debug info)
