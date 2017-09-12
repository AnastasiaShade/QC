set PROGRAM="%~1" 

rem Неверное число входных параметров
%PROGRAM% 5
if NOT ERRORLEVEL 1 goto err

rem Входной параметр содержит нецифровой символ за исключением запятой
%PROGRAM% 5 5 1a
if NOT ERRORLEVEL 1 goto err

rem Входной параметр является отрицательным числом
%PROGRAM% 1 -3 1
if NOT ERRORLEVEL 1 goto err

rem Входной параметр равен нулю
%PROGRAM% 0 1 1
if NOT ERRORLEVEL 1 goto err

rem Обычный треугольник
%PROGRAM% 5 3 4
if NOT ERRORLEVEL 0 goto err

rem Равнобедренный треугольник
%PROGRAM% 6 8 6
if NOT ERRORLEVEL 0 goto err

rem Равносторонний треугольник
%PROGRAM% 10 10 10 
if NOT ERRORLEVEL 0 goto err

rem Не треугольник
%PROGRAM% 1 1 6
if NOT ERRORLEVEL 0 goto err

echo Program testing succeeded 
exit 0

:err 
echo Program testing failed 
exit 1