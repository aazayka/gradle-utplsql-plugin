package com.iadams.gradle.plugins.utplsql

import nebula.test.IntegrationSpec
import nebula.test.functional.ExecutionResult
import org.gradle.api.logging.LogLevel

/**
 * Created by Iain Adams
 */
class UtplsqlIntegDbSpec extends IntegrationSpec {

    def setup() {
        directory('src/test/plsql')
    }

    def "run utplsql with passing tests"() {
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
                    }
                    '''.stripIndent()

        copyResources('src/test/plsql','src/test/plsql')

        when:
        runTasksSuccessfully('utplsql')

        then:
        fileExists("build/utplsql/TEST-ut_betwnstr.xml")
        fileExists("build/reports/utplsql/0_ut_betwnstr.html")
        fileExists("build/reports/utplsql/1_ut_simple_example.html")
    }

    def "run utplsql with failing tests"() {
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
                    }
                    '''.stripIndent()

        copyResources('src/failing/plsql','src/test/plsql')

        when:
        ExecutionResult result = runTasksWithFailure('utplsql')

        then:
        fileExists("build/utplsql/TEST-ut_failing.xml")
        fileExists("build/reports/utplsql/0_ut_failing.html")

        result.getFailure().cause.cause.message == "Failing unit tests.\nTests: 5 \nFailures: 1 \nErrors: 0"
    }

    def "run utplsql with elevated logging"() {
        setup:
        useToolingApi = false
        logLevel = LogLevel.INFO
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
                    }
                    '''.stripIndent()

        copyResources('src/test/plsql','src/test/plsql')

        when:
        ExecutionResult result = runTasksSuccessfully('utplsql')

        then:
        result.standardOutput.contains(':utDeploy')
        result.standardOutput.contains(':utRun')
        result.standardOutput.contains(':utReport')
        result.standardOutput.contains(':utplsql')
        result.standardOutput.contains('[INFO] Tests Run: 4')
        result.standardOutput.contains('[INFO] Failures: 0')
        result.standardOutput.contains('[INFO] Errors: 0')
}
}
