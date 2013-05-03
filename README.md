(c) Copyright 2013 WibiData, Inc.


This project contains the skeleton for a web application that uses the
KijiProject platform. It contains two modules in this directory:

* lib - Packages a JAR of gatherers, producers, importers, etc.
* app - The web application servlet.

To "app" module depends on the lib project. To build the entire project,
type 'mvn install' at the top level (in this directory).

    $ mvn install

If successful, you can launch the web application on your local machine
using the maven jetty plugin. You must do this from the 'app' module.

    $ cd app
    $ mvn jetty:run

Open up a web browser to http://localhost:8080/ to see your app in action!

You must have set HADOOP_HOME and HBASE_HOME environment variables to
allow the web application to work with WibiData. In particular,
$HADOOP_HOME/conf/{core,hdfs,mapred}-site.xml files need to specify the
addresses of your Hadoop cluster, and $HBASE_HOME/conf/hbase-site.xml needs
to specify the address of your ZooKeeper/HBase cluster.
