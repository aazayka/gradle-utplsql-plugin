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

class HtmlReportIntegSpec extends TestKitBaseIntegSpec {

  def setup() {
    buildFile << '''
      plugins {
        id 'com.iadams.utplsql'
      }

      repositories {
        mavenCentral()
      }

      dependencies {
        junitreport 'org.apache.ant:ant-junit:1.9.4'
      }'''.stripIndent()
  }

  def "Generate a HTML successful report from single XML files"() {
    setup:
    copyResources('output/passed.xml', 'build/utplsql/TEST-UT_BETWNSTR.xml')

    when:
    GradleRunner.create()
      .withProjectDir(testProjectDir.root)
      .withArguments(UtplsqlPlugin.UTPLSQL_TEST_REPORTS_TASK)
      .withPluginClasspath(pluginClasspath)
      .build()

    then:
    fileExists('build/reports/utplsql/0_UT_BETWNSTR.html')
  }

  def "Generate a HTML report with failed tests from XML files"() {
    setup:
    copyResources('output/failure.xml', 'build/utplsql/TEST-UT_BETWNSTR.xml')


    when:
    GradleRunner.create()
      .withProjectDir(testProjectDir.root)
      .withArguments(UtplsqlPlugin.UTPLSQL_TEST_REPORTS_TASK)
      .withPluginClasspath(pluginClasspath)
      .build()

    then:
    fileExists('build/reports/utplsql/0_UT_BETWNSTR.html')
  }

  def "Generate a HTML report with errors from XML files"() {
    setup:
    copyResources('output/error.xml', 'build/utplsql/TEST-UT_CHEESE.xml')

    when:
    GradleRunner.create()
      .withProjectDir(testProjectDir.root)
      .withArguments(UtplsqlPlugin.UTPLSQL_TEST_REPORTS_TASK)
      .withPluginClasspath(pluginClasspath)
      .build()

    then:
    fileExists('build/reports/utplsql/0_UT_CHEESE.html')
  }
}
