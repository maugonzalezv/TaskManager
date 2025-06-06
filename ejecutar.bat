@echo off
echo ===============================
echo    GESTOR DE TAREAS - JAVA
echo ===============================
echo.

echo Compilando archivos Java...
javac *.java

if %ERRORLEVEL% equ 0 (
    echo Compilacion exitosa!
    echo.
    echo Iniciando Gestor de Tareas...
    echo.
    java Main
) else (
    echo Error en la compilacion!
    echo Verifica que Java este instalado correctamente.
    echo.
    pause
) 