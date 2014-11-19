package com.iadams.gradle.plugins.utplsql

import com.iadams.gradle.plugins.utplsql.tasks.RunTestsTask
import nebula.test.PluginProjectSpec
import org.gradle.api.Task

/**
 * Created by Iain Adams on 20/09/2014.
 */
class UtplsqlPluginSpec extends PluginProjectSpec  {
    static final String PLUGIN_ID = 'com.iadams.utplsql'

    @Override
    String getPluginName() {
        return PLUGIN_ID
    }

    def setup() {
        project.apply plugin: pluginName
    }

    def "apply creates task of type RunTestsTask"(){
        setup:
        Task task = project.tasks.findByName( UtplsqlPlugin.UTPLSQL_RUN_TESTS_TASK )

        expect:
        task != null
        task instanceof RunTestsTask
        task.description == 'Executes all utPLSQL tests.'
    }

    def "check base plugin applies clean"() {
        expect:
            project.tasks.findByName('clean')
    }

    def "apply creates utplsql extension" () {
        expect:
            project.extensions.findByName( UtplsqlPlugin.UTPLSQL_EXTENSION )
    }

    def "check default task uses the utplsql extension"() {
        when:
            project.utplsql {
                driver = 'org.hsqldb.jdbcDriver'
                url = 'jdbc:oracle:thin:@localhost:1521:test'
                password = 'testing'
                username = 'testing'
                testMethod = 'test'
                packages = ['betwnstr']
                setupMethod = false
                outputDir = 'build/other'
                failOnNoTests = true
                outputFailuresToConsole = true
            }

        then:
            Task task = project.tasks.findByName( UtplsqlPlugin.UTPLSQL_RUN_TESTS_TASK )
            task.driver == 'org.hsqldb.jdbcDriver'
            task.url == 'jdbc:oracle:thin:@localhost:1521:test'
            task.password == 'testing'
            task.username == 'testing'
            task.testMethod == 'test'
            task.packages == ['betwnstr']
            task.setupMethod == false
            task.outputDir == project.file("$projectDir/build/other")
            task.failOnNoTests == true
            task.outputFailuresToConsole == true
    }
}
