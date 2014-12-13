package com.iadams.gradle.plugins.utplsql.core

/**
 * Created by iwarapter on 13/12/14.
 */
class UtplsqlDAOException extends Exception {
    UtplsqlDAOException(String message, Throwable cause) {
        super(message, cause)
    }

    UtplsqlDAOException(String message){
        super(message)
    }
}
