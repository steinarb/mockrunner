Mockrunner is a lightweight framework for unit testing applications in
the J2EE environment. It supports Struts actions and forms, servlets, 
filters and tag classes. Furthermore it includes a JDBC and a JMS test framework. 
The JDBC test framework can be used standalone or in conjunction with MockEJB 
(http://mockejb.sourceforge.net/) to test EJB based applications.

Mockrunner extends JUnit and simulates the necessary behaviour without calling 
the real infrastructure. It does not need a running application server or
a database. Furthermore it does not call the webcontainer or the
Struts ActionServlet. It is very fast and enables the user to manipulate all 
involved classes and mockobjects in all steps of the test. 
It can be used to write very sophisticated unit-tests for J2EE based 
applications without any overhead. 

Mockrunner does not read any configuration file like web.xml 
or struts-config.xml. You can specify all parameters using the 
Mockrunner API. So it is possible to test servlets, filters, tags
and Struts actions as reusable components regardless of the settings 
you use in one or another application. It is not possible to test the
definitions in the configuration files. If you want to do that you can
use StrutsTestCase (http://strutstestcase.sourceforge.net/) for
Struts based applications or Cactus (http://jakarta.apache.org/cactus/).

The JDBC test framework simulates a database but it does not execute any
SQL statements. You can use the Mockrunner API to specify the results the 
database would provide when executing different SQL statements. The
framework is meant for testing the Java part of JDBC based applications.
If you want to test SQL code you can use SQLUnit (http://sqlunit.sourceforge.net/)
or dbUnit (http://dbunit.sourceforge.net/) which runs against a real database.

The JMS test framework implements all JMS interfaces and can be used to test JMS 
based code. The JMS test framework is able to send and receive messages and to keep
track of everything that happens while delivering the message.
Receivers can be plain Java classes or message driven beans.

All test modules in Mockrunner can be combined. You can test a Servlet
that calls a SessionBean that uses some JDBC code to read data from a database.

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

Mockrunner requires at least Java 1.3 to run. If you are using Java 1.3
you have to use the mockrunner-jkd1.3.jar. The JDBCTestModule is limited
to JDBC API 2.1 in this case. If you are running Java 1.4 you can use
JDBC 3.0. You have to use mockrunner.jar in this case. The other test modules
are identical in both versions. Struts versions 1.1 and 1.2 are supported.
It's not possible to use Mockrunner with Struts 1.0.

To start with Mockrunner check out the JavaDoc and the examples
in the com.mockrunner.example packages.
Most methods are self-explanatory. Just subclass one of the adapters
(e.g. ActionTestCaseAdapter) and start testing. If you have your
own base class for tests, you can use the modules (e.g. ActionTestModule).

Mockrunner uses some classes from ActiveMQ project (http://activemq.codehaus.org/)
for JMS message selector parsing. The classes are modified in order to
fit the needs of Mockrunner. The corresponding classes are in a different package
than the original ActiveMQ classes, so you should not face classloading problems
when using ActiveMQ in your project.

Mockrunner depends on some third-party libraries. Most of them are included
in the release. Not included are the following libraries.
You have to download them. Please add the specified jars to the classpath:

Xerces XML Parser 2.0.0 (or higher):
http://xml.apache.org/xerces2-j/index.html
xml-apis.jar
xercesImpl.jar

The following libraries are included in the Mockrunner release. Just add them 
to the classpath. 
If you extend one of the test adapters (e.g. JDBCTestCaseAdapter), you have 
to add all third-party libraries to the classpath, because the base class 
for all adapters, BaseTestCase, depends on them. If you only use one test 
module, e.g. JDBCTestModule, it's also possible, to create the test module 
using:

JDBCMockObjectFactory factory = new JDBCMockObjectFactory();
JDBCTestModule module = new JDBCTestModule(factory);

In this case you don't have to add all libraries, just those that are
related to the test module you use.

The third-party libraries included in the Mockrunner release are:

Struts 1.2.1
http://jakarta.apache.org/struts
struts.jar

JUnit 3.8.1:
http://www.junit.org
junit.jar

J2EE 1.3:
Tomcat 4.1.30:
http://jakarta.apache.org/tomcat
servlet.jar
JBoss 3.2.3:
http://www.jboss.org
jboss-j2ee.jar                

Commons (from Struts 1.2.1 release):
http://jakarta.apache.org/commons       
commons-logging.jar
commons-logging-api.jar
commons-collections.jar
commons-beanutils.jar
commons-digester.jar
commons-validator.jar
commons-lang-2.0.jar (not included in Struts 1.2.1 release)

ORO (from Struts 1.2.1 release):
http://jakarta.apache.org/oro
jakarta-oro.jar

MockEJB 0.6 beta1:
http://mockejb.sourceforge.net
mockejb.jar

cglib (from MockEJB 0.6 beta1 release):
http://cglib.sourceforge.net
cglib-full-2.0-RC2.jar

JDOM Beta 10:
http://www.jdom.org
jdom.jar

NekoHTML Parser 0.9.2:
http://www.apache.org/~andyc/neko/doc/html/index.html
nekohtml.jar

IMPORTANT NOTE: There is a problem when using the JUnit GUI testrunners
together with Jakarta commons logging. If you are facing strange exceptions
regarding logger configuration, please put mockrunner.jar before junit.jar
in your classpath.

For suggestions, remarks or questions you can use the forums on
Sourceforge (http://sourceforge.net/projects/mockrunner/) or 
write me an email (alwin.ibba AT mockrunner.com).
