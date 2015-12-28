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
import spock.lang.Specification

/**
 * Created by iwarapter on 12/12/14.
 */
class UtplsqlDAOSpec extends Specification {

  Sql sql = Mock(Sql)
  UtplsqlDAO dao

  def setup() {
    dao = new UtplsqlDAO(sql)
  }

  def "invalid package returns false"() {
    given:
    sql.rows(_) >> [[STATUS: 'INVALID']]

    expect:
    dao.getPackageStatus('betwnstr') == 'INVALID'

  }

  def "valid package returns true"() {
    given:
    sql.rows(_) >> [[STATUS: 'VALID']]

    expect:
    dao.getPackageStatus('betwnstr') == 'VALID'
  }

  def "no package returns false"() {
    given:
    sql.rows(_) >> []

    expect:
    dao.getPackageStatus('') == 'NO PACKAGE FOUND'
  }

  def "recompile a valid package does not throw exception"() {
    given:
    sql.rows(_) >> [[STATUS: 'VALID']]
    dao.getPackageStatus(_) >> 'VALID'

    expect:
    dao.recompilePackage('UT_BETWNSTR') == true
  }

  def "recompile a broken package throws exception"() {
    given:
    sql.rows(_) >> [[STATUS: 'INVALID']]
    dao.getPackageStatus(_) >> 'INVALID'

    expect:
    dao.recompilePackage('UT_BETWNSTR') == false
  }
}
