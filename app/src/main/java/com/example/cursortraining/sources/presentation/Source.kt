package com.example.cursortraining.sources.presentation

import com.example.cursortraining.sources.data.SourceData

data class Source(
    val id: String,
    val name: String,
    val description: String?,
)

object SourceModel {
    fun mapSource(
        sourceData: SourceData,
        index: Int,
    ): Source {
        val stableId =
            sourceData.id.takeIf { it.isNotBlank() }
                ?: "${sourceData.name}_$index"
        return Source(
            id = stableId,
            name = sourceData.name,
            description = sourceData.description?.takeIf { it.isNotBlank() },
        )
    }
}