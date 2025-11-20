# Script para cambiar el nombre del proyecto de MovieQuizGame a CineQuiz

Write-Host "Cambiando nombre del proyecto a CineQuiz..." -ForegroundColor Cyan

# 1. Cambiar settings.gradle
Write-Host "1. Actualizando settings.gradle..." -ForegroundColor Yellow
$settingsPath = "C:\Users\rpcla\AndroidStudioProjects\QuizGame\settings.gradle"
$content = Get-Content $settingsPath -Raw
$content = $content -replace 'rootProject\.name = "QuizGame"', 'rootProject.name = "CineQuiz"'
Set-Content $settingsPath -Value $content -NoNewline
Write-Host "   settings.gradle actualizado" -ForegroundColor Green

# 2. Cambiar app/build.gradle (namespace y applicationId)
Write-Host "2. Actualizando app/build.gradle..." -ForegroundColor Yellow
$buildGradlePath = "C:\Users\rpcla\AndroidStudioProjects\QuizGame\app\build.gradle"
$content = Get-Content $buildGradlePath -Raw
$content = $content -replace "namespace 'com\.example\.quizgame'", "namespace 'com.example.cinequiz'"
$content = $content -replace 'applicationId "com\.example\.quizgame"', 'applicationId "com.example.cinequiz"'
Set-Content $buildGradlePath -Value $content -NoNewline
Write-Host "   app/build.gradle actualizado" -ForegroundColor Green

# 3. Cambiar AndroidManifest.xml (theme)
Write-Host "3. Actualizando AndroidManifest.xml..." -ForegroundColor Yellow
$manifestPath = "C:\Users\rpcla\AndroidStudioProjects\QuizGame\app\src\main\AndroidManifest.xml"
$content = Get-Content $manifestPath -Raw
$content = $content -replace 'Theme\.MovieQuizGame', 'Theme.CineQuiz'
Set-Content $manifestPath -Value $content -NoNewline
Write-Host "   AndroidManifest.xml actualizado" -ForegroundColor Green

# 4. Cambiar values/themes.xml
Write-Host "4. Actualizando themes.xml..." -ForegroundColor Yellow
$themesPath = "C:\Users\rpcla\AndroidStudioProjects\QuizGame\app\src\main\res\values\themes.xml"
$content = Get-Content $themesPath -Raw
$content = $content -replace 'Base\.Theme\.MovieQuizGame', 'Base.Theme.CineQuiz'
$content = $content -replace 'Theme\.MovieQuizGame', 'Theme.CineQuiz'
Set-Content $themesPath -Value $content -NoNewline
Write-Host "   themes.xml actualizado" -ForegroundColor Green

# 5. Cambiar values-night/themes.xml
Write-Host "5. Actualizando themes.xml (night)..." -ForegroundColor Yellow
$themesNightPath = "C:\Users\rpcla\AndroidStudioProjects\QuizGame\app\src\main\res\values-night\themes.xml"
$content = Get-Content $themesNightPath -Raw
$content = $content -replace 'Base\.Theme\.MovieQuizGame', 'Base.Theme.CineQuiz'
Set-Content $themesNightPath -Value $content -NoNewline
Write-Host "   themes.xml (night) actualizado" -ForegroundColor Green

Write-Host ""
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "Cambio de nombre completado exitosamente!" -ForegroundColor Green
Write-Host "Proyecto renombrado de MovieQuizGame a CineQuiz" -ForegroundColor Green
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Archivos modificados:" -ForegroundColor Yellow
Write-Host "  - settings.gradle" -ForegroundColor White
Write-Host "  - app/build.gradle" -ForegroundColor White
Write-Host "  - AndroidManifest.xml" -ForegroundColor White
Write-Host "  - values/themes.xml" -ForegroundColor White
Write-Host "  - values-night/themes.xml" -ForegroundColor White
Write-Host ""
Write-Host "Por favor, sincroniza Gradle en Android Studio." -ForegroundColor Cyan

