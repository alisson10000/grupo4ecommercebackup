@echo off
REM ============================================
REM Inicia a API Spring Boot do projeto Serratec
REM ============================================

cd /d "G:\Senai  Serratec FullStack\ApiRestfull\Trabalho final api\TrabalhoFinalAPI-Grupo4\target"

echo Iniciando o servidor Spring Boot...
echo ------------------------------------
java -jar TrabalhoFinalAPI-Grupo4-0.0.1-SNAPSHOT.jar

echo ------------------------------------
echo Servidor encerrado.
pause
