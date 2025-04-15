package com.example.facilitiesmanagementpj.data.sync.adapter

import com.example.facilitiesmanagementpj.data.dao.BaiVietDao
import com.example.facilitiesmanagementpj.data.entity.BaiViet
import com.example.facilitiesmanagementpj.data.sync.SyncableDao

class BaiVietAdapter(private val dao: BaiVietDao) : SyncableDao<BaiViet> {
    override suspend fun insertAll(items: List<BaiViet>) {
        items.forEach { dao.insert(it) }
    }
}