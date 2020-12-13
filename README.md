# Abhisek_Mohanty_TestVagrant
Assignment for TestVagrant

# Steps to Run the code:

1.Clone the Project to you local system.

2.Open eclipse IDE and Import this project.

3.For Dependencies, no need to add individually, all the depencies are mentioned in the POM.xml file. Once you import the project all dependcies will be downloaded automatically.

Still if the dependencies are not downloaded then follow these steps: In Eclipse right click on the project->Maven->Update Project->Select "Project" check box->select 'Forced Update of Snapshots/Releases'.

4.Expand the folder "Weather-Reporting-Comparator".

5.Select the testng.xml file.

6.Right click on that file then select "Run as" --> "TestNG Suite".

7.To see the test case execution logs check the application.log file in test/src/java/config folder.

8.For test case failures screenshot check the Screenshots folder in the user directory.



# To run the program from command propmt once the dependencies has been resolved:

1.open command prompt.

2.Go to the location where the project is present from command prompt.

3.Then type "mvn clean test -DsuiteXmlFile=testng.xml"


# Test Data:
 1.Test data are driven from config.properties file and the path to this file is Weather-Reporting-Comparator/src/test/java/config/config.properties.
 
 
# To check the logs
 1.To see the logs open application.log file and the path to this file is Weather-Reporting-Comparator/src/test/java/config/application.log.

