package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//package com.example.myapplication
//import android.os.Bundle
//import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity

class ChatbotActivity : AppCompatActivity() {

    private lateinit var chatInputEditText: EditText
    private lateinit var sendChatButton: Button
    private lateinit var chatbotTextView: TextView
    private lateinit var chatbotScrollView: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot)

        chatInputEditText = findViewById(R.id.chatInputEditText)
        sendChatButton = findViewById(R.id.sendChatButton)
        chatbotTextView = findViewById(R.id.chatbotTextView)
        chatbotScrollView = findViewById(R.id.chatbotScrollView)

        sendChatButton.setOnClickListener {
            val userMessage = chatInputEditText.text.toString()
            val chatbotResponse = getChatbotResponse(userMessage)

            // Append the user message and chatbot response to the conversation view
            val currentConversation = chatbotTextView.text.toString()
            val newConversation = "$currentConversation\nYou: $userMessage\nBot: $chatbotResponse"
            chatbotTextView.text = newConversation

            // Clear the input field
            chatInputEditText.text.clear()

            // Scroll to the bottom of the chat
            chatbotScrollView.post {
                chatbotScrollView.fullScroll(ScrollView.FOCUS_DOWN)
            }
        }
    }

    private fun getChatbotResponse(userMessage: String): String {
        // Replace this with your chatbot logic or API call to get responses.
        // For now, let's provide some simple responses.
        when (userMessage.toLowerCase()) {
            "hello" -> return "Hello! How can I help you?"
             "help" -> return "Sure, I can assist you. Please ask your question."
            "emergency" -> return "If you are in an emergency situation, please call 911 immediately."
            "safety tips" -> return "Here are some safety tips for women:\n1. Be aware of your surroundings.\n2. Avoid poorly lit or isolated areas at night.\n3. Trust your instincts.\n4. Keep your phone and keys accessible.\n5. Share your location with someone you trust when going out.\n6. Learn self-defense techniques.\n7. Use rideshare apps with safety features."
            "report harassment" -> return "If you experience harassment, consider reporting it to the authorities or a trusted organization. Your safety is important."
            "emergency contacts" -> return "Here are some important emergency contacts:\n1. Police: 100\n2. Women's Helpline: 7827170170\n3. Domestic Violence Hotline: 8793088814\n4. Medical Emergency: 102"
            else -> return "I'm sorry, I didn't understand that. If you have a specific question or need assistance, please let me know."
        }
    }
}