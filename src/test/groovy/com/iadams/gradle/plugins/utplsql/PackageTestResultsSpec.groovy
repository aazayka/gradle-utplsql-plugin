package com.iadams.gradle.plugins.utplsql

import spock.lang.Specification
import groovy.xml.MarkupBuilder
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit

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

    def "Get total tests with no results"(){
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
            new Diff( TestXmlFixtures.SINGLE_BETWNSTR_XML_RESULTS, result).similar()
    }
}
