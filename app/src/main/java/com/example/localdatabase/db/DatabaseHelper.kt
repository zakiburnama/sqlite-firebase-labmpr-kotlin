package com.example.localdatabase.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.localdatabase.db.DatabaseContract.HomeworkColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "dbhomework"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_HOMEWORK = "CREATE TABLE $TABLE_NAME" +
                "(${DatabaseContract.HomeworkColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${DatabaseContract.HomeworkColumns.TITLE} TEXT NOT NULL," +
                "${DatabaseContract.HomeworkColumns.DESCRIPTION} TEXT NOT NULL," +
                "${DatabaseContract.HomeworkColumns.DATE} TEXT NOT NULL)"
    }

    override fun onCreate(p0: SQLiteDatabase) {
        p0.execSQL(SQL_CREATE_TABLE_HOMEWORK)
    }

    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {
        p0.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(p0)
    }
}