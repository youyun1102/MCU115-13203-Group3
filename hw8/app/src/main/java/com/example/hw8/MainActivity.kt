package com.example.hw8

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val items: ArrayList<String> = ArrayList()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var dbrw: SQLiteDatabase

    private lateinit var edBrand: EditText
    private lateinit var edModel: EditText
    private lateinit var edYear: EditText
    private lateinit var edPrice: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edBrand = findViewById(R.id.ed_brand)
        edModel = findViewById(R.id.ed_model)
        edYear = findViewById(R.id.ed_year)
        edPrice = findViewById(R.id.ed_price)
        
        val btnInsert = findViewById<Button>(R.id.btn_insert)
        val btnUpdate = findViewById<Button>(R.id.btn_update)
        val btnDelete = findViewById<Button>(R.id.btn_delete)
        val btnQuery = findViewById<Button>(R.id.btn_query)
        val listView = findViewById<ListView>(R.id.listView)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        listView.adapter = adapter

        dbrw = MyDBHelper(this).writableDatabase

        btnInsert.setOnClickListener {
            val brand = edBrand.text.toString().trim()
            val model = edModel.text.toString().trim()
            val year = edYear.text.toString().trim()
            val price = edPrice.text.toString().trim()
            if (brand.isEmpty() || model.isEmpty() || year.isEmpty() || price.isEmpty()) {
                showToast("欄位請勿留空")
            } else {
                try {
                    dbrw.execSQL("INSERT INTO myTable(brand, model, year, price) VALUES(?,?,?,?)", 
                        arrayOf(brand, model, year, price))
                    showToast("新增成功")
                    cleanEditTexts()
                    refreshData()
                } catch (e: Exception) {
                    showToast("新增失敗:${e.message}")
                }
            }
        }

        btnUpdate.setOnClickListener {
            val brand = edBrand.text.toString().trim()
            val model = edModel.text.toString().trim()
            val year = edYear.text.toString().trim()
            val price = edPrice.text.toString().trim()
            if (brand.isEmpty() || model.isEmpty() || year.isEmpty() || price.isEmpty()) {
                showToast("欄位請勿留空")
            } else {
                try {
                    dbrw.execSQL("UPDATE myTable SET model = ?, year = ?, price = ? WHERE brand = ?", 
                        arrayOf(model, year, price, brand))
                    showToast("更新成功")
                    cleanEditTexts()
                    refreshData()
                } catch (e: Exception) {
                    showToast("更新失敗:${e.message}")
                }
            }
        }

        btnDelete.setOnClickListener {
            val brand = edBrand.text.toString().trim()
            if (brand.isEmpty()) {
                showToast("請輸入廠牌")
            } else {
                try {
                    dbrw.execSQL("DELETE FROM myTable WHERE brand = ?", arrayOf(brand))
                    showToast("刪除成功")
                    cleanEditTexts()
                    refreshData()
                } catch (e: Exception) {
                    showToast("刪除失敗:${e.message}")
                }
            }
        }

        btnQuery.setOnClickListener {
            refreshData(edBrand.text.toString().trim())
        }
        
        // 初始載入時顯示所有資料
        refreshData()
    }
    
    private fun refreshData(brandFilter: String = "") {
        val queryString = if (brandFilter.isEmpty()) {
            "SELECT * FROM myTable"
        } else {
            "SELECT * FROM myTable WHERE brand = ?"
        }
        
        try {
            val c = if (brandFilter.isEmpty()) {
                dbrw.rawQuery(queryString, null)
            } else {
                dbrw.rawQuery(queryString, arrayOf(brandFilter))
            }
            
            items.clear()
            showToast("共有${c.count}筆資料")
            
            if (c.moveToFirst()) {
                do {
                    items.add("廠牌:${c.getString(0)}\n型號:${c.getString(1)} 年份:${c.getString(2)} 價格:${c.getString(3)}")
                } while (c.moveToNext())
            }
            adapter.notifyDataSetChanged()
            c.close()
        } catch (e: Exception) {
            showToast("查詢失敗:${e.message}")
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun cleanEditTexts() {
        edBrand.setText("")
        edModel.setText("")
        edYear.setText("")
        edPrice.setText("")
    }

    override fun onDestroy() {
        super.onDestroy()
        dbrw.close()
    }
}