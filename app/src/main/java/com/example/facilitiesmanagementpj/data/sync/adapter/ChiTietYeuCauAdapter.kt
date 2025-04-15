package com.example.facilitiesmanagementpj.data.sync.adapter

import com.example.facilitiesmanagementpj.data.dao.ChiTietYeuCauDao
import com.example.facilitiesmanagementpj.data.entity.ChiTietYeuCau
import com.example.facilitiesmanagementpj.data.sync.SyncableDao

class ChiTietYeuCauAdapter(private val dao: ChiTietYeuCauDao) : SyncableDao<ChiTietYeuCau> {
    override suspend fun insertAll(items: List<ChiTietYeuCau>) {
        items.forEach { dao.insert(it) }
    }
}