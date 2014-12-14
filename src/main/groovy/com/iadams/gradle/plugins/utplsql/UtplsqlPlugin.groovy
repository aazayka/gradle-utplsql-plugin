package com.iadams.gradle.plugins.utplsql

import com.iadams.gradle.plugins.utplsql.extensions.UtplsqlPluginExtension
import com.iadams.gradle.plugins.utplsql.tasks.HtmlReportTask
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
    static final UTPLSQL_RUN_TESTS_TASK = 'runUtplsqlTests'
    static final UTPLSQL_DEPLOY_TESTS_TASK = 'deployUtplsqlTests'
    static final UTPLSQL_TEST_REPORTS_TASK = 'utplsqlReports'
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
            conventionMapping.outputFailuresToConsole = { extension.outputFailuresToConsole }
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

        //TODO Ensure we can generate HTML reports
        /*project.task( "UtplsqlReport") {
            group = 'utplsql'

            ant.taskdef(name: 'junitreport',
                        classname: 'org.apache.tools.ant.taskdefs.optional.junit.XMLResultAggregator',
                        classpath: project.configurations.runtime.asPath
            )
            ant.junitreport(todir: "${project.buildDir}/reports") {
                fileset(dir: "${project.buildDir}/utplsql", includes: 'TEST-*.xml')
                report(todir: "${project.buildDir}/reports/utplsql", format: "frames")
            }
        }*/

        project.getTasks().addRule(new ExecuteTestRule(project))
        project.getTasks().addRule(new DeployTestRule(project))
    }
}
