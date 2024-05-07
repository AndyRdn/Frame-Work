cls
set server_directory="D:\travail\Mr Naina\testFramework\Build\WEB-INF\lib"

xcopy "out\production\Frame-Work" "build\" /Y /E /I
@REM manao war
jar cf Frame-work.jar -C build .
@REM deployer
copy Frame-work.jar %server_directory%