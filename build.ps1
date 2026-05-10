param(
    [switch]$Clean
)

$ErrorActionPreference = "Stop"
$Root = Split-Path -Parent $MyInvocation.MyCommand.Path
$BuildDir = Join-Path $Root "build"
$DistDir = Join-Path $Root "dist"
$BeanClasses = Join-Path $BuildDir "classes\beans"
$AppClasses = Join-Path $BuildDir "classes\app"
$ArtifactDir = Join-Path $BuildDir "artifacts"
$RunId = [guid]::NewGuid().ToString("N")
$JavaOptions = @("-encoding", "UTF-8", "-source", "8", "-target", "8")

if ($Clean -and (Test-Path $BuildDir)) {
    try {
        Remove-Item -LiteralPath $BuildDir -Recurse -Force
    } catch {
        Write-Warning "Could not fully clean build directory; continuing with overwrite build."
    }
}
if ($Clean -and (Test-Path $DistDir)) {
    try {
        Remove-Item -LiteralPath $DistDir -Recurse -Force
    } catch {
        Write-Warning "Could not fully clean dist directory; continuing with overwrite build."
    }
}

New-Item -ItemType Directory -Force -Path $BeanClasses, $AppClasses, $ArtifactDir, $DistDir | Out-Null

$BeanSources = Get-ChildItem -Path (Join-Path $Root "src\main\java\mybeans") -Filter "*.java" | Sort-Object FullName
javac @JavaOptions -d $BeanClasses @($BeanSources.FullName)
if ($LASTEXITCODE -ne 0) {
    throw "Bean compilation failed."
}

$BeansManifest = Join-Path $Root "src\main\resources\META-INF\beans.mf"
$BeansJar = Join-Path $DistDir "databeans.jar"
$BeansJarBuild = Join-Path $ArtifactDir "databeans-$RunId.jar"
jar cfm $BeansJarBuild $BeansManifest -C $BeanClasses .
if ($LASTEXITCODE -ne 0) {
    throw "Bean JAR packaging failed."
}
Copy-Item -LiteralPath $BeansJarBuild -Destination $BeansJar -Force

$XmlSources = Get-ChildItem -Path (Join-Path $Root "src\main\java\xml") -Filter "*.java" | Sort-Object FullName
$AppSources = Get-ChildItem -Path (Join-Path $Root "src\main\java\myapplication") -Filter "*.java" | Sort-Object FullName
javac @JavaOptions -cp $BeansJar -d $AppClasses @($XmlSources.FullName + $AppSources.FullName)
if ($LASTEXITCODE -ne 0) {
    throw "Application compilation failed."
}

$AppManifest = Join-Path $Root "src\main\resources\META-INF\app.mf"
$AppJar = Join-Path $DistDir "rgr2-app.jar"
$AppJarBuild = Join-Path $ArtifactDir "rgr2-app-$RunId.jar"
jar cfm $AppJarBuild $AppManifest -C $AppClasses .
if ($LASTEXITCODE -ne 0) {
    throw "Application JAR packaging failed."
}
Copy-Item -LiteralPath $AppJarBuild -Destination $AppJar -Force

Write-Host "Created:"
Write-Host "  $BeansJar"
Write-Host "  $AppJar"
