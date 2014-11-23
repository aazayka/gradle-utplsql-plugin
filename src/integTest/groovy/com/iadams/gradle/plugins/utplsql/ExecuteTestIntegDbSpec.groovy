package com.iadams.gradle.plugins.utplsql

import nebula.test.IntegrationSpec
import nebula.test.functional.ExecutionResult

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

    def "use the rule to execute a test"() {
        setup:
            useToolingApi = false
            buildFile << '''
                        apply plugin: 'com.iadams.utplsql'

                        repositories {
                            mavenLocal()
                        }
                        dependencies {
                            driver "com.oracle:ojdbc6:11.2.0.1.0"
                        }

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
            fileExists("build/utplsql/TEST-Betwnstr.xml")
    }

    def "rule fails when executing a test that doesn't exist"() {
        setup:
        useToolingApi = false
        buildFile << '''
                        apply plugin: 'com.iadams.utplsql'

                        repositories {
                            mavenLocal()
                        }
                        dependencies {
                            driver "com.oracle:ojdbc6:11.2.0.1.0"
                        }

                        utplsql {
                            url = "jdbc:oracle:thin:@localhost:1521:test"
                            username = "testing"
                            password = "testing"
                            testMethod = "test"
                        }
                        '''.stripIndent()

        when:
            ExecutionResult result = runTasksWithFailure('executeTestcheese')

        then:
            result.getFailure().cause.cause.message == "No tests were run."
    }

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
