package com.olsera.warungmakan.home

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.olsera.repository.model.Warung
import com.olsera.warungmakan.R
import com.olsera.warungmakan.databinding.ActivityHomeBinding
import com.olsera.warungmakan.home.adapter.WarungAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.recyclerview.widget.RecyclerView
import com.olsera.warungmakan.warungeditor.WarungEditorActivity


class HomeActivity : AppCompatActivity() {
    companion object {
        private const val REQ_CODE_EDITOR = 12
    }

    private val viewModel by viewModel<HomeViewModel>()
    private val dataList = arrayListOf<Warung>()
    private lateinit var adapter: WarungAdapter
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupObserver()
        viewModel.getList()
        viewModel.getWarungCount()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_CODE_EDITOR && resultCode == RESULT_OK) {
            viewModel.pageParam = 0
            viewModel.getList()
            viewModel.getWarungCount()
        }
    }

    private fun setupView() {
        val layoutManager = LinearLayoutManager(this)
        adapter = WarungAdapter(dataList) {
            val intent = WarungEditorActivity.createIntent(this, it.id)
            startActivityForResult(intent, REQ_CODE_EDITOR)
        }
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                    if (viewModel.isLoadingData.value != true && !viewModel.isReachBottom) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            viewModel.pageParam = viewModel.currentPage+1
                            viewModel.getList()
                        }
                    }
                }
            }
        })

        binding.radioFilter.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.buttonAllStatus -> {
                    viewModel.pageParam = 0
                    viewModel.isActiveParam = null
                    viewModel.getList()
                }
                R.id.buttonActive -> {
                    viewModel.pageParam = 0
                    viewModel.isActiveParam = true
                    viewModel.getList()
                }
                R.id.buttonInactive -> {
                    viewModel.pageParam = 0
                    viewModel.isActiveParam = false
                    viewModel.getList()
                }
            }
        }

        binding.buttonAddWarung.setOnClickListener {
            val intent = WarungEditorActivity.createIntent(this)
            startActivityForResult(intent, REQ_CODE_EDITOR)
        }
    }

    private fun setupObserver() {
        viewModel.initialList.observe(this, {
            dataList.clear()
            dataList.addAll(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.loadMoreList.observe(this, {
            dataList.addAll(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.warungCount.observe(this, {
            binding.buttonAllStatus.text = "All Status (${it.total})"
            binding.buttonActive.text = "Active (${it.active})"
            binding.buttonInactive.text = "Inactive (${it.inActive})"
        })

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Mengambil data ...")
        viewModel.isLoadingData.observe(this, {
            if (it) {
                progressDialog.show()
            } else {
                progressDialog.dismiss()
            }
        })
    }
}