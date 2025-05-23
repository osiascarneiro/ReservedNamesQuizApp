package com.quiz.javareservednames.model

class KeywordsMemoryRepository: KeywordsRepository {

    override fun getInitialWords(): List<Keyword> {
        val list = emptyList<Keyword>().toMutableList();
        list.add(Keyword("class"))
        list.add(Keyword("private"))
        list.add(Keyword("public"))
        list.add(Keyword("interface"))
        list.add(Keyword("static"))
        list.add(Keyword("fun"))
        list.add(Keyword("data"))
        return list;
    }

}