package com.iadams.gradle.plugins.utplsql.core

import groovy.sql.Sql
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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

    def setup(){
        Logger slf4jLogger = LoggerFactory.getLogger('logger')
        runner = new UtplsqlRunner(mockfile, slf4jLogger)
        runner.reportGen = reportGen
        runner.sql = sql
    }

    def "test a package"(){
        given:
            sql.call(_,_) >> {1}
            reportGen.generateReport(_, _) >> pkgResults
            pkgResults.toXML(_, _) >> '<pretend xml>'

        when:
            def result = runner.runPackage('betwnstr', 'test', true)

        then:
            result == "<pretend xml>"
    }

    def "run package's tests"(){
        given:
        sql.call(_,_) >> {1}
        reportGen.generateReport(_, _) >> pkgResults
        pkgResults.toXML(_, _) >> '<pretend xml>'

        when:
        def result = runner.runPackage('betwnstr', 'run', false)

        then:
        result == "<pretend xml>"
    }

    def "test a package with a broken sql connection"(){
        given:
            sql.call(_,_) >> { throw new SQLException("Unable to connect to the DB!") }

        when:
            runner.runPackage('betwnstr', 'test', true)

        then:
            thrown(UtplsqlRunnerException)
    }
}
