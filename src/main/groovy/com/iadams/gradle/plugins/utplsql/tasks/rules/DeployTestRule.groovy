package com.iadams.gradle.plugins.utplsql.tasks.rules

import com.iadams.gradle.plugins.utplsql.tasks.DeployTestsTask
import org.gradle.api.Project
import org.gradle.api.Rule

/**
 * Created by Iain Adams on 18/10/2014.
 */
class DeployTestRule implements Rule {

    static final String PREFIX = "deployTest"
    Project project

    DeployTestRule(Project project) {
        this.project = project
    }

    String getDescription() {
        String.format("Pattern: %s<TestName>: Deploys a specific UTPLSQL test.", PREFIX)
    }

    @Override
    String toString() {
        String.format("Rule: %s", getDescription())
    }

    void apply(String taskName) {
        if (taskName.startsWith(PREFIX)) {
            project.task(taskName, type: DeployTestsTask) {
                def extension = project.extensions.utplsql

                def packageName = taskName - PREFIX
                driver = extension.driver
                url = extension.url
                username = extension.username
                password = extension.password
                outputDir = project.file(extension.outputDir)

                extension.includes = "**/*${packageName}.*"
            }

        }
    }
}
