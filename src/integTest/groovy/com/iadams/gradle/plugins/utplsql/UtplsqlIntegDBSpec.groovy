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
package com.iadams.gradle.plugins.utplsql

import com.iadams.gradle.plugins.utplsql.utils.TestKitBaseIntegSpec
import org.gradle.testkit.runner.GradleRunner

import static org.gradle.testkit.runner.TaskOutcome.FAILED
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class UtplsqlIntegDBSpec extends TestKitBaseIntegSpec {

  def setup() {
    directory('src/test/plsql')
    buildFile << '''
                    plugins {
                      id 'com.iadams.utplsql'
                    }

                    repositories {
                        mavenLocal()
                        mavenCentral()
                    }
                    dependencies {
                        driver "com.oracle:ojdbc6:11.2.0.1.0"
                        junitreport 'org.apache.ant:ant-junit:1.9.4'
                    }

                    utplsql {
                        url = "jdbc:oracle:thin:@localhost:1521:test"
                        username = "testing"
                        password = "testing"
                    }
                    '''.stripIndent()
  }

  def "run utplsql with passing tests"() {
    setup:
    copyResources('src/test/plsql', 'src/test/plsql')

    when:
    def result = GradleRunner.create()
      .withProjectDir(testProjectDir.root)
      .withArguments(UtplsqlPlugin.UTPLSQL_ALL_TASK)
      .withPluginClasspath(pluginClasspath)
      .build()

    then:
    result.task(':utDeploy').outcome == SUCCESS
    result.task(':utRun').outcome == SUCCESS
    result.task(':utReport').outcome == SUCCESS
    result.task(':utplsql').outcome == SUCCESS
    fileExists("build/utplsql/TEST-ut_betwnstr.xml")
    fileExists("build/reports/utplsql/0_ut_betwnstr.html")
  }

  def "run utplsql with failing tests"() {
    setup:
    copyResources('src/failing/plsql', 'src/test/plsql')

    when:
    def result = GradleRunner.create()
      .withProjectDir(testProjectDir.root)
      .withArguments(UtplsqlPlugin.UTPLSQL_ALL_TASK)
      .withPluginClasspath(pluginClasspath)
      .buildAndFail()

    then:
    result.task(':utDeploy').outcome == SUCCESS
    result.task(':utRun').outcome == FAILED
    result.task(':utReport').outcome == SUCCESS
    fileExists("build/utplsql/TEST-ut_failing.xml")
    fileExists("build/reports/utplsql/0_ut_failing.html")

    result.output.contains('Failures: 1')
  }

  def "run utplsql with erroring tests"() {
    setup:
    copyResources('src/broken/plsql', 'src/test/plsql')
    copyResources('src/test/plsql', 'src/test/plsql')

    when:
    def result = GradleRunner.create()
      .withProjectDir(testProjectDir.root)
      .withArguments(UtplsqlPlugin.UTPLSQL_ALL_TASK)
      .withPluginClasspath(pluginClasspath)
      .buildAndFail()

    then:
    result.task(':utDeploy').outcome == SUCCESS
    result.task(':utRun').outcome == FAILED
    result.task(':utReport').outcome == SUCCESS
    fileExists("build/utplsql/TEST-ut_broken.xml")
    fileExists("build/reports/utplsql/1_ut_broken-errors.html")

    result.output.contains('Errors: 1')
  }

  def "run utplsql with elevated logging"() {
    setup:
    copyResources('src/test/plsql', 'src/test/plsql')

    when:
    def result = GradleRunner.create()
      .withProjectDir(testProjectDir.root)
      .withArguments(UtplsqlPlugin.UTPLSQL_ALL_TASK, '--i')
      .withPluginClasspath(pluginClasspath)
      .build()

    then:
    result.task(':utDeploy').outcome == SUCCESS
    result.task(':utRun').outcome == SUCCESS
    result.task(':utReport').outcome == SUCCESS
    result.task(':utplsql').outcome == SUCCESS
    result.output.contains('[INFO] Tests Run: 4')
    result.output.contains('[INFO] Failures: 0')
    result.output.contains('[INFO] Errors: 0')
  }
}
