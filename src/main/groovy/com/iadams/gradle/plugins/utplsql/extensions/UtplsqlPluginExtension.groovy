/*
 * Gradle Utplsql Plugin
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Iain Adams
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.iadams.gradle.plugins.utplsql.extensions

import org.gradle.api.Project

/**
 * Created by Iain Adams.
 */
class UtplsqlPluginExtension {
  /**
   * The JDBC driver to use. Defaults to Oracle.
   */
  String driver = 'oracle.jdbc.driver.OracleDriver'

  /**
   * The JDBC URL to use.
   */
  String url = ''

  /**
   * The username to connect to the database.
   */
  String username = ''

  /**
   * The password to connect to the database.
   */
  String password = ''

  /**
   * The source directory for all the unit tests.
   */
  String sourceDir

  /**
   * Filter for which files to include in the sourceDir
   */
  String includes = '**/*.pks, **/*.pkb'

  /**
   * Filter for which files to excludes in the sourceDir
   */
  String excludes = ''

  /**
   * The output directory for the test results.
   */
  String outputDir

  /**
   * The output directory for the html reports.
   */
  String reportsDir

  /**
   * The type of test method to execute. Can be either test or run. Defaults to test.
   */
  String testMethod = 'run'

  /**
   * The setup method to use. set to TRUE to execute setup and teardown for each procedure. FALSE to execute for each package. Default is FALSE.
   */
  Boolean setupMethod = false

  /**
   * In the event of no tests being run, fail the build.
   */
  Boolean failOnNoTests = true

  UtplsqlPluginExtension(Project project) {
    sourceDir = "${project.projectDir}/src/test/plsql"
    outputDir = "${project.buildDir}/utplsql"
    reportsDir = "${project.buildDir}/reports/utplsql"
  }
}
