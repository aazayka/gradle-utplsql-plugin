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
package com.iadams.gradle.plugins.utplsql.tasks

import com.iadams.gradle.plugins.utplsql.UtplsqlPlugin
import com.iadams.gradle.plugins.utplsql.core.UtplsqlRunner
import groovy.sql.Sql
import java.sql.SQLException
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/**
 * Created by Iain Adams on 13/09/2014.
 */
class RunTestsTask extends DefaultTask {
  /**
   * The JDBC driver to use. Defaults to Oracle.
   *
   */
  @Input
  String driver
  /**
   * The JDBC URL to use.
   *
   */
  @Input
  String url

  /**
   * The username to connect to the database.
   *
   */
  @Input
  String username

  /**
   * The password to connect to the database.
   *
   */
  @Input
  String password

  /**
   * The type of test method to execute. Can be either test or run. Defaults to test.
   *
   */
  @Input
  String testMethod

  /**
   * The name of all the packages to test.
   *
   */
  @Input
  @Optional
  def packages = []

  /**
   * The setup method to use. set to TRUE to execute setup and teardown for each procedure. FALSE to execute for each package. Default is FALSE.
   *
   */
  @Input
  @Optional
  Boolean setupMethod

  /**
   * Location to load all the tests from.
   *
   */
  @Input
  String sourceDir

  /**
   * Location to which we will write the report file. Defaults to the gradle buildDir.
   *
   */
  @OutputDirectory
  File outputDir

  /**
   * If there is a connection problem or DB PLSQL installation error the plugin reports 0 tests run. This is an error condition which should be
   * flagged, by default an error exception shall be raised if 0 tests are run. This state can be unset in the plugin configuration if desired
   *
   */
  @Input
  @Optional
  Boolean failOnNoTests

  /**
   *
   * @throws GradleException
   */
  @TaskAction
  void runUtplsqlTests() throws GradleException {

    def extension = project.extensions.findByName(UtplsqlPlugin.UTPLSQL_EXTENSION)

    if (packages == []) {
      def files = new FileNameFinder().getFileNames(getSourceDir(), extension.includes, extension.excludes)
      files = files.collect {project.file(it).name.replaceFirst(~/\.[^\.]+$/, '')}
      packages = files.unique {a, b -> a <=> b}
    }

    logger.info "[INFO] URL: ${getUrl()}"
    logger.info "[INFO] Username: ${getUsername()}"
    logger.info "[INFO] Driver: ${getDriver()}"
    logger.info "[INFO] SourceDir: ${project.file(getSourceDir())}"
    logger.info "[INFO] Packages: ${getPackages()}"
    logger.info "[INFO] TestMethod: ${getTestMethod()}"
    logger.info "[INFO] SetupMethod: ${getSetupMethod()}"
    logger.info "[INFO] OutputDir: ${getOutputDir()}"

    try {
      //TODO extract the base configuration for the oracle driver into an abstract task.
      project.configurations.driver.each {File file ->
        project.gradle.class.classLoader.addURL(file.toURL())
      }

      def sql = Sql.newInstance(getUrl(), getUsername(), getPassword(), getDriver())

      UtplsqlRunner runner = new UtplsqlRunner(getOutputDir(), logger, sql)

      def failedTests = false
      def totalTests = 0

      getPackages().each {
        new File("${runner.outputDir}/TEST-${it}.xml").write(runner.runPackage(it, getTestMethod(), getSetupMethod()))
        totalTests += runner.results.getTestsRun()

        logger.info "[INFO] Package: $it"
        logger.info "[INFO] Tests Run: ${runner.results.getTestsRun()}"
        logger.info "[INFO] Failures: ${runner.results.getTestFailures()}"
        logger.info "[INFO] Errors: ${runner.results.getTestErrors()}"

        if (runner.results.getTestFailures() || runner.results.getTestErrors()) {
          failedTests = true
        }
      }

      sql.close()

      if (totalTests == 0 && project.extensions.utplsql.failOnNoTests) {
        throw new GradleException("No tests were run.")
      }

      if (failedTests) {
        throw new GradleException("Failing unit tests.\nTests: ${runner.testsRun} \nFailures: ${runner.testsFailed} \nErrors: ${runner.testsErrors}")
      }
    }
    catch (ClassNotFoundException e) {
      throw new GradleException("JDBC Driver class not found.", e)
    }
    catch (SQLException e) {
      throw new GradleException("Error communicating with the database.", e)
    }
  }
}
