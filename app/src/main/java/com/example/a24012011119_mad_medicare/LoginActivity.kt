package com.example.a24012011119_mad_medicare



import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtPassword: TextInputEditText
    private lateinit var btnLogin: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {

            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (email.isEmpty()) {

                edtEmail.error = "Enter email"

            } else if (password.isEmpty()) {

                edtPassword.error = "Enter password"

            } else if (email == "user@gmail.com" && password == "123456") {

                Toast.makeText(
                    this,
                    "Login Successful",
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(
                    this,
                    MainActivity::class.java
                )

                startActivity(intent)
                finish()

            } else {

                Toast.makeText(
                    this,
                    "Invalid Email or Password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}