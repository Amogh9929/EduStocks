<#
    Script: set-gemini-key.ps1
    Purpose: Prompt for a Gemini (Google AI) API key and set it for the current PowerShell session (and optionally persist it). Also sets AI_PROVIDER=gemini if you choose.
    Usage:
        1. Run:  .\set-gemini-key.ps1
        2. Choose whether to persist the key (setx) and provider.

    Notes:
        - Backend reads gemini.api.key via env GEMINI_API_KEY and ai.provider via env AI_PROVIDER.
        - Do NOT commit real keys. Rotate if exposed.
#>

Write-Host "=== Gemini API Key Configuration ===" -ForegroundColor Cyan

if ($Env:GEMINI_API_KEY) {
    Write-Host "Current session has GEMINI_API_KEY set (length: $($Env:GEMINI_API_KEY.Length))." -ForegroundColor Yellow
    $overwrite = Read-Host "Do you want to overwrite it? (y/N)"
    if ($overwrite -notin @('y','Y')) { Write-Host "Keeping existing key."; return }
}

$key = Read-Host -AsSecureString "Enter Gemini API Key (will not echo)"
$bstr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($key)
$plain = [Runtime.InteropServices.Marshal]::PtrToStringAuto($bstr)
[Runtime.InteropServices.Marshal]::ZeroFreeBSTR($bstr)

if ([string]::IsNullOrWhiteSpace($plain)) { Write-Host "No key entered. Aborting." -ForegroundColor Red; return }

$Env:GEMINI_API_KEY = $plain
$Env:AI_PROVIDER = "gemini"
Write-Host "Session GEMINI_API_KEY set (length: $($plain.Length)). AI_PROVIDER=gemini" -ForegroundColor Green

$persist = Read-Host "Persist GEMINI_API_KEY and AI_PROVIDER to user environment (setx)? (y/N)"
if ($persist -in @('y','Y')) {
    setx GEMINI_API_KEY $plain | Out-Null
    setx AI_PROVIDER gemini | Out-Null
    Write-Host "Persisted GEMINI_API_KEY and AI_PROVIDER. Restart terminals for it to take effect." -ForegroundColor Green
}

Write-Host "Done. Start backend with: mvn -DskipTests spring-boot:run" -ForegroundColor Cyan
