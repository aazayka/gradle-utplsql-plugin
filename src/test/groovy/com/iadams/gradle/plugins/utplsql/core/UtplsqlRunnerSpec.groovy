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
package com.iadams.gradle.plugins.utplsql.core

import groovy.sql.Sql
import java.sql.SQLException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

/**
 * Created by Iain Adams on 13/09/2014.
 */
class UtplsqlRunnerSpec extends Specification {

  def runner
  File mockfile = Mock(File)
  Sql sql = Mock(Sql)
  ReportGenerator reportGen = Mock(ReportGenerator)
  PackageTestResults pkgResults = Mock(PackageTestResults)
  UtplsqlDAO dao = Mock(UtplsqlDAO)

  def setup() {
    Logger slf4jLogger = LoggerFactory.getLogger('logger')
    runner = new UtplsqlRunner(mockfile, slf4jLogger, sql)
    runner.reportGen = reportGen
    runner.dao = dao
  }

  def "test a package"() {
    given:
    dao.getPackageStatus(_) >> 'VALID'
    dao.runUtplsqlProcedure(_, _, _) >> 1
    reportGen.generateReport(_, _) >> pkgResults
    pkgResults.toXML(_, _) >> '<pretend xml>'
    pkgResults.testsRun >> 4
    pkgResults.testErrors >> 0
    pkgResults.testFailures >> 0

    when:
    def result = runner.runPackage('betwnstr', 'test', true)

    then:
    result == "<pretend xml>"
  }

  def "run package's tests"() {
    given:
    dao.getPackageStatus(_) >> 'VALID'
    dao.runUtplsqlProcedure(_, _, _) >> 1
    reportGen.generateReport(_, _) >> pkgResults
    pkgResults.toXML(_, _) >> '<pretend xml>'
    pkgResults.testsRun >> 4
    pkgResults.testErrors >> 0
    pkgResults.testFailures >> 0

    when:
    def result = runner.runPackage('betwnstr', 'run', false)

    then:
    result == "<pretend xml>"
  }

  def "test a package with a broken sql connection"() {
    given:
    dao.getPackageStatus(_) >> 'VALID'
    dao.runUtplsqlProcedure(_, _, _) >> {throw new SQLException("Unable to connect to the DB!")}

    when:
    runner.runPackage('betwnstr', 'test', true)

    then:
    thrown(UtplsqlRunnerException)
  }

  def "generate a error report"() {
    given:
    dao.getPackageStatus(_) >> 'INVALID'
    reportGen.generateErrorReport() >> pkgResults
    pkgResults.toXML(_, _) >> '<pretend error xml>'
    pkgResults.testsRun >> 4
    pkgResults.testErrors >> 0
    pkgResults.testFailures >> 0

    expect:
    runner.runPackage('ut_betwnstr', 'test', true) == '<pretend error xml>'
  }
}
