package com.iadams.gradle.plugins.utplsql

import nebula.test.IntegrationSpec
import nebula.test.functional.ExecutionResult

/**
 * Created by Iain Adams
 */
class ValidateExceptionsIntegDbSpec extends IntegrationSpec {

    def setup(){
        buildFile << '''apply plugin: 'com.iadams.utplsql'

                        repositories {
                            mavenCentral()
                        }

                        dependencies {
                            junitreport 'org.apache.ant:ant-junit:1.9.4\'
                        }
                        '''.stripIndent()
    }

    def "validate jdbc driver error handling with runTestsTask"() {
        setup:
        useToolingApi = false
        buildFile << '''
                        utplsql {
                            driver = 'org.some.dbDriver'
                        }'''.stripIndent()

        when:
            ExecutionResult result = runTasksWithFailure('utRun-cheese')

        then:
            result.getFailure().cause.cause.message == "JDBC Driver class not found."
    }

    def "validate jdbc driver error handling with deployTestsTask"() {
        setup:
        useToolingApi = false
        directory('src/test/plsql')
        buildFile << '''
                        utplsql {
                            driver = 'org.some.dbDriver'
                        }'''.stripIndent()

        when:
            ExecutionResult result = runTasksWithFailure('utDeploy-cheese')

        then:
            result.getFailure().cause.cause.message == "JDBC Driver class not found."
    }

    def "validate sql connection error handling"() {
        setup:
            useToolingApi = false
            buildFile << '''
                            utplsql {
                                url = 'no db here'
                                username = 'no'
                                password = 'chance'
                            }'''.stripIndent()

        when:
            ExecutionResult result = runTasksWithFailure('utRun-cheese')

        then:
            result.getFailure().cause.cause.message == "Error communicating with the database."
    }
}
