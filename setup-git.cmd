@echo off
setlocal

set "NAME=Dmytro Parafieinyk"
set "EMAIL=dmytro.parafieinyk@student.karazin.ua"

git rev-parse --is-inside-work-tree >nul 2>nul
if errorlevel 1 (
    if exist ".git" (
        echo Removing incomplete .git directory from a previous failed initialization...
        rmdir /s /q ".git"
    )
    git init -b main
    if errorlevel 1 (
        git init
        if errorlevel 1 goto :error
        git checkout -b main
        if errorlevel 1 goto :error
    )
)

git config user.name "%NAME%"
if errorlevel 1 goto :error
git config user.email "%EMAIL%"
if errorlevel 1 goto :error

git add .gitignore
call :commit "Configure project ignores"

git add data/sample.xml src/main/java/mybeans/Data.java src/main/java/mybeans/DataSheet.java
git add src/main/java/xml/DataHandler.java src/main/java/xml/SAXRead.java src/main/java/xml/DataSheetToXML.java
call :commit "Add data model and XML helpers"

git add src/main/java/mybeans/DataSheetChangeEvent.java src/main/java/mybeans/DataSheetChangeListener.java
git add src/main/java/mybeans/DataSheetTableModel.java src/main/java/mybeans/DataSheetTable.java src/main/java/mybeans/DataSheetTableBeanInfo.java
git add src/main/java/mybeans/DataSheetGraph.java src/main/java/mybeans/DataSheetGraphBeanInfo.java
git add src/main/resources/META-INF/beans.mf
call :commit "Add JavaBeans table and graph components"

git add src/main/java/myapplication/Test.java src/main/resources/META-INF/app.mf
call :commit "Add Swing application using JavaBeans"

git add build.ps1 setup-git.ps1 setup-git.cmd README.md src/test/java/SmokeTest.java dist/databeans.jar dist/rgr2-app.jar
call :commit "Add build script tests and packaged jars"

echo.
echo Git setup is complete.
echo Configured author: %NAME% ^<%EMAIL%^>
echo.
git log --oneline --decorate --all
goto :eof

:commit
git diff --cached --quiet
if not errorlevel 1 (
    echo Nothing staged for commit: %~1
    goto :eof
)
git commit -m "%~1"
if errorlevel 1 goto :error
goto :eof

:error
echo.
echo Git setup failed. Check the message above.
exit /b 1
