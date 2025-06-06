#!/bin/bash

echo "==============================="
echo "   GESTOR DE TAREAS - JAVA"
echo "==============================="
echo

echo "Compilando archivos Java..."
javac *.java

if [ $? -eq 0 ]; then
    echo "¡Compilación exitosa!"
    echo
    echo "Iniciando Gestor de Tareas..."
    echo
    java Main
else
    echo "Error en la compilación!"
    echo "Verifica que Java esté instalado correctamente."
    echo
    read -p "Presiona Enter para continuar..."
fi 