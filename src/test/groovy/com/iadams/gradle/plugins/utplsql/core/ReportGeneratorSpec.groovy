package com.iadams.gradle.plugins.utplsql.core

import groovy.sql.Sql
import org.custommonkey.xmlunit.Diff
import spock.lang.Specification

/**
 * Created by Iain Adams on 29/09/2014.
 */
class ReportGeneratorSpec extends Specification {

    def generator
    Sql sql = Mock(Sql)

    def setup() {
        generator = new ReportGenerator()
    }

    def "generate a successful report"() {
        given:
            def runId = 1
            sql.rows(_) >> [[status:'SUCCESS', description:"BETWNSTR.UT_BETWNSTR: ISNULL \"null end\" Expected \"\" and got \"\""]]

        when:
            def result = generator.generateReport(sql, runId).toXML('UT_BETWNSTR', '0.123')

        then:
            new Diff( TestXmlFixtures.SINGLE_BETWNSTR_XML_RESULTS, result).similar()
    }

    def "generate a failed report"() {
        given:
        def runId = 1
        sql.rows(_) >> [[status:'FAILURE', description:"BETWNSTR.UT_BETWNSTR: EQ \"normal\" Expected \"cde\" and got \"dde\""]]

        when:
        def result = generator.generateReport(sql, runId).toXML('UT_BETWNSTR', '0.123')

        then:
        new Diff( TestXmlFixtures.SINGLE_FAILURE_XML_RESULTS, result).similar()
    }

    def "generate a error report"() {
        when:
        def result = generator.generateErrorReport().toXML('UT_CHEESE',0.145)

        then:
        new Diff( TestXmlFixtures.SINGLE_ERROR_XML_RESULTS, result).similar()
    }
}
