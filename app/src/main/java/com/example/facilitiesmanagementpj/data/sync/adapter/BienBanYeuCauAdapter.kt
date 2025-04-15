package com.example.facilitiesmanagementpj.data.sync.adapter

import com.example.facilitiesmanagementpj.data.dao.BienBanYeuCauDao
import com.example.facilitiesmanagementpj.data.entity.BienBanYeuCau
import com.example.facilitiesmanagementpj.data.sync.SyncableDao


class BienBanYeuCauAdapter(private val dao: BienBanYeuCauDao) : SyncableDao<BienBanYeuCau> {
    override suspend fun insertAll(items: List<BienBanYeuCau>) {
        items.forEach { dao.insert(it) }
    }
}