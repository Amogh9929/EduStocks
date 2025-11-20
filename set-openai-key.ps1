<#
    Script: set-openai-key.ps1
    Purpose: Prompt for an OpenAI API key and set it for the current PowerShell session (and optionally persist it).
    Usage:
        1. Run:  .\set-openai-key.ps1
        2. Choose whether to persist key (writes user-level env var via setx).

    Notes:
        - For development only. Do NOT commit real API keys.
        - Backend reads OPENAI_API_KEY (Spring property: openai.api.key=${OPENAI_API_KEY:}).
        - Frontend does not need the raw key; only the backend should hold it.
        - To remove a persisted key: setx OPENAI_API_KEY "" and then restart your shell / IDE.
#>

Write-Host "=== OpenAI API Key Configuration ===" -ForegroundColor Cyan

if ($Env:OPENAI_API_KEY) {
    Write-Host "Current session has OPENAI_API_KEY set (length: $($Env:OPENAI_API_KEY.Length))." -ForegroundColor Yellow
    $overwrite = Read-Host "Do you want to overwrite it? (y/N)"
    if ($overwrite -notin @('y','Y')) { Write-Host "Keeping existing key."; return }
}

$key = Read-Host -AsSecureString "Enter OpenAI API Key (will not echo)"
$bstr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($key)
$plain = [Runtime.InteropServices.Marshal]::PtrToStringAuto($bstr)
[Runtime.InteropServices.Marshal]::ZeroFreeBSTR($bstr)

if ([string]::IsNullOrWhiteSpace($plain)) { Write-Host "No key entered. Aborting." -ForegroundColor Red; return }

# Basic validation: starts with 'sk-' (older style) or 'op-' (new style). Allow others.
if ($plain.Length -lt 20) { Write-Host "Warning: key seems unusually short." -ForegroundColor Yellow }

$Env:OPENAI_API_KEY = $plain
Write-Host "Session OPENAI_API_KEY set (length: $($plain.Length))." -ForegroundColor Green

$persist = Read-Host "Persist this key to user environment (setx)? (y/N)"
if ($persist -in @('y','Y')) {
    setx OPENAI_API_KEY $plain | Out-Null
    Write-Host "Persisted OPENAI_API_KEY. Restart terminals for it to take effect globally." -ForegroundColor Green
}

Write-Host "Done. Start backend with: mvn -DskipTests spring-boot:run" -ForegroundColor Cyan
