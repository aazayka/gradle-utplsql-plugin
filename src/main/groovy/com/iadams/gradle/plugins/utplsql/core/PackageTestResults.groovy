package com.iadams.gradle.plugins.utplsql.core

import groovy.xml.MarkupBuilder

/**
 * Created by Iain Adams on 10/09/2014.
 */
class PackageTestResults {

    def descriptions = []
    def getTestsRun() { descriptions.size() }
    def getTestFailures() { descriptions.count { it.failure } }

    String toXML(String name, def duration){
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.setDoubleQuotes(true)

        xml.testsuite(name: name, tests: getTestsRun(), failures: getTestFailures(), skipped: '0', errors: '0', time: duration){
            descriptions.each{ it.toXML(xml) }
        }

        writer.toString()
    }
}
