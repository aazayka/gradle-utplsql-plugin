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

    def "recompile a valid package does not throw exception"(){
        given:
        sql.rows(_) >> [[STATUS:'VALID']]
        dao.getPackageStatus(_) >> 'VALID'
        
        when:
        dao.recompilePackage('UT_BETWNSTR')

        then:
        notThrown UtplsqlDAOException
    }

    def "recompile a broken package throws exception"(){
        given:
        sql.rows(_) >> [[STATUS:'INVALID']]
        dao.getPackageStatus(_) >> 'INVALID'

        when:
        dao.recompilePackage('UT_BETWNSTR')

        then:
        thrown UtplsqlDAOException
    }
}
