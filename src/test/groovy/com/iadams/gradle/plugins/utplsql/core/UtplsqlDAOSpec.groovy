package com.iadams.gradle.plugins.utplsql.core

import groovy.sql.Sql
import spock.lang.Specification
import spock.lang.Unroll

import java.sql.SQLException

/**
 * Created by iwarapter on 12/12/14.
 */
class UtplsqlDAOSpec extends Specification {

    Sql sql = Mock(Sql)
    UtplsqlDAO dao

    def setup(){
        dao = new UtplsqlDAO(sql)
    }

    def "invalid package returns false"() {
        given:
        sql.rows(_) >> [[STATUS:'INVALID']]

        expect:
        dao.getPackageStatus('betwnstr') == 'INVALID'

    }

    def "valid package returns true"() {
        given:
        sql.rows(_) >> [[STATUS:'VALID']]

        expect:
        dao.getPackageStatus('betwnstr') == 'VALID'
    }

    def "no package returns false"() {
        given:
        sql.rows(_) >> []

        expect:
        dao.getPackageStatus('') == 'NO PACKAGE FOUND'
    }

    def "given spec: true and body: true return true"(){
        given:
        1 * sql.execute(_) >> true
        1 * sql.execute(_) >> true

        expect:
        dao.recompilePackage('UT_BETWNSTR') == true
    }

    @Unroll
    def "given spec: #spec and body: #body throw exception"(){
        given:
        1 * sql.execute(_) >> spec
        1 * sql.execute(_) >> body

        when:
        dao.recompilePackage('UT_BETWNSTR')

        then:
        thrown UtplsqlDAOException

        where:
        spec    | body
        true    | false
        false   | true
        false   | false
    }
}
