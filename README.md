Gradle Utplsql Plugin
=========

This is a gradle plugin for the [utplsql] tool.

Latest Version
--------------
-N/A


Build Status
------------
[![Build Status](https://travis-ci.org/iwarapter/gradle-utplsql-plugin.svg)](https://travis-ci.org/iwarapter/gradle-utplsql-plugin)

Usage
-----------

To apply the plugin:
```
buildscript {
  repositories {
    mavenLocal()
    mavenCentral()
  }
  dependencies {
    classpath 'com.oracle:ojdbc6:11.2.0.1.0' //<-- Add your jdbc driver
    classpath 'com.iadams:gradle-utplsql-plugin:0.1'
  }
}

apply plugin: 'com.iadams.utplsql'
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
