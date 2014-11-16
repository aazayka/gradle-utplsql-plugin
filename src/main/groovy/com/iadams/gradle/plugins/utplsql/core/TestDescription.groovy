package com.iadams.gradle.plugins.utplsql.core

import groovy.xml.MarkupBuilder

/**
 * Created by Iain Adams on 23/09/2014.
 *
 * Class to contain the parsed description from the UTR_OUTCOME table.
 */
class TestDescription {
    def procedureName
    def testName
    def type
    def results
    def duration = 0
    def failure = false

    def toXML(MarkupBuilder xml){
        xml.testcase(name: testName, classname: procedureName, time: '0' ){
            if(failure){ xml.failure(type: type, results) }
        }
    }
}
