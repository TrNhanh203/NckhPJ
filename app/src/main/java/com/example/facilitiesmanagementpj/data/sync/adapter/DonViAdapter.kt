package com.example.facilitiesmanagementpj.data.sync.adapter

import com.example.facilitiesmanagementpj.data.dao.DonViDao
import com.example.facilitiesmanagementpj.data.entity.DonVi
import com.example.facilitiesmanagementpj.data.sync.SyncableDao

class DonViAdapter(private val dao: DonViDao) : SyncableDao<DonVi> {
    override suspend fun insertAll(items: List<DonVi>) {
        items.forEach { dao.insert(it) }
    }
}