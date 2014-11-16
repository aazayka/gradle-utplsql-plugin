package com.iadams.gradle.plugins.utplsql

import nebula.test.IntegrationSpec
import nebula.test.functional.ExecutionResult
import spock.lang.Unroll

/**
 * Created by Iain Adams
 */
class UtplsqlPluginIntegDbSpec extends IntegrationSpec {

    //TODO Actually write some tests using a DB!
    def "should allow to run pldoc with different Gradle versions"() {
        setup:
            useToolingApi = false
            buildFile << '''
                        apply plugin: 'com.iadams.utplsql'
                        '''.stripIndent()

        when:
            ExecutionResult result = runTasksSuccessfully('tasks')

        then:
            result.standardOutput.contains('runUtplsqlTests - Executes all utPLSQL tests.')
    }
}
