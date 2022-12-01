package com.example.localdatabase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.localdatabase.databinding.ActivityMainBinding
import com.example.localdatabase.db.HomeworkHelper
import com.example.localdatabase.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: HomeworkAdapter

    val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.data != null) {
            when (result.resultCode) {
                AddNewHomework.RESULT_ADD -> {
                    val homework =
                        result.data?.getParcelableExtra<Homework>(AddNewHomework.
                        EXTRA_HOMEWORK) as Homework
                    adapter.addItem(homework)
                    binding.rvHomework.smoothScrollToPosition(adapter.itemCount - 1)
                    showSnackbarMessage("Data berhasil ditambahkan")
                }
                AddNewHomework.RESULT_UPDATE -> {
                    val homework =
                        result.data?.getParcelableExtra<Homework>(AddNewHomework.
                        EXTRA_HOMEWORK) as Homework
                    val position =
                        result?.data?.getIntExtra(AddNewHomework.EXTRA_POSITION, 0)
                                as Int
                    adapter.updateItem(position, homework)
                    binding.rvHomework.smoothScrollToPosition(position)
                    showSnackbarMessage("Data berhasil diubah")
                }
                AddNewHomework.RESULT_DELETE -> {
                    val position =
                        result?.data?.getIntExtra(AddNewHomework.EXTRA_POSITION, 0)
                                as Int
                    adapter.removeItem(position)
                    showSnackbarMessage("Data berhasil dihapus")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Homework"
        binding.rvHomework.layoutManager = LinearLayoutManager(this)
        binding.rvHomework.setHasFixedSize(true)

        adapter = HomeworkAdapter(object : HomeworkAdapter.OnItemClickCallback {
            override fun onItemClicked(selectedHomework: Homework?, position: Int?) {
                val intent =
                    Intent(this@MainActivity, AddNewHomework::class.java)
                intent.putExtra(AddNewHomework.EXTRA_HOMEWORK, selectedHomework)
                intent.putExtra(AddNewHomework.EXTRA_POSITION, position)
                resultLauncher.launch(intent)
            }
        })
        binding.rvHomework.adapter = adapter

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddNewHomework::class.java)
            resultLauncher.launch(intent)
        }

        if (savedInstanceState == null) {
            loadHomeworkAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Homework>(EXTRA_STATE)
            if (list != null)
                adapter.listHomework = list
        }
    }

    private fun loadHomeworkAsync() {
        lifecycleScope.launch {
            val homeworkHelper = HomeworkHelper.getInstance(applicationContext)
            homeworkHelper.open()
            val deferredHomework = async(Dispatchers.IO) {
                val cursor = homeworkHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val homework = deferredHomework.await()
            if (homework.size > 0) {
                adapter.listHomework = homework
            } else {
                adapter.listHomework = ArrayList()
                showSnackbarMessage("Data tidak ada")
            }
            homeworkHelper.close()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listHomework)
    }


    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.rvHomework, message, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }
}