package com.iadams.gradle.plugins.utplsql

import nebula.test.IntegrationSpec
import nebula.test.functional.ExecutionResult
import spock.lang.Unroll

/**
 * Created by Iain Adams
 */
class GradleCompatabilityIntegSpec extends IntegrationSpec {

    @Unroll("should use Gradle #requestedGradleVersion when requested")
    def "should allow to run utplsql with different Gradle versions"() {
        setup:
            useToolingApi = false
            buildFile << '''
                        apply plugin: 'com.iadams.utplsql'
                        '''.stripIndent()

        and:
            gradleVersion = requestedGradleVersion

        when:
            ExecutionResult result = runTasksSuccessfully('tasks')

        then:
            result.standardOutput.contains('runUtplsqlTests - Executes all utPLSQL tests.')

        where:
            requestedGradleVersion << ['1.6','1.7','1.8','1.9','1.10','1.11','1.12','2.0','2.1','2.2']
    }
}
