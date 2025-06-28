@echo off
echo Sistema de Gestão de Projetos de Comunicação Visual
echo ==================================================
echo.
echo Verificando versão do Java...
java -version
echo.
echo Compilando o projeto...
call mvn clean package
echo.
echo Executando o sistema...
java -jar target\sistema-gestao-projetos-cv-1.0-SNAPSHOT-jar-with-dependencies.jar
pause
