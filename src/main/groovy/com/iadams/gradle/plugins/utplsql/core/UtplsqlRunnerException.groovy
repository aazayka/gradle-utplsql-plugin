package com.iadams.gradle.plugins.utplsql.core

/**
 * Created by Iain Adams on 23/09/2014.
 *
 * Custom exception if the utplsql runner fails.
 */
class UtplsqlRunnerException extends Exception {
    UtplsqlRunnerException(String message, Throwable cause) {
        super(message, cause)
    }
}
