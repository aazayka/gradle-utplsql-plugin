package com.iadams.gradle.plugins.utplsql

import nebula.test.IntegrationSpec

/**
 * Created by Iain Adams
 */
class GenerateTestIntegSpec extends IntegrationSpec {

    def setup(){
        useToolingApi = false
        buildFile << '''apply plugin: 'com.iadams.utplsql'

                        repositories {
                            mavenCentral()
                        }

                        dependencies {
                            junitreport 'org.apache.ant:ant-junit:1.9.4'
                        }'''.stripIndent()
    }

    def "Generate test file from the rule"() {
        when:
            runTasksSuccessfully('utGen-package')

        then:
            fileExists("src/test/plsql/ut_package.pks")
            fileExists("src/test/plsql/ut_package.pkb")
    }
}
