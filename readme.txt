Mockrunner is a lightweight framework for unit testing applications in
the J2EE environment. It supports servlets, filters, tag classes and Struts actions 
and forms. Furthermore it includes a JDBC, a JMS and a JCA test framework and can 
be used in conjunction with MockEJB (http://mockejb.sourceforge.net/)
to test EJB based applications.

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

The JCA test framework can be used to simulate backend systems that are accessed 
through the use of the JCA Common Client Interface API. Application code that uses 
the Common Client Interface can be executed against a simulated connector. The test 
framework intercepts the backend call and provides a suitable response.

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
example to see how to do that. Please note that this feature is a
bit experimental at the moment. Not all mock objects are properly 
synchronized, so you should use this feature with care.

For components that produce HTML fragments as output (e.g. servlets
and tags) Mockrunner provides an API to test this output as pure text
or as parsed XML.

It is planned to include support for other frameworks in the J2EE
environment in future releases.

Mockrunner supports the Java versions 1.3 to 1.6 and the J2EE version 1.3, 
1.4 and 5. The lib directory contains the jar files for all Java and J2EE
versions in the respective subdirectory. 
Each jar file contains a jarversion.txt which lists the JDK and J2EE version
this jar supports.
Mockrunner supports the Struts versions 1.1, 1.2 and 1.3. It's not possible 
to use Mockrunner with Struts 1.0 or J2EE 1.2.

To start with Mockrunner check out the JavaDoc and the examples
in the com.mockrunner.example packages. Most methods are self-explanatory.

Mockrunner uses some classes from ActiveMQ project (http://activemq.apache.org)
for JMS message selector parsing. The classes are modified in order to
fit the needs of Mockrunner. The corresponding classes are in a different package
than the original ActiveMQ classes, so you should not face classloading problems
when using ActiveMQ in your project.

Mockrunner depends on some third-party libraries. You have to add them to the classpath.
You can subclass one of the standard adapters (e.g. ActionTestCaseAdapter)
which extend BaseTestCase. BaseTestCase provides easy access to all test modules and all
mock object factories. BaseTestCase depends on all third-party libraries but you don't
have to add all the jar files to the classpath, just those that are related to the 
test module you use, because the factories are lazy initialized when necessary.
In addition to the "all-in-one"-file mockrunner.jar, separate jars for the 
different technologies are provided. These additional jar files contain only the classes 
necessary to test the corresponding technology. If you only need one technology it's 
recommended to use the corresponding jar file instead of the "all-in-one" jar file. 
E.g. if you only want to use the JDBC test framework, you can use mockrunner-jdbc.jar.
You have to to subclass the basic adapter versions (e.g. BasicJDBCTestCaseAdapter) in 
this case, which do not extend BaseTestCase and do not depend on all third-party libraries.

Please note, that all adapters just delegate to their corresponding module. If you 
don't want to extend a Mockrunner adapter, you can always create a module like this:

JDBCMockObjectFactory factory = new JDBCMockObjectFactory();
JDBCTestModule module = new JDBCTestModule(factory);

Please check out the files dependencies.txt and dependenciesj2ee1.3.txt for a detailed 
list of required third-party libraries for each mockrunner provided jar file.

The third-party libraries included in the Mockrunner release are:

Xerces XML Parser 2.10.0:
http://xml.apache.org/xerces2-j/index.html
xml-apis.jar
xercesImpl.jar

Struts 1.3.10:
http://struts.apache.org
struts-core-1.3.10.jar
struts-extras-1.3.10.jar
struts-taglib-1.3.10.jar

Struts 1.2.9:
http://struts.apache.org
struts.jar

JUnit 3.8.2:
http://www.junit.org
junit.jar

JavaEE 5:
Tomcat 6.0.29
http://tomcat.apache.org
servlet-api.jar
jsp-api.jar
el-api.jar
jasper.jar
jasper-el.jar
JBoss 5.1.0:
http://www.jboss.org
jboss-javaee-modified.jar (modified version of jboss-javaee.jar, 
                           added the org/jboss/util/id classes because jboss-javaee.jar contains references to these classes)

J2EE 1.4:
Tomcat 5.5.30:
http://tomcat.apache.org
servlet-api.jar
jsp-api.jar
JBoss 4.0.1sp1:
http://www.jboss.org
jboss-j2ee.jar

J2EE 1.3:
Tomcat 4.1.40:
http://tomcat.apache.org
servlet.jar
JBoss 3.2.3:
http://www.jboss.org
jboss-j2ee.jar
jboss-jaas.jar

Commons:
http://commons.apache.org
commons-logging-1.0.4.jar (from Struts 1.3.10 release)
commons-beanutils-1.8.0.jar (from Struts 1.3.10 release)
commons-digester-1.8.jar (from Struts 1.3.10 release)
commons-validator-1.3.1.jar (from Struts 1.3.10 release)

MockEJB 0.6 beta2:
http://mockejb.sourceforge.net
mockejb.jar

ORO:
http://jakarta.apache.org/oro
jakarta-oro-2.0.8.jar (from MockEJB 0.6 beta2 release)

cglib 2.2:
http://cglib.sourceforge.net
cglib-nodep-2.2.jar

JDOM 1.1.1:
http://www.jdom.org
jdom.jar

NekoHTML Parser 1.9.14:
http://nekohtml.sourceforge.net
nekohtml.jar

The above libraries are necessary at runtime. If you try to build Mockrunner
you will probably recognize that some libraries are missing. The com.mockrunner.gen
packages contain tools to generate code for older Java and J2EE versions and to analyze 
dependencies. If you just want to make small modifications to Mockrunner,
you probably won't need these tools. In this case, simply delete the com.mockrunner.gen
packages and the corresponding tests. If you want to use these tools, you need
the following libaries:

BCEL 5.2:
http://jakarta.apache.org/bcel
bcel-5.2.jar

JarAnalyzer 1.2:
http://www.kirkk.com/main/Main/JarAnalyzer
jaranalyzer-1.2.jar

regexp 1.3:
http://jakarta.apache.org/regexp
jakarta-regexp-1.3.jar (from JarAnalyzer 1.2 release)

You can also get Mockrunner from CVS to have everything in place including
a build script:
cvs -d:pserver:anonymous@mockrunner.cvs.sourceforge.net:/cvsroot/mockrunner

IMPORTANT NOTE: In order to fix problems with JUnits TestCaseClassLoader, all
Mockrunner jar files contain an excluded.properties file including all org.apache,
com.mockrunner and org.mockejb classes. If you are facing classloading problems,
put the mockrunner.jar before junit.jar in your classpath or turn of class
reloading in JUnit.

For suggestions, remarks or questions you can use the forums on
Sourceforge (http://sourceforge.net/projects/mockrunner/) or 
write me an email (alwin.ibba AT mockrunner.com).
