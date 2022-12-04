package com.example.localdatabase

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.localdatabase.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var database: DatabaseReference

//    private val listHomework = ArrayList<Homework>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Homework"

        database = Firebase.database.reference.child("homework")

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddNewHomework::class.java)
            startActivity(intent)
        }

        getAllData()

    }

    private fun getAllData() {
        val listHomework = ArrayList<Homework>()

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (h : DataSnapshot in snapshot.children) {
                        val hw = h.getValue(Homework::class.java)
                        if (hw != null) {
                            listHomework.add(hw)
                        }
                    }
                    showRecycler(listHomework)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun showRecycler(listHomework: ArrayList<Homework>) {
        if (applicationContext.resources.configuration.orientation == Configuration
                .ORIENTATION_LANDSCAPE) {
            binding.rvHomework.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvHomework.layoutManager = LinearLayoutManager(this)
        }

        val adapter = HomeworkAdapter2(listHomework)
        binding.rvHomework.adapter = adapter

        adapter.setOnItemClickCallback(object : HomeworkAdapter2.OnItemClickCallback {
            override fun onItemClicked(data: Homework) {
                val intent = Intent(this@MainActivity2,
                    AddNewHomework::class.java)
                intent.putExtra(AddNewHomework.EXTRA_HOMEWORK_TITLE, data.title)
                intent.putExtra(AddNewHomework.EXTRA_HOMEWORK_DESCRIPTION, data.description)
                startActivity(intent)
                Toast.makeText(this@MainActivity2, "INI ANIME ${data.title}", Toast.LENGTH_SHORT).show()
            }
        })

    }
}