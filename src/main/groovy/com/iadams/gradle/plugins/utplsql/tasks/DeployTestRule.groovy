package com.iadams.gradle.plugins.utplsql.tasks

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
            project.getTasks().create(taskName, type: DeployTestsTask)

            //TODO complete the rule to deploy a specific test (from the test source directory)
            println project.utplsql.username
            println project.utplsql.username
            println project.utplsql.username
            println project.utplsql.username
        }
    }
}
