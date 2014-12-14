package com.iadams.gradle.plugins.utplsql.core

import groovy.sql.Sql
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Ignore
import spock.lang.Specification

import java.sql.SQLException

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

    def setup(){
        Logger slf4jLogger = LoggerFactory.getLogger('logger')
        runner = new UtplsqlRunner(mockfile, slf4jLogger, sql)
        runner.reportGen = reportGen
        runner.dao = dao
    }

    def "test a package"(){
        given:
        dao.getPackageStatus(_) >> 'VALID'
        dao.runUtplsqlProcedure(_,_,_) >> 1
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

    def "run package's tests"(){
        given:
        dao.getPackageStatus(_) >> 'VALID'
        dao.runUtplsqlProcedure(_,_,_) >> 1
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

    def "test a package with a broken sql connection"(){
        given:
        dao.getPackageStatus(_) >> 'VALID'
        dao.runUtplsqlProcedure(_,_,_) >> { throw new SQLException("Unable to connect to the DB!") }

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

        expect:
        runner.runPackage('ut_betwnstr', 'test', true) == '<pretend error xml>'
    }
}
