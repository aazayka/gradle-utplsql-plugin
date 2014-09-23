package com.iadams.gradle.plugins.utplsql

import groovy.sql.Sql
import groovy.xml.MarkupBuilder

/**
 * Created by Iain Adams on 29/09/2014.
 */
class ReportGenerator {
    File reportDir

    /**
     *
     * @param sql
     * @param runId
     * @param name
     * @param duration
     * @return
     */
    def generateReport(Sql sql, def runId, String name, def duration) {

        def results = new PackageTestResults()
        sql.eachRow("select status, description from utr_outcome where run_id = ${Sql.expand(runId)} order by outcome_id DESC") { row->
            def desc = ResultParser.ParseDescription(row.description)
            if(row.status == "FAILURE") { desc.failure = true }
            results.descriptions.add(desc)
        }

        return results.toXML(name, duration)
    }
}
