Mockrunner is a lightweight framework for testing struts actions
and forms, servlets and tag classes. It simulates the necessary 
behavior without calling the real infrastructure, i.e. it does 
not call the struts ActionServlet or any other Struts class except 
for the tested action. This makes it very fast and enables the 
user to manipulate all invloved classes and mockobjects in all
steps of the test making it possible to write very sophisticated 
unit-tests for struts and servlets without any overhead.
The framework was inspired by StrutsTestCase 
(http://strutstestcase.sourceforge.net/)
but uses a different approach to make it faster and more flexible.
It is not meant for coarse grained action and workflow tests. 
For that purpose you can use StrutsTestCase or Cactus
(http://jakarta.apache.org/cactus/).

It is planned to include support for other frameworks in the J2EE
environment in future releases.

For usage examples have a look at the de.mockrunner.example and 
de.mockrunner.example.test package.

Mockrunner uses the following libraries and software components.
You have to add them to the classpath:

JUnit 3.8.1:
http://www.junit.org
junit.jar

Servlet J2EE:
http://jakarta.apache.org/tomcat
servlet.jar                            

Struts 1.1:
http://jakarta.apache.org/struts
struts.jar             
commons-logging.jar
commons-collections.jar
commons-beanutils.jar

Mockobjects 0.09:
http://www.mockobjects.com
mockobjects-core-0.09.jar
mockobjects-jdk1.3-j2ee1.3-0.09.jar

