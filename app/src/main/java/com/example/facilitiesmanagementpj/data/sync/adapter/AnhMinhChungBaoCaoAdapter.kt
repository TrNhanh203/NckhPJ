package com.example.facilitiesmanagementpj.data.sync.adapter

import com.example.facilitiesmanagementpj.data.dao.AnhMinhChungBaoCaoDao
import com.example.facilitiesmanagementpj.data.entity.AnhMinhChungBaoCao
import com.example.facilitiesmanagementpj.data.sync.SyncableDao

class AnhMinhChungBaoCaoAdapter(private val dao: AnhMinhChungBaoCaoDao) : SyncableDao<AnhMinhChungBaoCao> {
    override suspend fun insertAll(items: List<AnhMinhChungBaoCao>) {
        items.forEach { dao.insert(it) }
    }
}