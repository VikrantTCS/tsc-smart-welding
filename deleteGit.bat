set PROJECT_PATH=C:\Users\709105\Documents\tsc-smart-welding\predix-rmd-ref-app
for /d /r "%PROJECT_PATH%" %%a in (target\) do if exist "%%a" rmdir /s /q "%%a"
for /d /r "%PROJECT_PATH%" %%a in (.settings\) do if exist "%%a" rmdir /s /q "%%a"
for /d /r "%PROJECT_PATH%" %%a in (.classpath) do if exist "%%a" DEL /s /q "%%a"
for /d /r "%PROJECT_PATH%" %%a in (.project) do if exist "%%a" DEL /s /q "%%a"
for /d /r "%PROJECT_PATH%" %%a in (.gitignore) do if exist "%%a" DEL /s /q "%%a"
for /d /r "%PROJECT_PATH%" %%a in (.git) do if exist "%%a" DEL /s /q "%%a"