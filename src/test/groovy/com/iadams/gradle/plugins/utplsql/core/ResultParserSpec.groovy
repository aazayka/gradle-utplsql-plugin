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

import spock.lang.Specification

/**
 * Created by Iain Adams on 13/09/2014.
 */
class ResultParserSpec extends Specification {

  def "Parse an ISNULL test"() {
    when: "we parse the example description"
    TestDescription results = ResultParser.ParseDescription("BETWNSTR.UT_BETWNSTR: ISNULL \"null end\" Expected \"\" and got \"\"")

    then:
    results.procedureName == "BETWNSTR.UT_BETWNSTR"
    results.testName == "null end"
    results.type == "ISNULL"
    results.results == "Expected \"\" and got \"\""
    results.duration == 0
  }

  def "Parse an EQQUERYVALUE test"() {
    when:
    TestDescription results = ResultParser.
      ParseDescription("MYBOOKS_PKG.UT_1_INS: EQQUERYVALUE \"ins-2\" Result: Query \"select count(*) from mybooks where book_id=100\" returned value \"1\" that does match \"1\"")

    then:
    results.procedureName == "MYBOOKS_PKG.UT_1_INS"
    results.testName == "ins-2"
    results.type == "EQQUERYVALUE"
    results.results == "Query \"select count(*) from mybooks where book_id=100\" returned value \"1\" that does match \"1\""
    results.duration == 0
  }

  def "Parse a THROWS test"() {
    when:
    TestDescription results = ResultParser.
      ParseDescription("MYBOOKS_PKG.UT_1_INS: THROWS \"ins-5\" Result: Block \"mybooks_pkg.ins(100,'Something',sysdate)\" raises  Exception \"-1")

    then:
    results.procedureName == "MYBOOKS_PKG.UT_1_INS"
    results.testName == "ins-5"
    results.type == "THROWS"
    results.results == "Block \"mybooks_pkg.ins(100,'Something',sysdate)\" raises  Exception \"-1"
    results.duration == 0
  }

  def "Parse a Unable to run description"() {
    when: "we parse the example description"
    TestDescription results = ResultParser.
      ParseDescription("MYBOOKS_PKG.UT_4_SEL_BOOKNM: Unable to run \"TESTING\".ut_MYBOOKS_PKG.UT_4_SEL_BOOKNM: ORA-01403: no data found\nORA-01403: no data found")

    then:
    results.procedureName == "MYBOOKS_PKG.UT_4_SEL_BOOKNM"
    results.testName == "TESTING\".ut_MYBOOKS_PKG.UT_4_SEL_BOOKNM"
    results.type == "Unable to run"
    results.results == "ORA-01403 no data found\nORA-01403 no data found"
    results.duration == 0
  }

  def "check known assert extractor"() {
    given:
    def results = new TestDescription()

    when: "we parse the example description"
    def output = ResultParser.extractAssertType(results, "noQuotes")

    then: "We get the expected output"
    results.type == "Assert Type Unknown"
    output == "noQuotes"
  }

  def "get results with non-null string"() {
    when:
    def description = "something : else".split(':')
    def output = ResultParser.getResults("Result!", description, 0)

    then:
    output == "Result!"
  }

  def "get results with null string"() {
    when:
    def description = "something : else".split(':')
    def output = ResultParser.getResults(null, description, 0)

    then:
    output == "something  else"
  }

  def "check exceptions is thrown"() {
    when:
    ResultParser.ParseDescription("broken")

    then:
    def e = thrown(ResultParserException)
    e.message == "Unable to parse utPLSQL results, check the outcome table."
  }
}
