@echo off
echo Testando conexao com banco de dados...
"C:\Program Files\Java\jdk-25\bin\javac" -cp ".;mysql-connector-j-9.5.0.jar" TesteConexao.java
"C:\Program Files\Java\jdk-25\bin\java" -cp ".;mysql-connector-j-9.5.0.jar" TesteConexao
pause