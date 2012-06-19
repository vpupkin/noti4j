@echo  ^<settingsSecurity^>^<master^> >master.xml
@call mvn --encrypt-master-password start>>master.xml
@echo ^</master^>^</settingsSecurity^> >>master.xml
mvn -Dsettings.security=master.xml --encrypt-password 004916098989882