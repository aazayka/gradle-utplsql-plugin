package com.iadams.gradle.plugins.utplsql.subprojects

import com.iadams.gradle.plugins.utplsql.UtplsqlPlugin
import nebula.test.IntegrationSpec
import nebula.test.functional.ExecutionResult

/**
 * Created by Iain Adams
 */
class SubProjectDeployTestsIntegDbSpec extends IntegrationSpec {

    def "use the rule to deploy a test in a subproject"() {
        setup:
        addSubproject('schemaOne')
        addSubproject('schemaTwo')
            directory('schemaOne/src/test/plsql')
            copyResources('src/test/plsql/ut_betwnstr.pks','schemaOne/src/test/plsql/ut_betwnstr.pks')
            copyResources('src/test/plsql/ut_betwnstr.pkb','schemaOne/src/test/plsql/ut_betwnstr.pkb')
            copyResources('buildFiles/schemaOne.gradle', 'schemaOne/build.gradle')
            copyResources('buildFiles/schemaTwo.gradle', 'schemaTwo/build.gradle')

            useToolingApi = false

        when:
            ExecutionResult result = runTasksSuccessfully('schemaOne:utDeploy-betwnstr')

        then:
            result.standardOutput.contains('Deploying: ut_betwnstr.pks')
            result.standardOutput.contains('Deploying: ut_betwnstr.pkb')
    }

    def "deploy all tests across all schemas"() {
        setup:
        addSubproject('schemaOne')
        addSubproject('schemaTwo')
            copyResources('src/test/plsql/ut_betwnstr.pks','schemaOne/src/test/plsql/ut_betwnstr.pks')
            copyResources('src/test/plsql/ut_betwnstr.pkb','schemaOne/src/test/plsql/ut_betwnstr.pkb')
            copyResources('src/test/plsql/ut_simple_example.pks','schemaTwo/src/test/plsql/ut_simple_example.pks')
            copyResources('src/test/plsql/ut_simple_example.pkb','schemaTwo/src/test/plsql/ut_simple_example.pkb')
            copyResources('buildFiles/schemaOne.gradle', 'schemaOne/build.gradle')
            copyResources('buildFiles/schemaTwo.gradle', 'schemaTwo/build.gradle')

            useToolingApi = false

        when:
            ExecutionResult result = runTasksSuccessfully(UtplsqlPlugin.UTPLSQL_DEPLOY_TESTS_TASK)

        then:
            result.standardOutput.contains('Deploying: ut_betwnstr.pks')
            result.standardOutput.contains('Deploying: ut_betwnstr.pkb')
            result.standardOutput.contains('Deploying: ut_simple_example.pks')
            result.standardOutput.contains('Deploying: ut_simple_example.pkb')
    }

    def "deploy all tests across all schemas with custom sourceDir"() {
        setup:
        addSubproject('schemaOne')
        addSubproject('schemaTwo')
            directory('schemaOne/tests')
            directory('schemaTwo/tests')
            copyResources('src/test/plsql/ut_betwnstr.pks','schemaOne/tests/ut_betwnstr.pks')
            copyResources('src/test/plsql/ut_betwnstr.pkb','schemaOne/tests/ut_betwnstr.pkb')
            copyResources('src/test/plsql/ut_simple_example.pks','schemaTwo/tests/ut_simple_example.pks')
            copyResources('src/test/plsql/ut_simple_example.pkb','schemaTwo/tests/ut_simple_example.pkb')
            copyResources('buildFiles/non-default/schemaOne.gradle', 'schemaOne/build.gradle')
            copyResources('buildFiles/non-default/schemaTwo.gradle', 'schemaTwo/build.gradle')

            useToolingApi = false

        when:
            ExecutionResult result = runTasksSuccessfully(UtplsqlPlugin.UTPLSQL_DEPLOY_TESTS_TASK)

        then:
            result.standardOutput.contains('Deploying: ut_betwnstr.pks')
            result.standardOutput.contains('Deploying: ut_betwnstr.pkb')
            result.standardOutput.contains('Deploying: ut_simple_example.pks')
            result.standardOutput.contains('Deploying: ut_simple_example.pkb')
    }

    def "apply the plugin to subprojects and deploy"() {
        setup:
        directory('schemaOne/tests')
        directory('schemaTwo/tests')
        copyResources('src/test/plsql/ut_betwnstr.pks','schemaOne/tests/ut_betwnstr.pks')
        copyResources('src/test/plsql/ut_betwnstr.pkb','schemaOne/tests/ut_betwnstr.pkb')
        copyResources('src/test/plsql/ut_simple_example.pks','schemaTwo/tests/ut_simple_example.pks')
        copyResources('src/test/plsql/ut_simple_example.pkb','schemaTwo/tests/ut_simple_example.pkb')

        useToolingApi = false
        buildFile << """subprojects {
                            apply plugin: 'com.iadams.utplsql'

                            repositories {
                                mavenLocal()
                            }
                            dependencies {
                                junitreport 'org.apache.ant:ant-junit:1.9.4'
                                driver "com.oracle:ojdbc6:11.2.0.1.0"
                            }
                            utplsql {
                                url = "jdbc:oracle:thin:@localhost:1521:test"
                            }
                        }
                        """

        addSubproject('schemaOne',"""utplsql {
                                        username = 'schemaOne'
                                        password = 'schemaOne'
                                        sourceDir = "$projectDir/schemaOne/tests"
                                     }
                                  """)
        addSubproject('schemaTwo',"""utplsql {
                                        username = 'schemaTwo'
                                        password = 'schemaTwo'
                                        sourceDir = "$projectDir/schemaTwo/tests"
                                     }
                                  """)

        when:
        ExecutionResult result = runTasksSuccessfully(UtplsqlPlugin.UTPLSQL_DEPLOY_TESTS_TASK)

        then:
        result.standardOutput.contains('Deploying: ut_betwnstr.pks')
        result.standardOutput.contains('Deploying: ut_betwnstr.pkb')
        result.standardOutput.contains('Deploying: ut_simple_example.pks')
        result.standardOutput.contains('Deploying: ut_simple_example.pkb')
    }
}
