package com.iadams.gradle.plugins.utplsql

import nebula.test.IntegrationSpec
import nebula.test.functional.ExecutionResult

/**
 * Created by Iain Adams
 */
class DeployTestsIntegDbSpec extends IntegrationSpec {

    def setup() {
        directory('src/main/plsql')
        copyResources('src/main/plsql','src/main/plsql')

        directory('src/test/plsql')
        copyResources('src/test/plsql','src/test/plsql')
    }

    def "use the rule to deploy a test"() {
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
                        }
                        '''.stripIndent()

        when:
            ExecutionResult result = runTasksSuccessfully('deployTestbetwnstr')

        then:
            result.standardOutput.contains('Deploying: ut_betwnstr.pks')
            result.standardOutput.contains('Deploying: ut_betwnstr.pkb')
    }

    def "use task to deploy all tests"() {
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
                        }
                        '''.stripIndent()

        when:
            ExecutionResult result = runTasksSuccessfully('deployUtplsqlTests')

        then:
            result.standardOutput.contains('Deploying: ut_betwnstr.pks')
            result.standardOutput.contains('Deploying: ut_betwnstr.pkb')
            result.standardOutput.contains('Deploying: ut_simple_example.pks')
            result.standardOutput.contains('Deploying: ut_simple_example.pkb')
    }
}
