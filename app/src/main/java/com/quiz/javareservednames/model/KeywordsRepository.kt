package com.quiz.javareservednames.model

interface KeywordsRepository {

    fun getInitialWords(): List<Keyword>

}