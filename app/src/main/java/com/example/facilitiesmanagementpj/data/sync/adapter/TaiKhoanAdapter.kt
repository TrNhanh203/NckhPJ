package com.example.facilitiesmanagementpj.data.sync.adapter

import com.example.facilitiesmanagementpj.data.dao.TaiKhoanDao
import com.example.facilitiesmanagementpj.data.entity.TaiKhoan
import com.example.facilitiesmanagementpj.data.sync.SyncableDao

class TaiKhoanAdapter(private val dao: TaiKhoanDao) : SyncableDao<TaiKhoan> {
    override suspend fun insertAll(items: List<TaiKhoan>) {
        items.forEach { dao.insert(it) }
    }
}