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
import org.custommonkey.xmlunit.Diff
import spock.lang.Specification

/**
 * Created by Iain Adams on 29/09/2014.
 */
class ReportGeneratorSpec extends Specification {

  def generator
  Sql sql = Mock(Sql)

  def setup() {
    generator = new ReportGenerator()
  }

  def "generate a successful report"() {
    given:
    def runId = 1
    sql.rows(_) >> [[status: 'SUCCESS', description: "BETWNSTR.UT_BETWNSTR: ISNULL \"null end\" Expected \"\" and got \"\""]]

    when:
    def result = generator.generateReport(sql, runId).toXML('UT_BETWNSTR', '0.123')

    then:
    new Diff(TestXmlFixtures.SINGLE_BETWNSTR_XML_RESULTS, result).similar()
  }

  def "generate a failed report"() {
    given:
    def runId = 1
    sql.rows(_) >> [[status: 'FAILURE', description: "BETWNSTR.UT_BETWNSTR: EQ \"normal\" Expected \"cde\" and got \"dde\""]]

    when:
    def result = generator.generateReport(sql, runId).toXML('UT_BETWNSTR', '0.123')

    then:
    new Diff(TestXmlFixtures.SINGLE_FAILURE_XML_RESULTS, result).similar()
  }

  def "generate a error report"() {
    when:
    def result = generator.generateErrorReport().toXML('UT_CHEESE', 0.145)

    then:
    new Diff(TestXmlFixtures.SINGLE_ERROR_XML_RESULTS, result).similar()
  }
}
