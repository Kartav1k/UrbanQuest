package com.example.urbanquest.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.urbanquest.R
import com.example.urbanquest.retrofit.api.ProductApi
import com.example.urbanquest.ui.state_holder.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@AndroidEntryPoint
class ScreenOfData : Fragment() {
    private val userViewModel: ProductViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_screen_of_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val errorMsg: TextView=view.findViewById(R.id.error)
        val editTextID: EditText=view.findViewById(R.id.editText)
        val butF5: Button=view.findViewById(R.id.f5)
        val butDown: Button=view.findViewById(R.id.find)
        val output: TextView=view.findViewById(R.id.outputData)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val productApi = retrofit.create(ProductApi::class.java)

        butDown.setOnClickListener() {
            val idField = editTextID.text.toString()
            if (idField.isNotEmpty()) {
                errorMsg.text = ""
                val id = idField.toInt()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val product = productApi.getProductById(id)
                        userViewModel.insertProduct(product)
                        withContext(Dispatchers.Main) {
                            output.text = (product.title + "\n" + product.description)
                            Log.d("Room", "Product inserted into the database: $product")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } else {
                errorMsg.text = "Ошибка, введите целое число!"
            }
        }

        butF5.setOnClickListener() {
            findNavController().navigate(R.id.action_screenOfData_to_screenOfList)
        }
    }
}
