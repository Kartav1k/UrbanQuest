package com.example.urbanquest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.urbanquest.R
import com.example.urbanquest.ui.state_holder.ProductViewModel

class ScreenOfList : Fragment() {
    private lateinit var productViewModel: ProductViewModel
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_screen_of_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recView)
        productAdapter = ProductAdapter(emptyList())
        recyclerView.adapter = productAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        productViewModel.getAllProducts().observe(viewLifecycleOwner, Observer { products ->
            productAdapter.setData(products)
        })
    }
}
