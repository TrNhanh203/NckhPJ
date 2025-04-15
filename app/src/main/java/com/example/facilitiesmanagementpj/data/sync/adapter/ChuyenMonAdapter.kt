package com.example.facilitiesmanagementpj.data.sync.adapter

import com.example.facilitiesmanagementpj.data.dao.ChuyenMonDao
import com.example.facilitiesmanagementpj.data.entity.ChuyenMon
import com.example.facilitiesmanagementpj.data.sync.SyncableDao

class ChuyenMonAdapter(private val dao: ChuyenMonDao) : SyncableDao<ChuyenMon> {
    override suspend fun insertAll(items: List<ChuyenMon>) {
        items.forEach { dao.insert(it) }
    }
}