package com.bindglam.guider.util

import java.io.File

object FileUtils {
    fun getAllFiles(directory: File): List<File> {
        val result = ArrayList<File>()

        directory.listFiles().forEach { file ->
            if(file.isDirectory)
                result.addAll(getAllFiles(file))
            else
                result.add(file)
        }

        return result
    }
}