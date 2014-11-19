package com.iadams.gradle.plugins.utplsql.tasks.rules

import com.iadams.gradle.plugins.utplsql.tasks.RunTestsTask
import org.gradle.api.Rule
import org.gradle.api.Project

/**
 * Created by Iain Adams on 18/10/2014.
 */
class ExecuteTestRule implements Rule {

    static final String PREFIX = "executeTest"
    Project project

    ExecuteTestRule(Project project) {
        this.project = project
    }

    String getDescription() {
        String.format("Pattern: %s<TestName>: Executes a specific UTPLSQL test.", PREFIX)
    }

    @Override
    String toString() {
        String.format("Rule: %s", getDescription())
    }

    void apply(String taskName) {
        if (taskName.startsWith(PREFIX)) {
            project.task(taskName, type: RunTestsTask) {

                def extension = project.extensions.utplsql

                def packageName = taskName - PREFIX
                driver = extension.driver
                url = extension.url
                username = extension.username
                password = extension.password
                testMethod = extension.testMethod
                packages = [packageName]
                setupMethod = extension.setupMethod
                outputDir = project.file(extension.outputDir)
                failOnNoTests = extension.failOnNoTests
                outputFailuresToConsole = extension.outputFailuresToConsole
            }
        }
    }
}
