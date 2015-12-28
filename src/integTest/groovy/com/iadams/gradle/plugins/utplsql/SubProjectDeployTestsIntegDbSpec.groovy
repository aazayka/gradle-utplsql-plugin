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

class SubProjectDeployTestsIntegDbSpec extends TestKitBaseIntegSpec {

  def "use the rule to deploy a test in a subproject"() {
    setup:
    addSubproject('schemaOne')
    addSubproject('schemaTwo')
    directory('schemaOne/src/test/plsql')
    copyResources('src/test/plsql/ut_betwnstr.pks', 'schemaOne/src/test/plsql/ut_betwnstr.pks')
    copyResources('src/test/plsql/ut_betwnstr.pkb', 'schemaOne/src/test/plsql/ut_betwnstr.pkb')
    copyResources('buildFiles/schemaOne.gradle', 'schemaOne/build.gradle')
    copyResources('buildFiles/schemaTwo.gradle', 'schemaTwo/build.gradle')

    when:
    def result = GradleRunner.create()
      .withProjectDir(testProjectDir.root)
      .withArguments('schemaOne:utDeploy-betwnstr', '--i')
      .withPluginClasspath(pluginClasspath)
      .build()

    then:
    result.output.contains('Deploying: ut_betwnstr.pks')
    result.output.contains('Deploying: ut_betwnstr.pkb')
  }

  def "deploy all tests across all schemas"() {
    setup:
    addSubproject('schemaOne')
    addSubproject('schemaTwo')
    copyResources('src/test/plsql/ut_betwnstr.pks', 'schemaOne/src/test/plsql/ut_betwnstr.pks')
    copyResources('src/test/plsql/ut_betwnstr.pkb', 'schemaOne/src/test/plsql/ut_betwnstr.pkb')
    copyResources('src/test/plsql/ut_simple_example.pks', 'schemaTwo/src/test/plsql/ut_simple_example.pks')
    copyResources('src/test/plsql/ut_simple_example.pkb', 'schemaTwo/src/test/plsql/ut_simple_example.pkb')
    copyResources('buildFiles/schemaOne.gradle', 'schemaOne/build.gradle')
    copyResources('buildFiles/schemaTwo.gradle', 'schemaTwo/build.gradle')

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

  def "deploy all tests across all schemas with custom sourceDir"() {
    setup:
    addSubproject('schemaOne')
    addSubproject('schemaTwo')
    directory('schemaOne/tests')
    directory('schemaTwo/tests')
    copyResources('src/test/plsql/ut_betwnstr.pks', 'schemaOne/tests/ut_betwnstr.pks')
    copyResources('src/test/plsql/ut_betwnstr.pkb', 'schemaOne/tests/ut_betwnstr.pkb')
    copyResources('src/test/plsql/ut_simple_example.pks', 'schemaTwo/tests/ut_simple_example.pks')
    copyResources('src/test/plsql/ut_simple_example.pkb', 'schemaTwo/tests/ut_simple_example.pkb')
    copyResources('buildFiles/non-default/schemaOne.gradle', 'schemaOne/build.gradle')
    copyResources('buildFiles/non-default/schemaTwo.gradle', 'schemaTwo/build.gradle')

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
}
