package com.iadams.gradle.plugins.utplsql.core

import groovy.sql.*
import java.sql.SQLException
import groovy.time.TimeDuration
import groovy.time.TimeCategory

import java.util.logging.Logger

/**
 * Class to run utplsql tests
 *
 * Created by Iain Adams on 09/09/2014.
 */
class UtplsqlRunner {

    File outputDir
    org.slf4j.Logger logger

    UtplsqlRunner(File outputDir, org.slf4j.Logger logger)
    {
        outputDir.mkdirs()
        this.outputDir = outputDir
        this.logger = logger
    }

    /**
     * Run the utPLSQL tests in a single package. This method calls the relevant utPLSQL schema stored procedure and obtains the results, exporting
     * them in a Maven Surefire report.
     *
     * @param sql
     * @param packageName
     * @param testMethod
     * @param setupMethod
     * @return
     * @throws SQLException
     * @throws IOException
     */
    def runPackage(Sql sql, String packageName, String testMethod, boolean setupMethod, ReportGenerator reportGen) throws SQLException, IOException
    {
        try {
            def start = new Date()

            def setup = testMethod.equals('test') ? ' recompile_in => FALSE,' : ''              //; $Sql.VARCHAR := utplsql2.runnum
            //sql.call("{call utplsql.${Sql.expand(testMethod)}('${Sql.expand(packageName)}', ${Sql.expand(setup)} per_method_setup_in => ${Sql.expand(setupMethod)}); $Sql.VARCHAR := utplsql2.runnum}") { number->
            /*sql.call("{call utplsql.test('${Sql.expand(packageName)}', recompile_in => FALSE, per_method_setup_in => FALSE); $Sql.VARCHAR := utplsql2.runnum}") { number->
                runId = number
                println "runnum: $number"
            }*/
            //def runId = sql.call("{call utplsql.test('${Sql.expand(packageName)}', recompile_in => FALSE, per_method_setup_in => FALSE); $Sql.VARCHAR := utplsql2.runnum}")
            def runId = sql.call("{call utplsql.${Sql.expand(testMethod)}('${Sql.expand(packageName)}', ${Sql.expand(setup)} per_method_setup_in => ${Sql.expand(setupMethod.toString())}); $Sql.VARCHAR := utplsql2.runnum}")

            def stop = new Date()
            TimeDuration td = TimeCategory.minus( stop, start )

            return reportGen.generateReport(sql, runId, packageName, "${td.seconds}.${td.millis}".toFloat())
        }
        catch (SQLException e) {
            logger.error e.message
            return false
        }
    }

    /**
     * Run the utPLSQL tests in a single package. This method calls the relevant utPLSQL schema stored procedure and obtains the results, exporting
     * them in a Maven Surefire report.
     *
     * @param sql
     * @param testSuiteName
     * @param testMethod
     * @param setupMethod
     * @return
     * @throws SQLException
     * @throws IOException
     */
    PackageTestResults runTestSuite(Sql sql, String testSuiteName, String testMethod, String setupMethod) throws SQLException, IOException
    {

    }
}
