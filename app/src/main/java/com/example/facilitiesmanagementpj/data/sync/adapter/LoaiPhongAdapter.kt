package com.example.facilitiesmanagementpj.data.sync.adapter

import com.example.facilitiesmanagementpj.data.dao.LoaiPhongDao
import com.example.facilitiesmanagementpj.data.entity.LoaiPhong
import com.example.facilitiesmanagementpj.data.sync.SyncableDao

class LoaiPhongAdapter(private val dao: LoaiPhongDao) : SyncableDao<LoaiPhong> {
    override suspend fun insertAll(items: List<LoaiPhong>) {
        items.forEach { dao.insert(it) }
    }
}