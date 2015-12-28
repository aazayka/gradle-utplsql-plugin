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

/**
 * Created by Iain Adams on 29/09/2014.
 */
class ReportGenerator {
  /**
   * Generates a XML junit style report for tests with the given runId in the database.
   *
   * @param sql
   * @param runId
   *
   * @return PackageTestResults The test results for the package.
   */
  PackageTestResults generateReport(Sql sql, def runId) {

    def results = new PackageTestResults()
    def rows = sql.rows("select status, description from utr_outcome where run_id = ${Sql.expand(runId)} order by outcome_id DESC")
    rows.each {row ->
      def desc = ResultParser.ParseDescription(row.description)
      if (row.status == "FAILURE") {
        desc.failure = true
      }
      results.descriptions.add(desc)
    }

    return results
  }

  /**
   * Generates an XML junit style error report for a package that is invalid.
   *
   * @return PackageTestResults The test results for the package.
   */
  PackageTestResults generateErrorReport() {
    def results = new PackageTestResults()
    TestDescription desc = new TestDescription()

    desc.procedureName = ''
    desc.testName = ''
    desc.error = true
    desc.results = 'Warning: Object altered with compilation errors.'
    desc.type = 'INVALID'
    results.descriptions.add(desc)

    return results
  }
}
