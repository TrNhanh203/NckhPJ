package com.example.facilitiesmanagementpj.data.sync.adapter

import com.example.facilitiesmanagementpj.data.dao.ThietBiDao
import com.example.facilitiesmanagementpj.data.entity.ThietBi
import com.example.facilitiesmanagementpj.data.sync.SyncableDao

class ThietBiAdapter(private val dao: ThietBiDao) : SyncableDao<ThietBi> {
    override suspend fun insertAll(items: List<ThietBi>) {
        items.forEach { dao.insert(it) }
    }
}