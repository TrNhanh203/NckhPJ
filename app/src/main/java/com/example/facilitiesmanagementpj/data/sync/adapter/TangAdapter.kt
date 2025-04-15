package com.example.facilitiesmanagementpj.data.sync.adapter

import com.example.facilitiesmanagementpj.data.dao.TangDao
import com.example.facilitiesmanagementpj.data.entity.Tang
import com.example.facilitiesmanagementpj.data.sync.SyncableDao

class TangAdapter(private val dao: TangDao) : SyncableDao<Tang> {
    override suspend fun insertAll(items: List<Tang>) {
        items.forEach { dao.insert(it) }
    }
}