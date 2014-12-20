package com.iadams.gradle.plugins.utplsql

import com.iadams.gradle.plugins.utplsql.extensions.UtplsqlPluginExtension
import com.iadams.gradle.plugins.utplsql.tasks.HtmlReportTask
import com.iadams.gradle.plugins.utplsql.tasks.rules.GenerateTestRule
import com.iadams.gradle.plugins.utplsql.tasks.rules.TestObjectRule
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import com.iadams.gradle.plugins.utplsql.tasks.RunTestsTask
import com.iadams.gradle.plugins.utplsql.tasks.DeployTestsTask
import com.iadams.gradle.plugins.utplsql.tasks.rules.ExecuteTestRule
import com.iadams.gradle.plugins.utplsql.tasks.rules.DeployTestRule

/**
 * Created by Iain Adams on 02/09/2014.
 */
class UtplsqlPlugin implements Plugin<Project> {
    static final UTPLSQL_RUN_TESTS_TASK = 'utRun'
    static final UTPLSQL_DEPLOY_TESTS_TASK = 'utDeploy'
    static final UTPLSQL_TEST_REPORTS_TASK = 'utReport'
    static final UTPLSQL_ALL_TASK = 'utplsql'
    static final UTPLSQL_EXTENSION = 'utplsql'

    /**
     *
     * @param project
     */
    @Override
    void apply(Project project) {

        project.plugins.apply(BasePlugin.class)

        project.extensions.create( UTPLSQL_EXTENSION, UtplsqlPluginExtension, project)

        project.configurations{ driver }
        project.configurations{ junitreport }

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
            conventionMapping.sourceDir = { extension.sourceDir }
            conventionMapping.setupMethod = { extension.setupMethod }
            conventionMapping.outputDir = { project.file(extension.outputDir)}
            conventionMapping.failOnNoTests = { extension.failOnNoTests }
        }

        project.tasks.withType( DeployTestsTask ) {
            conventionMapping.driver = { extension.driver }
            conventionMapping.url = { extension.url }
            conventionMapping.username = { extension.username }
            conventionMapping.password = { extension.password }
            conventionMapping.sourceDir = { extension.sourceDir }
        }

        project.task( UTPLSQL_DEPLOY_TESTS_TASK , type: DeployTestsTask) {
            description = 'Deploys all the UTPLSQL tests in the test folder with JDBC driver.'
            group = 'utplsql'
        }

        project.task( UTPLSQL_RUN_TESTS_TASK , type: RunTestsTask) {
            description = 'Executes all utPLSQL tests.'
            group = 'utplsql'
        }

        project.task( UTPLSQL_TEST_REPORTS_TASK, type: HtmlReportTask) {
            description = 'Generates HTML reports from the XML results.'
            group = 'utplsql'
            resultsDir = project.file(extension.outputDir)
            reportsDir = project.file("${project.buildDir}/reports/utplsql")
        }

        project.task( UTPLSQL_ALL_TASK, dependsOn: [UTPLSQL_DEPLOY_TESTS_TASK, UTPLSQL_RUN_TESTS_TASK]) {
            description = 'Deploy, Run, Report'
            group = 'utplsql'
        }
        project.getTasks().getByName(UTPLSQL_RUN_TESTS_TASK).mustRunAfter project.getTasks().getByName(UTPLSQL_DEPLOY_TESTS_TASK)
        project.getTasks().getByName(UTPLSQL_RUN_TESTS_TASK).finalizedBy project.getTasks().getByName(UTPLSQL_TEST_REPORTS_TASK)

        project.getTasks().addRule(new ExecuteTestRule(project))
        project.getTasks().addRule(new TestObjectRule(project))
        project.getTasks().addRule(new DeployTestRule(project))
        project.getTasks().addRule(new GenerateTestRule(project))
    }
}
