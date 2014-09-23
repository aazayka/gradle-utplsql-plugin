package com.iadams.gradle.plugins.utplsql.tasks

import groovy.sql.Sql
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

import com.iadams.gradle.plugins.utplsql.UtplsqlRunner

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
    String setupMethod

    /**
     * Location to which we will write the report file. Defaults to the gradle buildDir.
     *
     */
    @Input
    @Optional
    File outputDirectory

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

    @TaskAction
    void runUtplsqlTests() {

        println "URL: ${getUrl()}"
        println "Username: ${getUsername()}"
        println "Password: ${getPassword()}"
        println "Driver: ${getDriver()}"
        println "Packages: ${getPackages()}"
        println "TestMethod: ${getTestMethod()}"
        println "SetupMethod: ${getSetupMethod()}"
        println "OutputDir: ${outputDirectory}"

        try {
            def sql = Sql.newInstance(getUrl() ,getUsername() ,getPassword() ,getDriver())

            UtplsqlRunner runner = new UtplsqlRunner(new File("$outputDirectory/utplsql"))

            if(getPackages()) {
                getPackages().each{
                    runner.runPackage(sql, it, getTestMethod(), getSetupMethod())
                }
            }
        }
        catch (ClassNotFoundException e) {
            throw new GradleException("JDBC Driver class not found", e);
        }
    }
}
