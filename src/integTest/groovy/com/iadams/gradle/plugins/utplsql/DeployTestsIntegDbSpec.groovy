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

class DeployTestsIntegDbSpec extends TestKitBaseIntegSpec {

  def setup() {
    directory('src/test/plsql')
    copyResources('src/test/plsql', 'src/test/plsql')
    buildFile << '''
      plugins {
        id 'com.iadams.utplsql'
      }

      repositories {
        mavenLocal()
      }
      dependencies {
        driver "com.oracle:ojdbc6:11.2.0.1.0"
      }

      utplsql {
        url = "jdbc:oracle:thin:@localhost:1521:test"
        username = "testing"
        password = "testing"
      }
      '''.stripIndent()
  }

  def "use the rule to deploy a test"() {
    setup:
    directory('src/test/plsql')
    copyResources('src/test/plsql', 'src/test/plsql')

    when:
    def result = GradleRunner.create()
      .withProjectDir(testProjectDir.root)
      .withArguments('utDeploy-betwnstr', '--i')
      .withPluginClasspath(pluginClasspath)
      .build()

    then:
    result.output.contains('Deploying: ut_betwnstr.pks')
    result.output.contains('Deploying: ut_betwnstr.pkb')
  }

  def "use task to deploy all tests"() {
    when:
    def result = GradleRunner.create()
      .withProjectDir(testProjectDir.root)
      .withArguments(UtplsqlPlugin.UTPLSQL_DEPLOY_TESTS_TASK, '--i')
      .withPluginClasspath(pluginClasspath)
      .build()

    then:
    result.output.contains('Deploying: ut_betwnstr.pks')
    result.output.contains('Deploying: ut_betwnstr.pkb')
    result.output.contains('Deploying: ut_simple_example.pks')
    result.output.contains('Deploying: ut_simple_example.pkb')
  }

  def "use task to deploy all tests in non-standard folder"() {
    setup:
    directory('tests')
    copyResources('src/test/plsql', 'tests')
    buildFile.append('''
      utplsql {
        sourceDir = "${projectDir}/tests"
      }
      ''')

    when:
    def result = GradleRunner.create()
      .withProjectDir(testProjectDir.root)
      .withArguments(UtplsqlPlugin.UTPLSQL_DEPLOY_TESTS_TASK, '--i')
      .withPluginClasspath(pluginClasspath)
      .build()

    then:
    result.output.contains('Deploying: ut_betwnstr.pks')
    result.output.contains('Deploying: ut_betwnstr.pkb')
    result.output.contains('Deploying: ut_simple_example.pks')
    result.output.contains('Deploying: ut_simple_example.pkb')
  }

  def "deploy a package that doesnt compile results in failure"() {
    setup:
    directory('tests')
    copyResources('src/broken/plsql', 'tests')

    buildFile << '''
      utplsql {
        sourceDir = "${projectDir}/tests"
      }
      '''.stripIndent()

    when:
    def result = GradleRunner.create()
      .withProjectDir(testProjectDir.root)
      .withArguments(UtplsqlPlugin.UTPLSQL_DEPLOY_TESTS_TASK, '--i')
      .withPluginClasspath(pluginClasspath)
      .build()

    then:
    result.output.contains('Deploying: ut_broken.pks')
    result.output.contains('Deploying: ut_broken.pkb')
    result.output.contains('Package ut_broken failed to compile.')
  }
}
