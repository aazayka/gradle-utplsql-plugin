package com.iadams.gradle.plugins.utplsql

import com.iadams.gradle.plugins.utplsql.tasks.DeployTestsTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import com.iadams.gradle.plugins.utplsql.tasks.RunTestsTask
import com.iadams.gradle.plugins.utplsql.tasks.ExecuteTestRule

/**
 * Created by Iain Adams on 02/09/2014.
 */
class UtplsqlPlugin implements Plugin<Project> {
    static final UTPLSQL_RUN_TESTS_TASK = 'runUtplsqlTests'
    static final UTPLSQL_DEPLOY_TESTS_TASK = 'deployUtplsqlTests'
    static final UTPLSQL_EXTENSION = 'utplsql'

    /**
     *
     * @param project
     */
    @Override
    void apply(Project project) {

        project.plugins.apply(BasePlugin.class)

        project.extensions.create( UTPLSQL_EXTENSION, UtplsqlPluginExtension )

        addTasks(project)
    }

    /**
     * Add tasks to the plugin
     * @param project the target Gradle project
     */
    void addTasks( Project project ) {
        def extension = project.extensions.findByName( UTPLSQL_EXTENSION )

        project.tasks.withType( RunTestsTask ) {
            conventionMapping.driver = { extension.driver }
            conventionMapping.url = { extension.url }
            conventionMapping.username = { extension.username }
            conventionMapping.password = { extension.password }
            conventionMapping.testMethod = { extension.testMethod }
            conventionMapping.packages = { extension.packages }
            conventionMapping.setupMethod = { extension.setupMethod }
            outputDirectory = project.buildDir
            conventionMapping.failOnNoTests = { extension.failOnNoTests }
            conventionMapping.outputFailuresToConsole = { extension.outputFailuresToConsole }
        }

        project.task( UTPLSQL_DEPLOY_TESTS_TASK , type: DeployTestsTask) {
            description = 'Deploys all the UTPLSQL tests with JDBC.'
            group = 'utplsql'
            conventionMapping.driver = { extension.driver }
            conventionMapping.url = { extension.url }
            conventionMapping.username = { extension.username }
            conventionMapping.password = { extension.password }
            inputDirectory = new File("${project.projectDir}/src/test/plsql")
        }

        project.task( UTPLSQL_RUN_TESTS_TASK , type: RunTestsTask) {
            description = 'Executes all utPLSQL tests.'
            group = 'utplsql'
        }

        project.getTasks().addRule(new ExecuteTestRule(project.getTasks()))
    }
}
