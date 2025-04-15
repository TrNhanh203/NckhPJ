package com.example.facilitiesmanagementpj.data.sync.adapter

import com.example.facilitiesmanagementpj.data.dao.DayDao
import com.example.facilitiesmanagementpj.data.entity.Day
import com.example.facilitiesmanagementpj.data.sync.SyncableDao

class DayAdapter(private val dao: DayDao) : SyncableDao<Day> {
    override suspend fun insertAll(items: List<Day>) {
        items.forEach { dao.insert(it) }
    }
}
