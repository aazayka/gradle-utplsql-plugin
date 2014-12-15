package com.iadams.gradle.plugins.utplsql.tasks.rules

import com.iadams.gradle.plugins.utplsql.tasks.RunTestsTask
import org.gradle.api.Project
import org.gradle.api.Rule

/**
 * Created by Iain Adams on 18/10/2014.
 */
class TestObjectRule implements Rule {

    static final String PREFIX = "utTest-"
    Project project

    TestObjectRule(Project project) {
        this.project = project
    }

    String getDescription() {
        String.format("Pattern: %s<TestName>: Executes the UTPLSQL tests for a specific object.", PREFIX)
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
                testMethod = 'test'
                packages = [packageName]
                setupMethod = extension.setupMethod
                outputDir = project.file(extension.outputDir)
                failOnNoTests = extension.failOnNoTests
            }
        }
    }
}
