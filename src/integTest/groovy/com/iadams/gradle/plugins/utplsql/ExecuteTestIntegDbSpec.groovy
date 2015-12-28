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

class ExecuteTestIntegDbSpec extends TestKitBaseIntegSpec {

  def setup() {
    directory('src/test/plsql')
    copyResources('src/test/plsql', 'src/test/plsql')
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

  def "use the rule to execute a test"() {
    when:
    GradleRunner.create()
      .withProjectDir(testProjectDir.root)
      .withArguments('utRun-ut_betwnstr')
      .withPluginClasspath(pluginClasspath)
      .build()

    then:
    fileExists("build/utplsql/TEST-ut_betwnstr.xml")
  }

  def "use the rule to test an object"() {
    when:
    GradleRunner.create()
      .withProjectDir(testProjectDir.root)
      .withArguments('utTest-betwnstr')
      .withPluginClasspath(pluginClasspath)
      .build()

    then:
    fileExists("build/utplsql/TEST-betwnstr.xml")
  }

  def "rule fails when executing a test that doesn't exist"() {
    when:
    def result = GradleRunner.create()
      .withProjectDir(testProjectDir.root)
      .withArguments('utRun-ut_cheese')
      .withPluginClasspath(pluginClasspath)
      .buildAndFail()

    then:
    result.output.contains('No tests were run.')
  }

  def "execute all the tests in the sourceDir"() {
    setup:
    directory('src/test/other')
    copyResources('src/test/plsql', 'src/test/other')
    buildFile << '''
      utplsql {
          sourceDir = "${projectDir}/src/test/other"
      }
      '''.stripIndent()

    when:
    GradleRunner.create()
      .withProjectDir(testProjectDir.root)
      .withArguments(UtplsqlPlugin.UTPLSQL_RUN_TESTS_TASK)
      .withPluginClasspath(pluginClasspath)
      .build()

    then:
    fileExists("build/utplsql/TEST-ut_betwnstr.xml")
    fileExists("build/utplsql/TEST-ut_simple_example.xml")
  }
}
