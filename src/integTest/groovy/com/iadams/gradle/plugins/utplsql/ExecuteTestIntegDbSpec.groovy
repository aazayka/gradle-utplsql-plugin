package com.iadams.gradle.plugins.utplsql

import nebula.test.IntegrationSpec
import nebula.test.functional.ExecutionResult

/**
 * Created by Iain Adams
 */
class ExecuteTestIntegDbSpec extends IntegrationSpec {

    def setup() {
        directory('src/test/plsql')
        copyResources('src/test/plsql','src/test/plsql')
    }

    def "use the rule to execute a test"() {
        setup:
            useToolingApi = false
            buildFile << '''
                        apply plugin: 'com.iadams.utplsql'

                        buildscript {
                            repositories {
                                mavenLocal()
                            }
                            dependencies {
                                classpath "com.oracle:ojdbc6:11.2.0.1.0"
                            }
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

                        buildscript {
                            repositories {
                                mavenLocal()
                            }
                            dependencies {
                                classpath "com.oracle:ojdbc6:11.2.0.1.0"
                            }
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

    def "execute all the tests in the sourceDir"() {
        setup:
        useToolingApi = false
        buildFile << '''
                        apply plugin: 'com.iadams.utplsql'

                        buildscript {
                            repositories {
                                mavenLocal()
                            }
                            dependencies {
                                classpath "com.oracle:ojdbc6:11.2.0.1.0"
                            }
                        }

                        utplsql {
                            url = "jdbc:oracle:thin:@localhost:1521:test"
                            username = "testing"
                            password = "testing"
                            sourceDir = "${projectDir}/src/test/plsql"
                        }
                        '''.stripIndent()

        when:
            runTasksSuccessfully('runUtplsqlTests')

        then:
            fileExists("build/utplsql/TEST-ut_betwnstr.xml")
            fileExists("build/utplsql/TEST-ut_simple_example.xml")
    }
}
