package com.iadams.gradle.plugins.utplsql.core

/**
 * Created by Iain Adams on 23/09/2014.
 *
 * Custom exception if the description column in utr_outcome cannot be parsed.
 */
class ResultParserException extends Exception {
    ResultParserException(String message) {
        super(message)
    }
}
