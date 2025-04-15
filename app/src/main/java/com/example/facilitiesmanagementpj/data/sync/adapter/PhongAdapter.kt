package com.example.facilitiesmanagementpj.data.sync.adapter

import com.example.facilitiesmanagementpj.data.dao.PhongDao
import com.example.facilitiesmanagementpj.data.entity.Phong
import com.example.facilitiesmanagementpj.data.sync.SyncableDao

class PhongAdapter(private val dao: PhongDao) : SyncableDao<Phong> {
    override suspend fun insertAll(items: List<Phong>) {
        items.forEach { dao.insert(it) }
    }
}