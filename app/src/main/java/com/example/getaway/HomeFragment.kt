package com.example.getaway

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide

class HomeFragment : Fragment() {

    private lateinit var imageViewRandom: ImageView
    private lateinit var buttonMyGroups: Button
    private lateinit var buttonNewGroup: Button

    // List of picture names
    private val randomPictures = arrayOf(
         "passport.jpeg", "paris.jpeg", "lantern.jpeg", "coast.jpeg", "mombasa2.jpeg", "church.jpeg"
        , "beachnights.jpeg", "Egypt.jpeg", "forest.jpeg", "hands.jpeg", "fort.jpeg"
    )
    private var currentPictureIndex = 0

    // Handler to schedule periodic updates
    private val handler = Handler(Looper.getMainLooper())

    // Runnable to change picture after a delay
    private val changePictureRunnable = object : Runnable {
        override fun run() {
            loadRandomPicture()
            handler.postDelayed(this, 5 * 1000) // Schedule the same runnable after 30 seconds
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        imageViewRandom = view.findViewById(R.id.imageViewRandom)
        buttonMyGroups = view.findViewById(R.id.buttonMyGroups)
        buttonNewGroup = view.findViewById(R.id.buttonNewGroup)

        // Set initial picture
        loadRandomPicture()

        // Start the handler to change pictures every 30 seconds
        handler.postDelayed(changePictureRunnable, 5 * 1000)

        buttonMyGroups.setOnClickListener {
            // Handle My Groups button click
            // Open MyGroupsActivity or perform desired action
        }

        buttonNewGroup.setOnClickListener {
            // Handle New Group button click
            val intent = Intent(activity, NewGroupActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun loadRandomPicture() {
        // Get a random picture name from the list
        val randomIndex = (0 until randomPictures.size).random()
        val pictureName = randomPictures[randomIndex]

        // Construct the image path
        val imagePath = "file:///android_asset/$pictureName"

        // Load the image using Glide
        Glide.with(this).load(imagePath).into(imageViewRandom)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove the callback to prevent memory leaks
        handler.removeCallbacks(changePictureRunnable)
    }
}
