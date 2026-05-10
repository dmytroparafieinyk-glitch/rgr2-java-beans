$ErrorActionPreference = "Stop"

$Name = "Dmytro Parafieinyk"
$Email = "dmytro.parafieinyk@student.karazin.ua"

function Test-GitRepository {
    git rev-parse --is-inside-work-tree *> $null
    return $LASTEXITCODE -eq 0
}

if (-not (Test-GitRepository)) {
    if (Test-Path ".git") {
        Write-Host "Removing incomplete .git directory from a previous failed initialization..."
        Remove-Item -LiteralPath ".git" -Recurse -Force
    }
    git init -b main
}

git config user.name $Name
git config user.email $Email

git add .gitignore
git commit -m "Configure project ignores" 2>$null

git add data/sample.xml src/main/java/mybeans/Data.java src/main/java/mybeans/DataSheet.java
git add src/main/java/xml/DataHandler.java src/main/java/xml/SAXRead.java src/main/java/xml/DataSheetToXML.java
git commit -m "Add data model and XML helpers" 2>$null

git add src/main/java/mybeans/DataSheetChangeEvent.java src/main/java/mybeans/DataSheetChangeListener.java
git add src/main/java/mybeans/DataSheetTableModel.java src/main/java/mybeans/DataSheetTable.java src/main/java/mybeans/DataSheetTableBeanInfo.java
git add src/main/java/mybeans/DataSheetGraph.java src/main/java/mybeans/DataSheetGraphBeanInfo.java
git add src/main/resources/META-INF/beans.mf
git commit -m "Add JavaBeans table and graph components" 2>$null

git add src/main/java/myapplication/Test.java src/main/resources/META-INF/app.mf
git commit -m "Add Swing application using JavaBeans" 2>$null

git add build.ps1 README.md src/test/java/SmokeTest.java dist/databeans.jar dist/rgr2-app.jar
git commit -m "Add build script tests and packaged jars" 2>$null

Write-Host ""
Write-Host "Git setup is complete."
Write-Host "Configured author: $Name <$Email>"
Write-Host ""
git log --oneline --decorate --all
