/*
 * Gradle Utplsql Plugin
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Iain Adams
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.iadams.gradle.plugins.utplsql.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

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
