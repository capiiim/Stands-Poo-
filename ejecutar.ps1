# Ejecutar desde la carpeta FILEY_Java_Backend en PowerShell.
$ErrorActionPreference = 'Stop'
Set-Location $PSScriptRoot
if (-not (Test-Path 'out')) { New-Item -ItemType Directory -Path out | Out-Null }
$archivos = @(Get-ChildItem -Path 'src' -Filter '*.java' -Recurse | ForEach-Object { $_.FullName })
if ($archivos.Count -eq 0) { throw 'No se encontraron archivos Java' }
& javac -encoding UTF-8 --release 17 -d out $archivos
if ($LASTEXITCODE -ne 0) { throw 'Falló la compilación Java' }
& java -cp out filey.Main
if ($LASTEXITCODE -ne 0) { throw 'Error al ejecutar FILEY' }
