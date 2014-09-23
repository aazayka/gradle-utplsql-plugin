package com.iadams.gradle.plugins.utplsql

import groovy.sql.*

import java.sql.SQLException
import groovy.time.TimeDuration
import groovy.time.TimeCategory

/**
 * Class to run utplsql tests
 *
 * Created by Iain Adams on 09/09/2014.
 */
class UtplsqlRunner {

    File outputDir

    UtplsqlRunner(File outputDir)
    {
        outputDir.mkdirs()
        this.outputDir = outputDir
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
    PackageTestResults runPackage(Sql sql, String packageName, String testMethod, String setupMethod) throws SQLException, IOException
    {
        PackageTestResults testResults = new PackageTestResults()
        println "Package: $packageName"
        //def stmt = buildPackageStatment(packageName, testMethod, setupMethod)

        try {
            def start = new Date()

            def runId

            def setup = testMethod.equals('test') ? ' recompile_in => FALSE,' : ''              //; $Sql.VARCHAR := utplsql2.runnum
            //sql.call("{call utplsql.${Sql.expand(testMethod)}('${Sql.expand(packageName)}', ${Sql.expand(setup)} per_method_setup_in => ${Sql.expand(setupMethod)}); $Sql.VARCHAR := utplsql2.runnum}") { number->
            sql.call("{call utplsql.test('${Sql.expand(packageName)}', recompile_in => FALSE, per_method_setup_in => FALSE); $Sql.VARCHAR := utplsql2.runnum}") { number->
                runId = number
                println "runnum: $number"
            }

            def stop = new Date()
            TimeDuration td = TimeCategory.minus( stop, start )

            def reportGen = new ReportGenerator()
            new File("$outputDir/TEST-${packageName}.xml").write(reportGen.generateReport(sql, runId, packageName, "${td.seconds}.${td.millis}".toFloat()))


        }
        catch(e) {
            println e.message
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
    /**
     * Generate SQL to run UTPLSQL package
     *
     * @param pkg
     * @param testMethod
     * @param setupMethod
     * @return
     */
    public def buildPackageStatment(def pkg, def testMethod, def setupMethod) {
        def setup = testMethod.equals('test') ? " recompile_in => FALSE," : ""
        //"{call utplsql.${testMethod}(\"${pkg}\",${setup} per_method_setup_in => ${setupMethod}); $Sql.VARCHAR := utplsql2.runnum}"
        return GString.EMPTY + "{call utplsql." + testMethod + '(\'' + pkg + '\',' + setup + ' per_method_setup_in => ' + setupMethod + ')}'
    }
}
