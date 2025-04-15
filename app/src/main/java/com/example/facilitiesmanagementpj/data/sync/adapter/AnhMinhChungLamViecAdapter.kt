package com.example.facilitiesmanagementpj.data.sync.adapter

import com.example.facilitiesmanagementpj.data.dao.AnhMinhChungLamViecDao
import com.example.facilitiesmanagementpj.data.entity.AnhMinhChungLamViec
import com.example.facilitiesmanagementpj.data.sync.SyncableDao

class AnhMinhChungLamViecAdapter(private val dao: AnhMinhChungLamViecDao) : SyncableDao<AnhMinhChungLamViec> {
    override suspend fun insertAll(items: List<AnhMinhChungLamViec>) {
        items.forEach { dao.insert(it) }
    }
}