set PROGRAM="%~1" 

rem �������� ����� ������� ����������
%PROGRAM% 5
if NOT ERRORLEVEL 1 goto err

rem ������� �������� �������� ���������� ������ �� ����������� �������
%PROGRAM% 5 5 1a
if NOT ERRORLEVEL 1 goto err

rem ������� �������� �������� ������������� ������
%PROGRAM% 1 -3 1
if NOT ERRORLEVEL 1 goto err

rem ������� �������� ����� ����
%PROGRAM% 0 1 1
if NOT ERRORLEVEL 1 goto err

rem ������� �����������
%PROGRAM% 5 3 4
if NOT ERRORLEVEL 0 goto err

rem �������������� �����������
%PROGRAM% 6 8 6
if NOT ERRORLEVEL 0 goto err

rem �������������� �����������
%PROGRAM% 10 10 10 
if NOT ERRORLEVEL 0 goto err

rem �� �����������
%PROGRAM% 1 1 6
if NOT ERRORLEVEL 0 goto err

echo Program testing succeeded 
exit 0

:err 
echo Program testing failed 
exit 1