#VMVM - Unit Test Virtualization for Java
==========
**VMVM** is a system for isolating unit tests with very low overhead.

This repository contains the source and a [runnable binary](https://github.com/Programming-Systems-Lab/vmvm/blob/master/binaries/vmvm.jar) for **VMVM**.

###For more information
Please refer to our [Technical Report](http://mice.cs.columbia.edu/getTechreport.php?techreportID=1549) or email [Jonathan Bell](mailto:jbell@cs.columbia.edu)

Running
-----
### Instrumenting your code
Execute the instrumenter. Make sure that the asm library and the vmvm library are both in your classpath, then run the Instrumenter class with two arguments. The first argument is the folder containing the project to instrument. The second argument is the destination for the instrumented version.
Example:
`java -cp lib/asm-all-4.1.jar:vmvm.jar edu.columbia.cs.psl.vmvm.Instrumenter folder-to-instrument folder-dest`


### Modifying your build scripts
To automatically have VMVM be called by ant, modify the `junit` task of your `build.xml` file to add the following elements:

```xml
<classpath>
<pathelement path="ant-mvn-formatter.jar" />
<pathelement location="vmvm.jar"/>
</classpath>
<formatter classname="edu.columbia.cs.psl.vmvm.AntJUnitTestListener" extension=".xml"/>
<jvmarg value="-Xbootclasspath/a:vmvm.jar:asm-all-4.1.jar"/>
```

Make sure that the paths are correct to [vmvm.jar](https://github.com/Programming-Systems-Lab/vmvm/blob/master/binaries/vmvm.jar) and [ant-mvn-formatter.jar](https://github.com/Programming-Systems-Lab/vmvm/blob/master/binaries/ant-mvn-formatter.jar). Also be sure that your `junit` task has the argument `forkMode="once"` (e.g. that it won't fork a new process for each test, defating the purpose of VMVM).

That's it. You may also need to include the vmvm.jar in the ant master classpath, by adding the argument `-lib=vmvm.jar` when you invoke ant.

To automatically have VMVM be called by mvn, modify the `pom.xml` file for your project as follows:

1.	Make sure that you are using a recent version of the surefire plugin (e.g., 2.15)
1.	Add the [vmvm.jar](https://github.com/Programming-Systems-Lab/vmvm/blob/master/binaries/vmvm.jar) and [ant-mvn-formatter.jar](https://github.com/Programming-Systems-Lab/vmvm/blob/master/binaries/ant-mvn-formatter.jar) to the surefire additional classpath (e.g. within the plugin configuration for surefire add):

```xml
<additionalClasspathElements>
<additionalClasspathElement>vmvm.jar</additionalClasspathElement>
<additionalClasspathElement>ant-mvn-formatter.jar</additionalClasspathElement>
</additionalClasspathElements>
```

3.	Register the VMVM test listener with surefire. If you don't have any other properties set for the surefire plugin, then that would look like this:

```xml
<properties>
<property>
<name>listener</name>
<value>edu.columbia.cs.psl.vmvm.MvnVMVMListener</value>
</property>
</properties>
```

Here's an example of a complete pom.xml block declaring the surefire plugin and registering VMVM with it:
```xml
<plugin>
	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-surefire-plugin</artifactId>
	<version>2.16</version>
	<configuration>
		<additionalClasspathElements>
		<additionalClasspathElement>vmvm.jar</additionalClasspathElement>
		<additionalClasspathElement>ant-mvn-formatter.jar</additionalClasspathElement>
	</additionalClasspathElements>
		<properties>
		<property>
			<name>listener</name>
			<value>edu.columbia.cs.psl.vmvm.MvnVMVMListener</value>
		</property>
		</properties>
	</configuration>
	<executions>
		<execution>
		<id>plain</id>
		<configuration>
			<includes>
				<include>**/*Test.java</include>
			</includes>
		</configuration>
		</execution>
	</executions>
</plugin>
```

In case of problems..
-----
We have verified that VMVM works with the following applications. If you notice any incompatibilities with it, please email [Jonathan Bell](mailto:jbell@cs.columbia.edu), (or, fix the bug yourself and send a patch :P).

Applications that we have tested with VMVM (and are known to work):
* [Apache Ivy](http://ant.apache.org/ivy/)
* [Apache Nutch](http://nutch.apache.org/)
* [Apache River](http://river.apache.org/)
* [Apache Tomcat](http://tomcat.apache.org/)
* [betterFORM](http://www.betterform.de/en/index.html)
* [Bristlecone](http://www.ohloh.net/p/bristlecone)
* [btrace](https://kenai.com/projects/btrace)
* [Closure Compiler](http://commons.apache.org/proper/commons-jci/)
* [Commons Codec](http://commons.apache.org/proper/commons-codec/)
* [Commons IO](http://commons.apache.org/proper/commons-io/)
* [Commons Validator](http://commons.apache.org/proper/commons-validator/)
* [FreeRapid Downloader](http://wordrider.net/freerapid/)
* [gedcom4j](http://gedcom4j.org/main/)
* [JAXX](http://nuiton.org/projects/jaxx)
* [Jetty](http://www.eclipse.org/jetty/jtor* )
* [JTor](https://github.com/brl/JTor)
* [mkgmap](http://wiki.openstreetmap.org/wiki/Mkgmap)
* [Openfire](http://www.igniterealtime.org/projects/openfire/)
* [Trove, for Java](http://trove.starlight-systems.com/)
* [Universal Password Mananager (UPM)](http://upm.sourceforge.net/)

License
------
This software is released under the MIT license.

Copyright (c) 2013, by The Trustees of Columbia University in the City of New York.

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


Acknowledgements
-----
This project makes use of the following libraries:
* [ASM](http://asm.ow2.org/license.html), (c) 2000-2011 INRIA, France Telecom, [license](http://asm.ow2.org/license.html)
* [Log4j](http://logging.apache.org/log4j/), (c) 1999-2012, Apache Software Foundation, released under the Apache License 2.0

The authors of this software are [Jonathan Bell](http://jonbell.net) and [Gail Kaiser](http://www.cs.columbia.edu/~kaiser/). The authors are members of the [Programming Systems Laboratory](http://www.psl.cs.columbia.edu/), funded in part by NSF CCF-1161079, NSF CNS-0905246, and NIH U54 CA121852.
