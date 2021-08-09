package com.vikas.mobile.mysafenotes.adapter

import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test

class NoteListAdapterTest {

    @Test
    fun testNoteHeaderExtract() {
        val defaultHeaderCharsCountLength = 35
        val noteContent = "This is the content\n of my note\n, not having new line character"
        val headerString = noteContent.substring(startIndex = 0,
                endIndex = if (noteContent.indexOf("\n") == -1) defaultHeaderCharsCountLength else noteContent.indexOf("\n"))
        println(headerString)
        assertNotNull(headerString)
    }

    @Test
    fun testNoteContentExtract() {
        val noteContent = "This is the content of my note, not having new line character"
        val contentString = noteContent.substring(startIndex = if (noteContent.indexOf("\n") == -1) 0 else noteContent.indexOf("\n"),
            endIndex = noteContent.length)
        println(contentString)
        assertNotNull(contentString)
    }
}