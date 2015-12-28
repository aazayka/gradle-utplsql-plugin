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

import com.iadams.gradle.plugins.utplsql.tasks.RunTestsTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

/**
 * Created by Iain Adams on 20/09/2014.
 */
class UtplsqlPluginSpec extends Specification {

  static final String PLUGIN_ID = 'com.iadams.utplsql'
  Project project

  //  @Rule
  //  TemporaryFolder projectDir

  def setup() {
    project = ProjectBuilder.builder().build()
    project.pluginManager.apply PLUGIN_ID
  }

  def "apply creates task of type RunTestsTask"() {
    setup:
    Task task = project.tasks.findByName(UtplsqlPlugin.UTPLSQL_RUN_TESTS_TASK)

    expect:
    task != null
    task instanceof RunTestsTask
    task.description == 'Executes all utPLSQL tests.'
  }

  def "check base plugin applies clean"() {
    expect:
    project.tasks.findByName('clean')
  }

  def "apply creates utplsql extension"() {
    expect:
    project.extensions.findByName(UtplsqlPlugin.UTPLSQL_EXTENSION)
  }

  def "check default task uses the utplsql extension"() {
    when:
    project.utplsql {
      driver = 'org.hsqldb.jdbcDriver'
      url = 'jdbc:oracle:thin:@localhost:1521:test'
      password = 'testing'
      username = 'testing'
      testMethod = 'test'
      setupMethod = false
      outputDir = 'build/other'
      failOnNoTests = true
    }

    then:
    Task task = project.tasks.findByName(UtplsqlPlugin.UTPLSQL_RUN_TESTS_TASK)
    task.driver == 'org.hsqldb.jdbcDriver'
    task.url == 'jdbc:oracle:thin:@localhost:1521:test'
    task.password == 'testing'
    task.username == 'testing'
    task.testMethod == 'test'
    task.setupMethod == false
    task.outputDir == project.file('build/other')
    task.failOnNoTests == true
  }
}
