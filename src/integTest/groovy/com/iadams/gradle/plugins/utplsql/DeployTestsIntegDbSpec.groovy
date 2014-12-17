package com.iadams.gradle.plugins.utplsql

import nebula.test.IntegrationSpec
import nebula.test.functional.ExecutionResult

/**
 * Created by Iain Adams
 */
class DeployTestsIntegDbSpec extends IntegrationSpec {

    def "use the rule to deploy a test"() {
        setup:
            directory('src/test/plsql')
            copyResources('src/test/plsql','src/test/plsql')

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
            ExecutionResult result = runTasksSuccessfully('utDeploy-betwnstr')

        then:
            result.standardOutput.contains('Deploying: ut_betwnstr.pks')
            result.standardOutput.contains('Deploying: ut_betwnstr.pkb')
    }

    def "use task to deploy all tests"() {
        setup:
            directory('src/test/plsql')
            copyResources('src/test/plsql','src/test/plsql')

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
            ExecutionResult result = runTasksSuccessfully(UtplsqlPlugin.UTPLSQL_DEPLOY_TESTS_TASK)

        then:
            result.standardOutput.contains('Deploying: ut_betwnstr.pks')
            result.standardOutput.contains('Deploying: ut_betwnstr.pkb')
            result.standardOutput.contains('Deploying: ut_simple_example.pks')
            result.standardOutput.contains('Deploying: ut_simple_example.pkb')
    }

    def "use task to deploy all tests in non-standard folder"() {
        setup:
            directory('tests')
            copyResources('src/test/plsql','tests')

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
                            sourceDir = "${projectDir}/tests"
                        }
                        '''.stripIndent()

        when:
            ExecutionResult result = runTasksSuccessfully(UtplsqlPlugin.UTPLSQL_DEPLOY_TESTS_TASK)

        then:
            result.standardOutput.contains('Deploying: ut_betwnstr.pks')
            result.standardOutput.contains('Deploying: ut_betwnstr.pkb')
            result.standardOutput.contains('Deploying: ut_simple_example.pks')
            result.standardOutput.contains('Deploying: ut_simple_example.pkb')
    }

    def "deploy a package that doesnt compile results in failure"() {
        setup:
        directory('tests')
        copyResources('src/broken/plsql','tests')

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
                            sourceDir = "${projectDir}/tests"
                        }
                        '''.stripIndent()

        when:
        ExecutionResult result = runTasksWithFailure(UtplsqlPlugin.UTPLSQL_DEPLOY_TESTS_TASK)

        then:
        result.standardOutput.contains('Deploying: ut_broken.pks')
        result.standardOutput.contains('Deploying: ut_broken.pkb')
        result.getFailure().cause.cause.message == "The package ut_broken failed to compile."
    }
}
