# Update README with Screenshot Paths

# This script updates the README.md file to use local screenshot paths
# Run this after you've saved screenshots to the ./screenshots/ folder

Write-Host "📸 Updating README with screenshot paths..." -ForegroundColor Cyan

$readmePath = "$PSScriptRoot\README.md"
$screenshotsDir = "$PSScriptRoot\screenshots"

# Check if README exists
if (-not (Test-Path $readmePath)) {
    Write-Host "❌ README.md not found!" -ForegroundColor Red
    exit 1
}

# Check screenshots directory
if (-not (Test-Path $screenshotsDir)) {
    Write-Host "❌ screenshots/ directory not found!" -ForegroundColor Red
    Write-Host "   Create it and add your screenshots first.`n" -ForegroundColor Gray
    exit 1
}

# List available screenshots
Write-Host "`n📁 Found screenshots:" -ForegroundColor Yellow
$screenshots = Get-ChildItem -Path $screenshotsDir -Filter "*.png" | Sort-Object Name
if ($screenshots.Count -eq 0) {
    Write-Host "⚠️  No PNG files found in screenshots/ directory" -ForegroundColor Yellow
    Write-Host "   Please add screenshots first, then run this script again.`n" -ForegroundColor Gray
    exit 1
}

foreach ($screenshot in $screenshots) {
    $sizeKB = [math]::Round($screenshot.Length / 1KB, 1)
    Write-Host "  ✓ $($screenshot.Name) ($sizeKB KB)" -ForegroundColor Green
}

Write-Host "`n🔄 Updating README.md..." -ForegroundColor Cyan

# Read current README
$readmeContent = Get-Content $readmePath -Raw

# Define screenshot mappings
$screenshotMap = @{
    'https://via.placeholder.com/800x450/2563eb/ffffff\?text=Dashboard\+-\+Portfolio\+Overview\+%26\+Performance\+Metrics' = './screenshots/03-dashboard.png'
    'https://via.placeholder.com/800x450/16a34a/ffffff\?text=Trading\+Interface\+-\+Buy%2FSell\+Stocks\+with\+Live\+Prices' = './screenshots/04-trading.png'
    'https://via.placeholder.com/800x450/8b5cf6/ffffff\?text=Learning\+Center\+-\+Structured\+Lessons\+%26\+Quizzes' = './screenshots/06-lessons.png'
    'https://via.placeholder.com/800x450/f59e0b/ffffff\?text=AI\+Trainer\+-\+Personalized\+Trading\+Guidance' = './screenshots/08-ai-trainer.png'
}

$updated = $false
foreach ($placeholder in $screenshotMap.Keys) {
    $newPath = $screenshotMap[$placeholder]
    if ($readmeContent -match [regex]::Escape($placeholder)) {
        $readmeContent = $readmeContent -replace [regex]::Escape($placeholder), $newPath
        Write-Host "  ✓ Updated: $newPath" -ForegroundColor Green
        $updated = $true
    }
}

if ($updated) {
    # Update the note about screenshots
    $readmeContent = $readmeContent -replace 'Note: Screenshots show mockups\. Live deployment coming soon!', 'Screenshots taken from local development environment'
    
    # Save updated README
    Set-Content -Path $readmePath -Value $readmeContent -NoNewline
    Write-Host "`n✅ README.md updated successfully!" -ForegroundColor Green
    
    # Ask to commit
    Write-Host "`n📤 Ready to commit?" -ForegroundColor Cyan
    Write-Host "   Run these commands:" -ForegroundColor Gray
    Write-Host "   git add screenshots/ README.md" -ForegroundColor White
    Write-Host "   git commit -m 'docs: Add real application screenshots'" -ForegroundColor White
    Write-Host "   git push origin main`n" -ForegroundColor White
    
    $shouldCommit = Read-Host "Commit and push now? (y/n)"
    if ($shouldCommit -eq "y") {
        Write-Host "`n🔄 Committing changes..." -ForegroundColor Cyan
        git add screenshots/ README.md SCREENSHOT_GUIDE.md take-screenshots.ps1 update-readme-screenshots.ps1
        git commit -m "docs: Add real application screenshots and screenshot tooling"
        git push origin main
        Write-Host "✅ Changes pushed to GitHub!" -ForegroundColor Green
    }
} else {
    Write-Host "`n⚠️  No placeholder images found to replace" -ForegroundColor Yellow
    Write-Host "   README might already be updated or use different placeholders`n" -ForegroundColor Gray
}

Write-Host "`n✨ Done!" -ForegroundColor Cyan
