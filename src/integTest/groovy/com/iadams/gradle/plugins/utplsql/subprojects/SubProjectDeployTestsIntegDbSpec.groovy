package com.iadams.gradle.plugins.utplsql.subprojects

import nebula.test.IntegrationSpec
import nebula.test.functional.ExecutionResult

/**
 * Created by Iain Adams
 */
class SubProjectDeployTestsIntegDbSpec extends IntegrationSpec {

    def setup(){
        addSubproject('schemaOne')
        addSubproject('schemaTwo')
    }

    def "use the rule to deploy a test in a subproject"() {
        setup:
            directory('schemaOne/src/test/plsql')
            copyResources('src/test/plsql/ut_betwnstr.pks','schemaOne/src/test/plsql/ut_betwnstr.pks')
            copyResources('src/test/plsql/ut_betwnstr.pkb','schemaOne/src/test/plsql/ut_betwnstr.pkb')
            copyResources('buildFiles/schemaOne.gradle', 'schemaOne/build.gradle')
            copyResources('buildFiles/schemaTwo.gradle', 'schemaTwo/build.gradle')

            useToolingApi = false

        when:
            ExecutionResult result = runTasksSuccessfully('schemaOne:deployTestbetwnstr')

        then:
            result.standardOutput.contains('Deploying: ut_betwnstr.pks')
            result.standardOutput.contains('Deploying: ut_betwnstr.pkb')
    }

    def "deploy all tests across all schemas"() {
        setup:
            copyResources('src/test/plsql/ut_betwnstr.pks','schemaOne/src/test/plsql/ut_betwnstr.pks')
            copyResources('src/test/plsql/ut_betwnstr.pkb','schemaOne/src/test/plsql/ut_betwnstr.pkb')
            copyResources('src/test/plsql/ut_simple_example.pks','schemaTwo/src/test/plsql/ut_simple_example.pks')
            copyResources('src/test/plsql/ut_simple_example.pkb','schemaTwo/src/test/plsql/ut_simple_example.pkb')
            copyResources('buildFiles/schemaOne.gradle', 'schemaOne/build.gradle')
            copyResources('buildFiles/schemaTwo.gradle', 'schemaTwo/build.gradle')

            useToolingApi = false

        when:
            ExecutionResult result = runTasksSuccessfully('deployUtplsqlTests')

        then:
            result.standardOutput.contains('Deploying: ut_betwnstr.pks')
            result.standardOutput.contains('Deploying: ut_betwnstr.pkb')
            result.standardOutput.contains('Deploying: ut_simple_example.pks')
            result.standardOutput.contains('Deploying: ut_simple_example.pkb')
    }

    def "deploy all tests across all schemas with custom sourceDir"() {
        setup:
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
            ExecutionResult result = runTasksSuccessfully('deployUtplsqlTests')

        then:
            result.standardOutput.contains('Deploying: ut_betwnstr.pks')
            result.standardOutput.contains('Deploying: ut_betwnstr.pkb')
            result.standardOutput.contains('Deploying: ut_simple_example.pks')
            result.standardOutput.contains('Deploying: ut_simple_example.pkb')
    }
}
