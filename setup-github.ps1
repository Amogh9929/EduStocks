# GitHub Repository Setup Script for EduStocks
# This script helps you create and push your project to GitHub

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   EduStocks - GitHub Setup Script" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if git is installed
Write-Host "Checking Git installation..." -ForegroundColor Yellow
try {
    $gitVersion = git --version
    Write-Host "✓ Git is installed: $gitVersion" -ForegroundColor Green
} catch {
    Write-Host "✗ Git is not installed. Please install Git first." -ForegroundColor Red
    Write-Host "Download from: https://git-scm.com/download/win" -ForegroundColor Yellow
    exit
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Step 1: Initialize Git Repository" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# Check if already a git repository
if (Test-Path .git) {
    Write-Host "✓ Git repository already initialized" -ForegroundColor Green
} else {
    Write-Host "Initializing Git repository..." -ForegroundColor Yellow
    git init
    Write-Host "✓ Git repository initialized" -ForegroundColor Green
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Step 2: Create GitHub Repository" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Please create a new repository on GitHub:" -ForegroundColor Yellow
Write-Host "1. Go to https://github.com/new" -ForegroundColor White
Write-Host "2. Repository name: stock-learning-app (or your preferred name)" -ForegroundColor White
Write-Host "3. Description: Interactive stock trading learning platform" -ForegroundColor White
Write-Host "4. Make it Public or Private (your choice)" -ForegroundColor White
Write-Host "5. DO NOT initialize with README (we already have one)" -ForegroundColor Red
Write-Host "6. DO NOT add .gitignore (we already have one)" -ForegroundColor Red
Write-Host "7. Choose MIT License or skip (we already have LICENSE file)" -ForegroundColor White
Write-Host "8. Click 'Create repository'" -ForegroundColor White
Write-Host ""

# Prompt for GitHub repository URL
$repoUrl = Read-Host "Enter your GitHub repository URL (e.g., https://github.com/username/stock-learning-app.git)"

if ([string]::IsNullOrWhiteSpace($repoUrl)) {
    Write-Host "✗ No repository URL provided. Exiting..." -ForegroundColor Red
    exit
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Step 3: Stage Files" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

Write-Host "Staging all files..." -ForegroundColor Yellow
git add .

Write-Host "✓ Files staged" -ForegroundColor Green

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Step 4: Create Initial Commit" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

Write-Host "Creating initial commit..." -ForegroundColor Yellow
git commit -m "Initial commit: EduStocks - Interactive Stock Learning Platform

Features:
- React + TypeScript frontend with Tailwind CSS
- Spring Boot backend with Java 21
- Firebase authentication and database
- Real-time stock trading simulation
- Interactive lessons with AI trainer
- Portfolio management and progress tracking"

Write-Host "✓ Initial commit created" -ForegroundColor Green

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Step 5: Set Default Branch to 'main'" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

git branch -M main
Write-Host "✓ Default branch set to 'main'" -ForegroundColor Green

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Step 6: Add Remote Repository" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

Write-Host "Adding remote repository..." -ForegroundColor Yellow
try {
    git remote add origin $repoUrl
    Write-Host "✓ Remote repository added" -ForegroundColor Green
} catch {
    Write-Host "Note: Remote 'origin' may already exist. Updating..." -ForegroundColor Yellow
    git remote set-url origin $repoUrl
    Write-Host "✓ Remote repository updated" -ForegroundColor Green
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Step 7: Push to GitHub" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

Write-Host "Pushing to GitHub..." -ForegroundColor Yellow
Write-Host "You may be prompted for your GitHub credentials..." -ForegroundColor Yellow
Write-Host ""

git push -u origin main

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "   🎉 SUCCESS! 🎉" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "Your EduStocks project is now on GitHub!" -ForegroundColor Green
Write-Host ""
Write-Host "Repository URL: $repoUrl" -ForegroundColor Cyan
Write-Host ""
Write-Host "Next Steps:" -ForegroundColor Yellow
Write-Host "1. Visit your repository on GitHub" -ForegroundColor White
Write-Host "2. Add topics/tags: stock, education, react, spring-boot, firebase" -ForegroundColor White
Write-Host "3. Update repository description" -ForegroundColor White
Write-Host "4. Add screenshots to README.md" -ForegroundColor White
Write-Host "5. Enable GitHub Pages if you want to host documentation" -ForegroundColor White
Write-Host "6. Set up branch protection rules (optional)" -ForegroundColor White
Write-Host ""
Write-Host "Happy coding! 📚📈" -ForegroundColor Cyan
Write-Host ""
