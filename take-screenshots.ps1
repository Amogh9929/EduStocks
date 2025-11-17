# Screenshot Automation Helper

# This PowerShell script helps you prepare for taking screenshots
# It opens the app in your default browser and provides a checklist

Write-Host "🎯 EduStocks Screenshot Helper" -ForegroundColor Cyan
Write-Host "================================`n" -ForegroundColor Cyan

# Check if app is running
Write-Host "Checking if application is running..." -ForegroundColor Yellow

$frontendRunning = Test-NetConnection -ComputerName localhost -Port 3000 -InformationLevel Quiet -WarningAction SilentlyContinue
$backendRunning = Test-NetConnection -ComputerName localhost -Port 8080 -InformationLevel Quiet -WarningAction SilentlyContinue

if (-not $frontendRunning) {
    Write-Host "❌ Frontend not running on port 3000" -ForegroundColor Red
    Write-Host "   Start with: cd frontend && npm start`n" -ForegroundColor Gray
    $shouldStart = Read-Host "Start frontend now? (y/n)"
    if ($shouldStart -eq "y") {
        Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PSScriptRoot\frontend'; npm start"
        Write-Host "✅ Frontend starting in new window..." -ForegroundColor Green
        Start-Sleep -Seconds 10
    }
} else {
    Write-Host "✅ Frontend running on http://localhost:3000" -ForegroundColor Green
}

if (-not $backendRunning) {
    Write-Host "❌ Backend not running on port 8080" -ForegroundColor Red
    Write-Host "   Start with: cd backend && mvn spring-boot:run`n" -ForegroundColor Gray
    $shouldStart = Read-Host "Start backend now? (y/n)"
    if ($shouldStart -eq "y") {
        Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PSScriptRoot\backend'; mvn spring-boot:run"
        Write-Host "✅ Backend starting in new window..." -ForegroundColor Green
        Start-Sleep -Seconds 15
    }
} else {
    Write-Host "✅ Backend running on http://localhost:8080" -ForegroundColor Green
}

Write-Host "`n📋 Screenshot Checklist:" -ForegroundColor Cyan
Write-Host "========================`n" -ForegroundColor Cyan

$checklist = @(
    "[ ] Login page - Clean UI with no errors",
    "[ ] Signup page - Registration form",
    "[ ] Dashboard - Portfolio with 5-10 stocks",
    "[ ] Trading page - Search and buy/sell interface",
    "[ ] Buy modal - Active purchase dialog",
    "[ ] Lessons list - All lessons with difficulty",
    "[ ] Lesson detail - Content and quiz",
    "[ ] AI Trainer - Chat conversation",
    "[ ] Profile - Stats, XP, level, rank",
    "[ ] Mobile view - Responsive dashboard"
)

foreach ($item in $checklist) {
    Write-Host "  $item" -ForegroundColor White
}

Write-Host "`n📸 Screenshot Tips:" -ForegroundColor Cyan
Write-Host "==================`n" -ForegroundColor Cyan
Write-Host "  • Press Win + Shift + S for Snipping Tool" -ForegroundColor Gray
Write-Host "  • Use F11 for full-screen (hide browser UI)" -ForegroundColor Gray
Write-Host "  • Set zoom to 100% (Ctrl + 0)" -ForegroundColor Gray
Write-Host "  • Save as PNG in ./screenshots/ folder" -ForegroundColor Gray
Write-Host "  • Name: 01-login.png, 02-signup.png, etc.`n" -ForegroundColor Gray

$openBrowser = Read-Host "Open browser to http://localhost:3000? (y/n)"
if ($openBrowser -eq "y") {
    Write-Host "🌐 Opening browser..." -ForegroundColor Green
    Start-Process "http://localhost:3000"
    Start-Sleep -Seconds 2
    
    Write-Host "`n📍 URLs to screenshot:" -ForegroundColor Cyan
    Write-Host "  1. http://localhost:3000/login" -ForegroundColor Gray
    Write-Host "  2. http://localhost:3000/signup" -ForegroundColor Gray
    Write-Host "  3. http://localhost:3000/dashboard" -ForegroundColor Gray
    Write-Host "  4. http://localhost:3000/trading" -ForegroundColor Gray
    Write-Host "  5. http://localhost:3000/lessons" -ForegroundColor Gray
    Write-Host "  6. http://localhost:3000/lesson/1" -ForegroundColor Gray
    Write-Host "  7. http://localhost:3000/ai-trainer" -ForegroundColor Gray
    Write-Host "  8. http://localhost:3000/profile`n" -ForegroundColor Gray
}

Write-Host "📁 Save screenshots to: $PSScriptRoot\screenshots\" -ForegroundColor Yellow
Write-Host "`n✨ After taking screenshots, run:" -ForegroundColor Cyan
Write-Host "   .\update-readme-screenshots.ps1`n" -ForegroundColor White

# Create a quick reference card
$referenceCard = @"
╔════════════════════════════════════════════════════════════════╗
║                    SCREENSHOT QUICK REFERENCE                   ║
╠════════════════════════════════════════════════════════════════╣
║ File Name              │ URL                    │ What to Show ║
╠════════════════════════════════════════════════════════════════╣
║ 01-login.png           │ /login                 │ Login form   ║
║ 02-signup.png          │ /signup                │ Signup form  ║
║ 03-dashboard.png       │ /dashboard             │ Portfolio    ║
║ 04-trading.png         │ /trading               │ Stock search ║
║ 05-buy-modal.png       │ /trading (click buy)   │ Buy dialog   ║
║ 06-lessons.png         │ /lessons               │ Lesson list  ║
║ 07-lesson-detail.png   │ /lesson/1              │ Lesson+quiz  ║
║ 08-ai-trainer.png      │ /ai-trainer            │ AI chat      ║
║ 09-profile.png         │ /profile               │ User stats   ║
║ 10-mobile-dashboard.png│ /dashboard (mobile)    │ Responsive   ║
╚════════════════════════════════════════════════════════════════╝
"@

Write-Host $referenceCard -ForegroundColor Green

Write-Host "`n🎯 Ready to take screenshots! Good luck!" -ForegroundColor Cyan
