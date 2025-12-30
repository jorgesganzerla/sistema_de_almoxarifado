@echo off
echo Compilando com JDK 25...
"C:\Program Files\Java\jdk-25\bin\javac" -cp ".;mysql-connector-j-9.5.0.jar" *.java

if %errorlevel% == 0 (
    echo Compilacao concluida com sucesso!
) else (
    echo Erro na compilacao. Verifique se o arquivo .jar esta na pasta.
)
pause