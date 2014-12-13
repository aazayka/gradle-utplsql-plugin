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
     *  @return PackageTestResults The test results for the package.
     */
    PackageTestResults generateReport(Sql sql, def runId) {

        def results = new PackageTestResults()
        def rows = sql.rows("select status, description from utr_outcome where run_id = ${Sql.expand(runId)} order by outcome_id DESC")
        rows.each{ row->
            def desc = ResultParser.ParseDescription(row.description)
            if(row.status == "FAILURE") { desc.failure = true }
            results.descriptions.add(desc)
        }

        return results
    }

    /**
     * Generates an XML junit style error report for a package that is invalid.
     *
     *  @return PackageTestResults The test results for the package.
     */
    PackageTestResults generateErrorReport(){
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
