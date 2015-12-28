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

import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import spock.lang.Specification

/**
 * Created by Iain Adams on 13/09/2014.
 */
class PackageTestResultsSpec extends Specification {

  def desc1, desc2, desc3

  def setup() {
    desc1 = new TestDescription()
    desc1.procedureName = "BETWNSTR.UT_BETWNSTR"
    desc1.testName = "null end"
    desc1.type = "ISNULL"
    desc1.results = "Expected \"\" and got \"\""

    desc2 = new TestDescription()
    desc2.failure = true

    desc3 = new TestDescription()
    desc3.failure = true
  }

  def "Get total tests with no results"() {
    given:
    def results = new PackageTestResults()

    expect:
    results.getTestsRun() == 0
  }

  def "Get total tests in package"() {
    given:
    def results = new PackageTestResults()

    when:
    results.descriptions.add(desc1)
    results.descriptions.add(desc2)

    then:
    results.getTestsRun() == 2
  }

  def "Get total test failures in package"() {
    given:
    def results = new PackageTestResults()

    when:
    results.descriptions.add(desc1)
    results.descriptions.add(desc2)
    results.descriptions.add(desc3)

    then:
    results.getTestFailures() == 2
  }

  def "successful test toXML()"() {
    given:
    def results = new PackageTestResults()

    when:
    results.descriptions.add(desc1)
    def result = results.toXML('UT_BETWNSTR', 0.123)
    XMLUnit.setIgnoreWhitespace(true)

    then:
    new Diff(TestXmlFixtures.SINGLE_BETWNSTR_XML_RESULTS, result).similar()
  }
}
