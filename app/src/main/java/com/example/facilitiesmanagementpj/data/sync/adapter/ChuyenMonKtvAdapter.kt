package com.example.facilitiesmanagementpj.data.sync.adapter

import com.example.facilitiesmanagementpj.data.dao.ChuyenMonKtvDao
import com.example.facilitiesmanagementpj.data.entity.ChuyenMonKtv
import com.example.facilitiesmanagementpj.data.sync.SyncableDao

class ChuyenMonKtvAdapter(private val dao: ChuyenMonKtvDao) : SyncableDao<ChuyenMonKtv> {
    override suspend fun insertAll(items: List<ChuyenMonKtv>) {
        items.forEach { dao.insert(it) }
    }
}