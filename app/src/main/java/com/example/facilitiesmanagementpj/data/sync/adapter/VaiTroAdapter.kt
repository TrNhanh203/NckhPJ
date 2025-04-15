package com.example.facilitiesmanagementpj.data.sync.adapter

import com.example.facilitiesmanagementpj.data.dao.VaiTroDao
import com.example.facilitiesmanagementpj.data.entity.VaiTro
import com.example.facilitiesmanagementpj.data.sync.SyncableDao

class VaiTroAdapter(private val dao: VaiTroDao) : SyncableDao<VaiTro> {
    override suspend fun insertAll(items: List<VaiTro>) {
        items.forEach { dao.insert(it) }
    }
}