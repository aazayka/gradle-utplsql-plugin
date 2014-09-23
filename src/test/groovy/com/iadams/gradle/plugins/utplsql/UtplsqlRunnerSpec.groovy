package com.iadams.gradle.plugins.utplsql

import groovy.sql.Sql
import spock.lang.Specification

/**
 * Created by Iain Adams on 13/09/2014.
 */
class UtplsqlRunnerSpec extends Specification {

    Sql sql = Mock(Sql)

    def "run package test"(){
        /*given: "A new test runner object"
            def runner = new UtplsqlRunner()

        when:
            //def results = runner.runPackage()

        then:
            //sql.call(_) >> "1"*/
    }
}
