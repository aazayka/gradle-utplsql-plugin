package com.iadams.gradle.plugins.utplsql

import nebula.test.IntegrationSpec
import nebula.test.functional.ExecutionResult

/**
 * Created by Iain Adams
 */
class ValidateExceptionsIntegDbSpec extends IntegrationSpec {

    def "validate jdbc driver error handling"() {
        setup:
        useToolingApi = false
        buildFile << '''
                        apply plugin: 'com.iadams.utplsql'

                        utplsql {
                            driver = 'org.some.dbDriver'
                        }
                        '''.stripIndent()

        when:
            ExecutionResult result = runTasksWithFailure('executeTestcheese')

        then:
            result.getFailure().cause.cause.message == "JDBC Driver class not found."
    }

    def "validate sql connection error handling"() {
        setup:
            useToolingApi = false
            buildFile << '''
                        apply plugin: 'com.iadams.utplsql'

                        utplsql {
                            url = 'no db here'
                            username = 'no'
                            password = 'chance'
                        }
                        '''.stripIndent()

        when:
            ExecutionResult result = runTasksWithFailure('executeTestcheese')

        then:
            result.getFailure().cause.cause.message == "Error communicating with the database."
    }
}
