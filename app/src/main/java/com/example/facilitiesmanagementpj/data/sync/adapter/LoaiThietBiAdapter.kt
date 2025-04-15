package com.example.facilitiesmanagementpj.data.sync.adapter

import com.example.facilitiesmanagementpj.data.dao.LoaiThietBiDao
import com.example.facilitiesmanagementpj.data.entity.LoaiThietBi
import com.example.facilitiesmanagementpj.data.sync.SyncableDao

class LoaiThietBiAdapter(private val dao: LoaiThietBiDao) : SyncableDao<LoaiThietBi> {
    override suspend fun insertAll(items: List<LoaiThietBi>) {
        items.forEach { dao.insert(it) }
    }
}