Mockrunner is a lightweight framework for testing struts actions
and forms, servlets and tag classes. It extends JUnit and simulates 
the necessary behaviour without calling the real infrastructure, i.e. 
it does not call the struts ActionServlet or any other struts class 
except for the tested action. This makes it very fast and enables the 
user to manipulate all invloved classes and mockobjects in all steps 
of the test making it possible to write very sophisticated unit-tests 
for struts and servlets without any overhead. The framework was inspired 
by StrutsTestCase (http://strutstestcase.sourceforge.net/)
but uses a different approach to make it faster and more flexible.

Mockrunner does not read the struts-conig.xml file or any other
configuration file (like web.xml). You have to specify all preconditions
and parameters using the Mockrunner API. For example you can specify
if you want to have form validation and you can manipulate the parameter
attribute. This makes it possible to test the action as a reusable
component with all possible parameters and with different form beans.
So you can be sure the action works properly regardless of the settings 
you use for that action in one or another application.

Mockrunner is a pure mock based solution for unit tests. It's purpose
is to test one single action, form bean, tag class or servlet at a time
without overhead. It is not meant for workflow tests or use-case tests.
Furthermore you cannot test the definitions in the struts-config.xml file. 
If you want to do that, you can use StrutsTestCase
(http://strutstestcase.sourceforge.net/) or Cactus 
(http://jakarta.apache.org/cactus/).

There is a simple mechanism included in Mockrunner that makes it possible
to write multithread tests. You can simulate multiple requests from
the same client or from different clients and test concurrent access
to shared ressources efficiently. Have a look at the StoreDataAction
example to see how to do that.

It is planned to include support for other frameworks in the J2EE
environment in future releases.

To start with Mockrunner check out the JavaDoc and the examples
in the com.mockrunner.example and com.mockrunner.example.test packages.
Most methods are self-explanatory. Just subclass one of the Adapters
(e.g. ActionTestCaseAdapter) and start testing. If you have your
own TestCase base class, you can use the Modules (e.g. ActionTestModule).

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

Commons (also included in Struts release):
http://jakarta.apache.org/commons       
commons-logging.jar
commons-collections.jar
commons-beanutils.jar

Mockobjects 0.09:
http://www.mockobjects.com
mockobjects-core-0.09.jar
mockobjects-jdk1.3-j2ee1.3-0.09.jar

For suggestions, remarks or questions you can use the forums on
Sourceforge (http://sourceforge.net/projects/mockrunner/) or 
write me an email (alwin.ibba@mockrunner.com).
