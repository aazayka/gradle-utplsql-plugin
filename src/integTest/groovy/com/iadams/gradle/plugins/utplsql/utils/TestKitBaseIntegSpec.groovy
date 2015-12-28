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
package com.iadams.gradle.plugins.utplsql.utils

import org.apache.commons.io.FileUtils
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class TestKitBaseIntegSpec extends Specification {
  @Rule
  TemporaryFolder testProjectDir
  File buildFile
  File settingsFile

  List<File> pluginClasspath

  def setup() {
    buildFile = testProjectDir.newFile('build.gradle')

    def pluginClasspathResource = getClass().classLoader.findResource("plugin-classpath.txt")
    if (pluginClasspathResource == null) {
      throw new IllegalStateException("Did not find plugin classpath resource, run `testClasses` build task.")
    }

    pluginClasspath = pluginClasspathResource.readLines().collect {new File(it)}

    settingsFile = testProjectDir.newFile('settings.gradle')
  }

  /**
   * Copy a given set of files/directories
   *
   * @param srcDir
   * @param destination
   */
  protected void copyResources(String srcDir, String destination) {
    ClassLoader classLoader = getClass().getClassLoader()
    URL resource = classLoader.getResource(srcDir)
    if (resource == null) {
      throw new TeskKitBaseIntegSpecException("Could not find classpath resource: $srcDir")
    }

    File destinationFile = file(destination)
    File resourceFile = new File(resource.toURI())
    if (resourceFile.file) {
      FileUtils.copyFile(resourceFile, destinationFile)
    } else {
      FileUtils.copyDirectory(resourceFile, destinationFile)
    }
  }

  protected File file(String path, File baseDir = testProjectDir.root) {
    def splitted = path.split('/')
    def directory = splitted.size() > 1 ? directory(splitted[0..-2].join('/'), baseDir) : baseDir
    def file = new File(directory, splitted[-1])
    file.createNewFile()
    file
  }

  protected File directory(String path, File baseDir = testProjectDir.root) {
    new File(baseDir, path).with {
      mkdirs()
      it
    }
  }

  protected boolean fileExists(String path) {
    new File(testProjectDir.root, path).exists()
  }

  protected File addSubproject(String subprojectName, String subBuildGradleText) {
    def subProjFolder = testProjectDir.newFolder(subprojectName)
    testProjectDir.newFile("$subprojectName/build.gradle") << "$subBuildGradleText\n"
    settingsFile << "include '$subprojectName'\n"
    subProjFolder
  }

  protected File addSubproject(String subprojectName) {
    def subProjFolder = testProjectDir.newFolder(subprojectName)
    settingsFile << "include '$subprojectName'\n"
    subProjFolder
  }
}

class TeskKitBaseIntegSpecException extends Exception {
  TeskKitBaseIntegSpecException(String message, Throwable cause) {
    super(message, cause)
  }

  TeskKitBaseIntegSpecException(String message) {
    super(message)
  }
}
