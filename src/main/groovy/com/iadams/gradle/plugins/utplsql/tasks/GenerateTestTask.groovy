package com.iadams.gradle.plugins.utplsql.tasks

import com.iadams.gradle.plugins.utplsql.UtplsqlPlugin
import com.iadams.gradle.plugins.utplsql.core.UtplsqlRunner
import groovy.sql.Sql
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

import java.sql.SQLException

/**
 * Created by Iain Adams on 19/12/2014.
 */
class GenerateTestTask extends DefaultTask {

    /**
     * The name of the database object we wish to create a test for.
     */
    @Input
    String testName

    /**
     * Location to load all the tests from.
     *
     */
    @Input
    String sourceDir

    /**
     *
     * @throws GradleException
     */
    @TaskAction
    void generateTestTask() throws GradleException {

        project.file(getSourceDir()).mkdirs()

        logger.info "[INFO] Generating a unit test shell for ${testName}."
        logger.info "[INFO] ${getSourceDir()}/ut_${testName}.pks"
        def spec = project.file("${getSourceDir()}/ut_${testName}.pks")
        spec.createNewFile()

        spec.text = """CREATE OR REPLACE PACKAGE ut_${testName}
IS
    PROCEDURE ut_setup;
    PROCEDURE ut_teardown;

    -- For each program to test...
    PROCEDURE ut_${testName};
END ut_${testName};
"""


        logger.info "[INFO] ${getSourceDir()}/ut_${testName}.pkb"
        def body = project.file("${getSourceDir()}/ut_${testName}.pkb")
        body.createNewFile()

        body.text = """CREATE OR REPLACE PACKAGE BODY ut_${testName}
IS
    PROCEDURE ut_setup
    IS
    BEGIN
    NULL;
    END;

    PROCEDURE ut_teardown
    IS
    BEGIN
    NULL;
    END;

    -- For each program to test...
END ut_${testName};
""".stripIndent()

    }
}
