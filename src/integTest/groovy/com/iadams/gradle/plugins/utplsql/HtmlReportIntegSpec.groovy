package com.iadams.gradle.plugins.utplsql

import nebula.test.IntegrationSpec
import nebula.test.functional.ExecutionResult

/**
 * Created by Iain Adams
 */
class HtmlReportIntegSpec extends IntegrationSpec {

    def "Generate a HTML successful report from single XML files"() {
        setup:
        useToolingApi = false
        buildFile << '''
                        apply plugin: 'com.iadams.utplsql'
                        '''.stripIndent()
        copyResources('output/passed.xml', 'build/utplsql/TEST-UT_BETWNSTR.xml')


        when:
            runTasksSuccessfully(UtplsqlPlugin.UTPLSQL_TEST_REPORTS_TASK)

        then:
            fileExists('build/reports/utplsql/0_UT_BETWNSTR.html')
    }

    def "Generate a HTML report with failed tests from XML files"() {
        setup:
        useToolingApi = false
        buildFile << '''
                        apply plugin: 'com.iadams.utplsql'
                        '''.stripIndent()
        copyResources('output/failure.xml', 'build/utplsql/TEST-UT_BETWNSTR.xml')


        when:
        runTasksSuccessfully(UtplsqlPlugin.UTPLSQL_TEST_REPORTS_TASK)

        then:
        fileExists('build/reports/utplsql/0_UT_BETWNSTR.html')
    }

    def "Generate a HTML report with errors from XML files"() {
        setup:
        useToolingApi = false
        buildFile << '''
                        apply plugin: 'com.iadams.utplsql'
                        '''.stripIndent()
        copyResources('output/error.xml', 'build/utplsql/TEST-UT_CHEESE.xml')


        when:
        runTasksSuccessfully(UtplsqlPlugin.UTPLSQL_TEST_REPORTS_TASK)

        then:
        fileExists('build/reports/utplsql/0_UT_CHEESE.html')
    }
}
