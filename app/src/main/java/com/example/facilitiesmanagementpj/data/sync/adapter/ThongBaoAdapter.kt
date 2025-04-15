package com.example.facilitiesmanagementpj.data.sync.adapter

import com.example.facilitiesmanagementpj.data.dao.ThongBaoDao
import com.example.facilitiesmanagementpj.data.entity.ThongBao
import com.example.facilitiesmanagementpj.data.sync.SyncableDao
import kotlin.collections.forEach

class ThongBaoAdapter(private val dao: ThongBaoDao) : SyncableDao<ThongBao> {
    override suspend fun insertAll(items: List<ThongBao>) {
        items.forEach { dao.insert(it) }
    }
}