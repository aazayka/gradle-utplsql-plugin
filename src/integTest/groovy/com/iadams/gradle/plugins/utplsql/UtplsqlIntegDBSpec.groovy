package com.iadams.gradle.plugins.utplsql

import nebula.test.IntegrationSpec
import nebula.test.functional.ExecutionResult
import org.gradle.api.logging.LogLevel
import spock.lang.Unroll

/**
 * Created by Iain Adams
 */
class UtplsqlIntegDbSpec extends IntegrationSpec {

    def setup() {
        useToolingApi = false
        directory('src/test/plsql')
        buildFile << '''
                    apply plugin: 'com.iadams.utplsql'

                    repositories {
                        mavenLocal()
                        mavenCentral()
                    }
                    dependencies {
                        driver "com.oracle:ojdbc6:11.2.0.1.0"
                        junitreport 'org.apache.ant:ant-junit:1.9.4'
                    }

                    utplsql {
                        url = "jdbc:oracle:thin:@localhost:1521:test"
                        username = "testing"
                        password = "testing"
                    }
                    '''.stripIndent()
    }

    def "run utplsql with passing tests"() {
        setup:
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
        copyResources('src/failing/plsql','src/test/plsql')

        when:
        ExecutionResult result = runTasksWithFailure('utplsql')

        then:
        fileExists("build/utplsql/TEST-ut_failing.xml")
        fileExists("build/reports/utplsql/0_ut_failing.html")

        result.getFailure().cause.cause.message == "Failing unit tests.\nTests: 5 \nFailures: 1 \nErrors: 0"
    }

    def "run utplsql with erroring tests"() {
        setup:
        copyResources('src/broken/plsql','src/test/plsql')
        copyResources('src/test/plsql', 'src/test/plsql')

        when:
        ExecutionResult result = runTasksWithFailure('utplsql')

        then:
        fileExists("build/utplsql/TEST-ut_broken.xml")
        fileExists("build/reports/utplsql/1_ut_broken-errors.html")

        result.getFailure().cause.cause.message == "Failing unit tests.\nTests: 9 \nFailures: 0 \nErrors: 1"
    }

    def "run utplsql with elevated logging"() {
        setup:
        logLevel = LogLevel.INFO
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

    @Unroll
    def "run utplsql against version #requestedGradleVersion of gradle"(){
        setup:
        copyResources('src/test/plsql','src/test/plsql')

        when:
        gradleVersion = requestedGradleVersion

        then:
        runTasksSuccessfully(UtplsqlPlugin.UTPLSQL_ALL_TASK)

        where:
        requestedGradleVersion << ['1.6','1.7','1.8','1.9','1.10','1.11','1.12','2.0','2.1','2.2']
    }
}
