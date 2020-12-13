# Abhisek_Mohanty_TestVagrant
Assignment for TestVagrant

Steps to Run the code:
Clone the Project to you local system.
Open eclipse IDE and Import this project.
For Dependencies, no need to add individually, all the depencies are mentioned in the POM.xml file. Once you import the project all dependcies will be downloaded automatically. Still if the dependencies are not downloaded then follow these steps: In Eclipse right click on the project->Maven->Update Project->Select "Project" check box->select 'Forced Update of Snapshots/Releases'.
Expand the folder "Weather-Reporting-Comparator".
Select the testng.xml file.
Right click on that file then select "Run as" --> "TestNG Suite".
To see the test case execution logs check the application.log file in test/src/java/config folder.
For test case failures screenshot check the Screenshots folder in the user directory.


To run the program from command propmt once the dependencies has been resolved:
open command prompt.
Go to the location where the project is present from command prompt.
Then type "mvn clean test -DsuiteXmlFile=testng.xml"
