Gradle Utplsql Plugin
=========

This is a gradle plugin for the [utplsql] tool.

Latest Version
--------------
[ ![Download](https://api.bintray.com/packages/iwarapter/gradle-plugins/gradle-utplsql-plugin/images/download.svg) ](https://bintray.com/iwarapter/gradle-plugins/gradle-utplsql-plugin/_latestVersion)


Build Status
------------
[![Build Status](https://travis-ci.org/iwarapter/gradle-utplsql-plugin.svg)](https://travis-ci.org/iwarapter/gradle-utplsql-plugin)

Usage
-----------

To apply the plugin:
```
buildscript {
  repositories {
    maven { url 'http://dl.bintray.com/iwarapter/gradle-plugins/' }
  }
  dependencies {
    classpath 'com.iadams:gradle-utplsql-plugin:0.1.2'
  }
}

apply plugin: 'com.iadams.utplsql'

repositories {
  mavenCentral()
}
dependencies {
  junitreport 'org.apache.ant:ant-junit:1.9.4'
  driver 'com.oracle:ojdbc6:11.2.0.1.0' //<-- Add your jdbc driver
}
```

Tasks
-----------
```
Utplsql tasks
-------------
utDeploy - Deploys all the UTPLSQL tests in the test folder with JDBC driver.
utplsql - Deploy, Run, Report
utReport - Generates HTML reports from the XML results.
utRun - Executes all utPLSQL tests.
```

Rules
-----------
```
Rules
-----
Pattern: clean<TaskName>: Cleans the output files of a task.
Pattern: utRun-<TestName>: Executes a specific UTPLSQL test.
Pattern: utTest-<TestName>: Executes the UTPLSQL tests for a specific object.
Pattern: utDeploy-<TestName>: Deploys a specific UTPLSQL test.
Pattern: utGen-<TestName>: Generates UTPLSQL test spec and body shell.
```

## Configuration

### build.gradle
```groovy
utplsql {
  url = 'jdbc:oracle:thin:@localhost:1521:test'
  username = 'testing'
  password = 'testing'
  sourceDir = "tests"
  includes = '**/*.pks, **/*.pkb'
  excludes = ''
  outputDir = 'build/utplsql'
  testMethod = 'run'
  setupMethod = false
  failOnNoTests = true
}
```

* `url` : JDBC Connection URL.
* `username` : Database schema username.
* `password` :  Database Schema password.
* `sourceDir` : The source directory for all the unit tests.
* `includes` : Comma seperated list of files to include.
* `excludes` :  Comma seperated list of files to ignore.
* `outputDir` : The output directory for the test results.
* `testMethod` : The type of test method to execute. Can be either test or run. Defaults to test.
* `setupMethod` : The setup method to use. Set to true to execute setup and teardown for each procedure and false to execute for each package. Default is false.
* `failOnNoTests` : In the event of no tests being run, fail the build.

[utplsql]:http://utplsql.sourceforge.net/
