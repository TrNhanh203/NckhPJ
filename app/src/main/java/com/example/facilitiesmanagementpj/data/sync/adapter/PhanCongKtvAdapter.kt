package com.example.facilitiesmanagementpj.data.sync.adapter

import com.example.facilitiesmanagementpj.data.dao.PhanCongKtvDao
import com.example.facilitiesmanagementpj.data.entity.PhanCongKtv
import com.example.facilitiesmanagementpj.data.sync.SyncableDao

class PhanCongKtvAdapter(private val dao: PhanCongKtvDao) : SyncableDao<PhanCongKtv> {
    override suspend fun insertAll(items: List<PhanCongKtv>) {
        items.forEach { dao.insert(it) }
    }
}