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
    ReportGenerator reportGen
    Sql sql
    PackageTestResults results
    UtplsqlDAO dao

    UtplsqlRunner(File outputDir, org.slf4j.Logger logger, Sql conn)
    {
        outputDir.mkdirs()
        this.outputDir = outputDir
        this.logger = logger
        this.sql = conn
        this.dao = new UtplsqlDAO(sql)
        this.reportGen = new ReportGenerator()
    }

    /**
     * Run the utPLSQL tests in a single package. This method calls the relevant utPLSQL schema stored procedure and obtains the results, exporting
     * them in a Maven Surefire report.
     *
     * @param packageName
     * @param testMethod
     * @param setupMethod
     * @return
     * @throws SQLException
     */
    def runPackage(String packageName, String testMethod, boolean setupMethod) throws UtplsqlRunnerException
    {
        try {
            def packageStatus = dao.getPackageStatus(packageName)
            switch(packageStatus){
                case 'INVALID':
                    results = reportGen.generateErrorReport()
                    return results.toXML(packageName, 0)

                case 'VALID':
                    def start = new Date()

                    def runId = dao.runUtplsqlProcedure(packageName,testMethod,setupMethod)
                    logger.info "[INFO] RunID: $runId"

                    def stop = new Date()
                    TimeDuration td = TimeCategory.minus(stop, start)

                    results = reportGen.generateReport(sql, runId)

                    return results.toXML(packageName, "${td.seconds}.${td.millis}".toFloat())

                case 'NO PACKAGE FOUND':
                    results = new PackageTestResults()
                    return results.toXML(packageName, 0)
            }
        }
        catch (SQLException e) {
            throw new UtplsqlRunnerException("Database communication error.", e)
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
        //TODO add support for running test suites.
    }
}
