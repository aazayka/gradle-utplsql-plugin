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

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class GenerateTestIntegSpec extends TestKitBaseIntegSpec {

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

  def "Generate test file from the rule"() {
    when:
    def result = GradleRunner.create()
      .withProjectDir(testProjectDir.root)
      .withArguments('utGen-package')
      .withPluginClasspath(pluginClasspath)
      .build()

    then:
    result.task(':utGen-package').outcome == SUCCESS
    fileExists("src/test/plsql/ut_package.pks")
    fileExists("src/test/plsql/ut_package.pkb")
  }
}
