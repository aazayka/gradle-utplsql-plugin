package com.iadams.gradle.plugins.utplsql.tasks

import org.gradle.api.Rule
import org.gradle.api.tasks.TaskContainer

/**
 * Created by Iain Adams on 18/10/2014.
 */
class ExecuteTestRule implements Rule {

    static final String PREFIX = "executeTest"
    final TaskContainer tasks;

    ExecuteTestRule(TaskContainer tasks) {
        this.tasks = tasks;
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
            tasks.create(taskName)
            println "Pinging: " + (taskName - PREFIX)
        }
    }
}
