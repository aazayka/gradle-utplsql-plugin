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
package com.iadams.gradle.plugins.utplsql.core

import groovy.sql.Sql
import java.sql.SQLException

/**
 * Created by iwarapter.
 */
class UtplsqlDAO {

  Sql sql

  UtplsqlDAO(Sql conn) {
    this.sql = conn
  }

  //TODO add enum for package status responses
  //TODO define a DAO custom exception

  /**
   * @param packageName
   *
   * @return
   * @throws SQLException
   */
  def getPackageStatus(String packageName) throws SQLException {
    def result = sql.rows("select status from user_objects where object_name = ${packageName.toString().toUpperCase()}")

    if (result.size() == 0) {
      return 'NO PACKAGE FOUND'
    }

    if (!result.contains([STATUS: 'INVALID'])) {
      return 'VALID'
    } else {
      return 'INVALID'
    }
  }

  /**
   * @param packageName
   * @param testMethod
   * @param setupMethod
   *
   * @return
   * @throws SQLException
   */
  def runUtplsqlProcedure(String packageName, String testMethod, boolean setupMethod) throws SQLException {
    def setup = testMethod.equals('test') ? ' recompile_in => FALSE,' : ''

    def returnVal

    sql.call(
      "{call utplsql.${Sql.expand(testMethod)}('${Sql.expand(packageName)}', ${Sql.expand(setup)} per_method_setup_in => ${Sql.expand(setupMethod.toString())}); $Sql.VARCHAR := utplsql2.runnum}") {
      result ->
        returnVal = result
    }

    return returnVal
  }

  /**
   * @param packageName
   *
   * @return
   * @throws SQLException
   */
  def recompilePackage(String packageName) throws UtplsqlDAOException {
    try {
      sql.execute("ALTER PACKAGE ${Sql.expand(packageName.toString().toUpperCase())} COMPILE")
      sql.execute("ALTER PACKAGE ${Sql.expand(packageName.toString().toUpperCase())} COMPILE BODY")

      if (getPackageStatus(packageName) != 'VALID') {
        return false
      }
      return true
    }
    catch (SQLException e) {
      throw new UtplsqlDAOException("Error communicating with the database.", e)
    }
  }
}
