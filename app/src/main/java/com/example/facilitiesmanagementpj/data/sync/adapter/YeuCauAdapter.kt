package com.example.facilitiesmanagementpj.data.sync.adapter

import com.example.facilitiesmanagementpj.data.dao.YeuCauDao
import com.example.facilitiesmanagementpj.data.entity.YeuCau
import com.example.facilitiesmanagementpj.data.sync.SyncableDao

class YeuCauAdapter(private val dao: YeuCauDao) : SyncableDao<YeuCau> {
    override suspend fun insertAll(items: List<YeuCau>) {
        items.forEach { dao.insert(it) }
    }
}