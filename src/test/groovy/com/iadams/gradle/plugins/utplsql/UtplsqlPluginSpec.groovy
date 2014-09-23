package com.iadams.gradle.plugins.utplsql

import com.iadams.gradle.plugins.utplsql.tasks.RunTestsTask
import nebula.test.PluginProjectSpec
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration

/**
 * Created by Iain Adams on 20/09/2014.
 */
class UtplsqlPluginSpec extends PluginProjectSpec  {
    static final String PLUGIN_ID = 'com.iadams.utplsqlplugin'

    @Override
    String getPluginName() {
        return PLUGIN_ID
    }

    def setup() {
        project.apply plugin: pluginName
    }

    def "check base plugin applies clean"() {
        expect:
            project.tasks.findByName('clean')
    }

    def "apply creates task of type RunTestsTask"(){
        setup:
            Task task = project.tasks.findByName('runUtplsqlTests')

        expect:
            task != null
            task instanceof RunTestsTask
            task.description == 'Executes all utPLSQL tests.'
    }
}
