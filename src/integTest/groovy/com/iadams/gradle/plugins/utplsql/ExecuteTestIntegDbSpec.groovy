package com.iadams.gradle.plugins.utplsql

import nebula.test.IntegrationSpec
import nebula.test.functional.ExecutionResult
import spock.lang.Ignore

import groovy.sql.Sql

/**
 * Created by Iain Adams
 */
class ExecuteTestIntegDbSpec extends IntegrationSpec {

    //TODO Actually write some tests using a DB!

    def setup() {
        directory('src/main/plsql')
        copyResources('src/main/plsql','src/main/plsql')

        directory('src/test/plsql')
        copyResources('src/test/plsql','src/test/plsql')
    }

    @Ignore
    def "use the rule to execute the tests"() {
        setup:
            useToolingApi = false
            buildFile << '''
                        apply plugin: 'com.iadams.utplsql'

                        utplsql {
                            url = "jdbc:oracle:thin:@localhost:1521:test"
                            username = "testing"
                            password = "testing"
                            testMethod = "test"
                        }
                        '''.stripIndent()

        when:
            runTasksSuccessfully('executeTestBetwnstr')

        then:
            fileExists("${project.extensions.utplsql.outputDir}/TEST-Betwnstr.xml")
    }
}
