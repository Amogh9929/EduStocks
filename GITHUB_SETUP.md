# 🚀 Quick GitHub Setup Guide

This guide will help you create a GitHub repository for your EduStocks project.

## Prerequisites

- Git installed on your computer ([Download Git](https://git-scm.com/download/win))
- GitHub account ([Sign up](https://github.com/signup))

## Option 1: Automated Setup (Recommended)

Run the provided PowerShell script:

```powershell
# Navigate to project directory
cd c:\Users\agarw\stock-learning-app

# Run the setup script
.\setup-github.ps1
```

The script will guide you through the entire process!

## Option 2: Manual Setup

### Step 1: Create GitHub Repository

1. Go to [GitHub](https://github.com) and sign in
2. Click the **+** icon in the top right → **New repository**
3. Fill in the details:
   - **Repository name**: `stock-learning-app` (or your preferred name)
   - **Description**: `Interactive stock trading learning platform with React, Spring Boot, and AI`
   - **Visibility**: Public (recommended) or Private
   - **DO NOT** check "Initialize this repository with a README"
   - **DO NOT** add .gitignore
   - **DO NOT** add a license
4. Click **Create repository**

### Step 2: Initialize Local Git Repository

Open PowerShell in your project directory:

```powershell
# Navigate to project directory
cd c:\Users\agarw\stock-learning-app

# Initialize Git (if not already initialized)
git init

# Configure Git with your info (if not already done)
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"

# Check status
git status
```

### Step 3: Stage and Commit Files

```powershell
# Stage all files
git add .

# Create initial commit
git commit -m "Initial commit: EduStocks - Interactive Stock Learning Platform"
```

### Step 4: Connect to GitHub

```powershell
# Set default branch to main
git branch -M main

# Add remote repository (replace YOUR_USERNAME and REPO_NAME)
git remote add origin https://github.com/YOUR_USERNAME/stock-learning-app.git

# Verify remote
git remote -v
```

### Step 5: Push to GitHub

```powershell
# Push to GitHub
git push -u origin main
```

You may be prompted for GitHub credentials. If using HTTPS, consider setting up:
- [Personal Access Token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token)
- [Git Credential Manager](https://github.com/git-ecosystem/git-credential-manager)

## Post-Setup: Enhance Your Repository

### 1. Add Repository Topics

On GitHub repository page:
- Click the ⚙️ gear icon next to "About"
- Add topics: `stock-trading`, `education`, `react`, `typescript`, `spring-boot`, `firebase`, `java`, `learning-platform`, `ai`
- Save changes

### 2. Update Repository Description

- Click the ⚙️ gear icon next to "About"
- Add description: `Interactive stock trading learning platform with virtual currency, real-time data, lessons, and AI trainer`
- Add website URL (if deployed)
- Save changes

### 3. Add Screenshots

1. Take screenshots of your application
2. Create an `assets` or `screenshots` folder
3. Upload images to the folder
4. Update README.md to include the screenshots

### 4. Create Repository Sections

Enable these GitHub features:
- **Issues**: For bug reports and feature requests
- **Projects**: For project management
- **Wiki**: For detailed documentation
- **Discussions**: For community Q&A

### 5. Set Up Branch Protection (Optional)

For collaborative projects:
1. Go to **Settings** → **Branches**
2. Add rule for `main` branch
3. Enable:
   - Require pull request reviews
   - Require status checks to pass
   - Enforce restrictions

### 6. Add Badges (Optional)

Add status badges to your README:
- Build status
- License
- Version
- Contributors

## Keeping Your Repository Updated

### Regular Commits

```powershell
# Check what changed
git status

# Stage specific files
git add <filename>

# Or stage all changes
git add .

# Commit with a message
git commit -m "Add feature X" -m "Detailed description here"

# Push to GitHub
git push
```

### Creating Feature Branches

```powershell
# Create and switch to new branch
git checkout -b feature/new-feature

# Make changes and commit
git add .
git commit -m "Add new feature"

# Push branch to GitHub
git push -u origin feature/new-feature

# Create Pull Request on GitHub
```

### Pulling Latest Changes

```powershell
# Get latest changes from GitHub
git pull origin main
```

## Important: Protect Sensitive Information

**Never commit these files:**
- ✅ Already added to `.gitignore`:
  - `firebase-service-account.json`
  - `.env` files
  - `application.properties` with real keys

**Before first commit, verify:**

```powershell
# Check what will be committed
git status

# If you see sensitive files, add them to .gitignore immediately
```

## Troubleshooting

### Authentication Issues

If you get authentication errors:

1. **Use Personal Access Token** instead of password
   - Generate at: Settings → Developer settings → Personal access tokens
   - Use token as password when prompted

2. **Or use SSH** instead of HTTPS
   ```powershell
   # Generate SSH key
   ssh-keygen -t ed25519 -C "your.email@example.com"
   
   # Add to GitHub: Settings → SSH and GPG keys
   
   # Change remote URL
   git remote set-url origin git@github.com:YOUR_USERNAME/stock-learning-app.git
   ```

### Large File Warning

If you get warnings about large files:
```powershell
# Remove from staging
git reset <filename>

# Add to .gitignore
echo "large-file.jar" >> .gitignore

# Commit without the large file
git commit -m "Initial commit"
```

### Fixing Committed Secrets

If you accidentally committed sensitive data:
1. **Remove from repository**
2. **Rotate all exposed credentials immediately**
3. **Use tools like `git-filter-repo` to clean history**

## Need Help?

- **Git Documentation**: [git-scm.com](https://git-scm.com/doc)
- **GitHub Guides**: [guides.github.com](https://guides.github.com/)
- **GitHub Support**: [support.github.com](https://support.github.com/)

---

## Quick Command Reference

```powershell
# Check status
git status

# Stage files
git add .
git add <filename>

# Commit
git commit -m "Message"

# Push
git push

# Pull
git pull

# View history
git log --oneline

# Create branch
git checkout -b branch-name

# Switch branch
git checkout branch-name

# Merge branch
git merge branch-name

# View remotes
git remote -v
```

---

**Ready to go? Run `.\setup-github.ps1` to get started!** 🚀
