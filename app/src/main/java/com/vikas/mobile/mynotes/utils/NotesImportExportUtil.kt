package com.vikas.mobile.mynotes.utils

import com.vikas.mobile.mynotes.data.entity.NoteExport
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.FileInputStream
import java.nio.channels.FileChannel
import java.nio.charset.Charset

object NotesImportExportUtil {

    fun writeToFile(bufferedWriter: BufferedWriter, noteExport: NoteExport) {
        var isFirstElement = true
        bufferedWriter.write("{")
        noteExport.categoryNotesMap.forEach { notemap ->
            val jsonArray = JSONArray()
            notemap.value.forEach { note -> jsonArray.put(note) }
            if (!isFirstElement) {
                bufferedWriter.write(",")
            }
            isFirstElement = false
            bufferedWriter.write("\"${notemap.key}\":$jsonArray")
        }
        bufferedWriter.write("}")
        bufferedWriter.flush()
    }

    fun readFromFile(fs: FileInputStream): JSONObject? {
        val channel = fs.channel
        val mappedBuffered = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size())
        val notesAsJsonString = Charset.defaultCharset().decode(mappedBuffered).toString()
        fs.close()

        return if (notesAsJsonString.isEmpty()) null else JSONObject(notesAsJsonString)
    }

}