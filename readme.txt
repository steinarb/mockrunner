Mockrunner is a lightweight framework for testing struts actions
and forms, servlets, filters and tag classes. It extends JUnit and 
simulates  the necessary behaviour without calling the real infrastructure.
It does not call the webcontainer or the Struts ActionServlet.
This makes it very fast and enables the user to manipulate all involved 
classes and mockobjects in all steps  of the test making it possible to 
write very sophisticated unit-tests for web based applications without 
any overhead.

Mockrunner does not read any configuration file like web.xml 
or struts-config.xml. You can specify all parameters using the 
Mockrunner API. This makes it possible to test servlets, filters, tags
and Struts actions as reusable components regardless of the settings 
you use in one or another application. It is not possible to test the
definitions in the configuration files. If you want to do that you can
use StrutsTestCase (http://strutstestcase.sourceforge.net/) for
Struts based applications or Cactus (http://jakarta.apache.org/cactus/).

Mockrunner is a pure mock based solution for unit tests. It's purpose
is to write very fast unit tests without overhead. It is not meant 
for workflow tests or use-case tests. Furthermore it does not support
any type of in-container testing.

There is a simple mechanism included in Mockrunner that makes it possible
to write multithread tests. You can simulate multiple requests from
the same client or from different clients and test concurrent access
to shared ressources efficiently. Have a look at the StoreDataAction
example to see how to do that.

For components that produce HTML fragments as output (e.g. servlets
and tags) Mockrunner provides an API to test this output as pure text
or as parsed XML.

It is planned to include support for other frameworks in the J2EE
environment in future releases.

To start with Mockrunner check out the JavaDoc and the examples
in the com.mockrunner.example packages.
Most methods are self-explanatory. Just subclass one of the Adapters
(e.g. ActionTestCaseAdapter) and start testing. If you have your
own base class for tests, you can use the Modules (e.g. ActionTestModule).

Mockrunner uses the following libraries and software components.
You have to download them. Please add the specified jars to the classpath.

Struts 1.1 (or higher):
http://jakarta.apache.org/struts
struts.jar

Xerces XML Parser 2.0.0 (or higher):
http://xml.apache.org/xerces2-j/index.html
xml-apis.jar
xercesImpl.jar

The following libraries are included in the Mockrunner release.
Just add them to the classpath.

JUnit 3.8.1:
http://www.junit.org
junit.jar

J2EE:
http://jakarta.apache.org/tomcat
servlet.jar
http://www.jboss.org
jboss-j2ee.jar                

Commons:
http://jakarta.apache.org/commons       
commons-logging.jar
commons-collections.jar
commons-beanutils.jar

Mockobjects 0.09:
http://www.mockobjects.com
mockobjects-core-0.09.jar
mockobjects-jdk1.4-0.09.jar
mockobjects-jdk1.4-j2ee1.3-0.09.jar

MockEjb 0.4:
http://www.mockejb.org
mockejb.jar

JDOM Beta 9 (or higher):
http://www.jdom.org
jdom.jar

NekoHTML Parser:
http://www.apache.org/~andyc/neko/doc/html/index.html
nekohtml.jar

For suggestions, remarks or questions you can use the forums on
Sourceforge (http://sourceforge.net/projects/mockrunner/) or 
write me an email (alwin.ibba AT mockrunner.com).

