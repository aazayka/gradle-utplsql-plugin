package com.iadams.gradle.plugins.utplsql

import spock.lang.Specification
import groovy.xml.MarkupBuilder
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit

/**
 * Created by Iain Adams on 11/10/2014.
 */
class TestDescriptionSpec extends Specification {
    def desc1, desc2

    def setup() {
        desc1 = new TestDescription()
        desc1.procedureName = "BETWNSTR.UT_BETWNSTR"
        desc1.testName = "null end"
        desc1.type = "ISNULL"
        desc1.results = "Expected \"\" and got \"\""

        desc2 = new TestDescription()
        desc2.procedureName = "BETWNSTR.UT_BETWNSTR"
        desc2.testName = "normal"
        desc2.type = "EQ"
        desc2.results = "Expected \"cde\" and got \"dde\""
        desc2.failure = true
    }

    def "failed test to XML"() {
        given:
            def writer = new StringWriter()
            def xml = new MarkupBuilder(writer)

        when:
            desc2.toXML(xml)

            XMLUnit.setIgnoreWhitespace(true)

        then:
            new Diff( TestXmlFixtures.FAILURE_TEST_DESCRIPTION, writer.toString()).similar()
    }

    def "successful test to XML"() {
        given:
            def writer = new StringWriter()
            def xml = new MarkupBuilder(writer)

        when:
            def result = desc1.toXML(xml)

            XMLUnit.setIgnoreWhitespace(true)

        then:
            new Diff( TestXmlFixtures.SUCCESSFUL_TEST_DESCRIPTION, writer.toString()).similar()
    }
}
