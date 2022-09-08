package com.alien.location

import android.Manifest
import android.Manifest.permission.READ_CONTACTS
import android.R
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexstyl.contactstore.ContactStore
import com.alien.location.databinding.ActivityContactsBinding

class ContactsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactsBinding

    private lateinit var contactAdapter: ContactAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)




        val store = ContactStore.newInstance(application)

        store.fetchContacts()
            .collect { contacts ->
                Log.d("alien","$contacts")


            }


    }




}
