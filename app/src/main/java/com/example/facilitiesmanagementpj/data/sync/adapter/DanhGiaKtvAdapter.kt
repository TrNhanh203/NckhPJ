package com.example.facilitiesmanagementpj.data.sync.adapter

import com.example.facilitiesmanagementpj.data.dao.DanhGiaKTVDao
import com.example.facilitiesmanagementpj.data.entity.DanhGiaKTV
import com.example.facilitiesmanagementpj.data.sync.SyncableDao

class DanhGiaKtvAdapter(private val dao: DanhGiaKTVDao) : SyncableDao<DanhGiaKTV> {
    override suspend fun insertAll(items: List<DanhGiaKTV>) {
        items.forEach { dao.insert(it) }
    }
}