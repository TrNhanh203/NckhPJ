package com.example.facilitiesmanagementpj.data.sync.adapter

import com.example.facilitiesmanagementpj.data.dao.KyThuatVienDao
import com.example.facilitiesmanagementpj.data.entity.KyThuatVien
import com.example.facilitiesmanagementpj.data.sync.SyncableDao

class KyThuatVienAdapter(private val dao: KyThuatVienDao) : SyncableDao<KyThuatVien> {
    override suspend fun insertAll(items: List<KyThuatVien>) {
        items.forEach { dao.insert(it) }
    }
}