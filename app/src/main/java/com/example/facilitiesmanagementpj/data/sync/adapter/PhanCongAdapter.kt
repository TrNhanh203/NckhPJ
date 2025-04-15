package com.example.facilitiesmanagementpj.data.sync.adapter

import com.example.facilitiesmanagementpj.data.dao.PhanCongDao
import com.example.facilitiesmanagementpj.data.entity.PhanCong
import com.example.facilitiesmanagementpj.data.sync.SyncableDao

class PhanCongAdapter(private val dao: PhanCongDao) : SyncableDao<PhanCong> {
    override suspend fun insertAll(items: List<PhanCong>) {
        items.forEach { dao.insert(it) }
    }
}