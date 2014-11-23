package com.iadams.gradle.plugins.utplsql.tasks

import com.iadams.gradle.plugins.utplsql.core.UtplsqlRunner
import com.iadams.gradle.plugins.utplsql.core.ReportGenerator
import groovy.sql.Sql
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import java.sql.SQLException

/**
 * Created by Iain Adams on 13/09/2014.
 */
class RunTestsTask extends DefaultTask {
    /**
     * The JDBC driver to use. Defaults to Oracle.
     *
     */
    @Input
    String driver
    /**
     * The JDBC URL to use.
     *
     */
    @Input
    String url

    /**
     * The username to connect to the database.
     *
     */
    @Input
    String username

    /**
     * The password to connect to the database.
     *
     */
    @Input
    String password

    /**
     * The type of test method to execute. Can be either test or run. Defaults to test.
     *
     */
    @Input
    String testMethod

    /**
     * The name of all the packages to test.
     *
     */
    @Input
    def packages = []

    /**
     * The setup method to use. set to TRUE to execute setup and teardown for each procedure. FALSE to execute for each package. Default is FALSE.
     *
     */
    @Input
    @Optional
    Boolean setupMethod

    /**
     * Location to which we will write the report file. Defaults to the gradle buildDir.
     *
     */
    @OutputDirectory
    File outputDir

    /**
     * If there is a connection problem or DB PLSQL installation error the plugin reports 0 tests run. This is an error condition which should be
     * flagged, by default an error exception shall be raised if 0 tests are run. This state can be unset in the plugin configuration if desired
     *
     */
    @Input
    @Optional
    Boolean failOnNoTests

    /**
     * If errors occur the failure message can be written to the console.
     *
     */
    @Input
    @Optional
    Boolean outputFailuresToConsole

    /**
     *
     * @throws GradleException
     */
    @TaskAction
    void runUtplsqlTests() throws GradleException {
        //TODO This task should really generate a list of packages from the test-source folder

        logger.info "URL: ${getUrl()}"
        logger.info "Username: ${getUsername()}"
        logger.info "Driver: ${getDriver()}"
        logger.info "Packages: ${getPackages()}"
        logger.info "TestMethod: ${getTestMethod()}"
        logger.info "SetupMethod: ${getSetupMethod()}"
        logger.info "OutputDir: ${getOutputDir()}"

        try {
            //TODO extract the base configuration for the oracle driver into an abstract task.
            project.configurations.driver.each {File file ->
                project.gradle.class.classLoader.addURL(file.toURL())
            }

            def sql = Sql.newInstance(getUrl() ,getUsername() ,getPassword() ,getDriver())

            UtplsqlRunner runner = new UtplsqlRunner(getOutputDir(), logger)
            runner.reportGen = new ReportGenerator()
            runner.sql = sql

            def failedTests = false
            def totalTests = 0

            getPackages().each{
                new File("${runner.outputDir}/TEST-${it}.xml").write(runner.runPackage( it, getTestMethod(), getSetupMethod()))
                totalTests += runner.results.getTestsRun()

                if(runner.results.getTestFailures()){
                    failedTests = true
                    logger.error "Package $it contains ${runner.results.getTestFailures()} failing tests."
                }
            }

            if(totalTests == 0 && project.extensions.utplsql.failOnNoTests){
                throw new GradleException("No tests were run.")
            }

            if(failedTests){
                throw new GradleException("Failing unit tests.")
            }
        }
        catch (ClassNotFoundException e) {
            throw new GradleException("JDBC Driver class not found.", e)
        }
        catch (SQLException e) {
            throw new GradleException("Error communicating with the database.", e)
        }
    }
}
