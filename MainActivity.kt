package com.example.rasmalkhazan

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var startDateBtn: Button
    private lateinit var endDateBtn: Button
    private lateinit var weightEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var taxEditText: EditText
    private lateinit var calculateBtn: Button
    private lateinit var resultText: TextView

    private var startDate: Calendar? = null
    private var endDate: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startDateBtn = findViewById(R.id.startDateBtn)
        endDateBtn = findViewById(R.id.endDateBtn)
        weightEditText = findViewById(R.id.weightEditText)
        priceEditText = findViewById(R.id.priceEditText)
        taxEditText = findViewById(R.id.taxEditText)
        calculateBtn = findViewById(R.id.calculateBtn)
        resultText = findViewById(R.id.resultText)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        startDateBtn.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                startDate = Calendar.getInstance().apply { set(year, month, day) }
                startDateBtn.text = dateFormat.format(startDate!!.time)
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        endDateBtn.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                endDate = Calendar.getInstance().apply { set(year, month, day) }
                endDateBtn.text = dateFormat.format(endDate!!.time)
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        calculateBtn.setOnClickListener {
            if (startDate == null || endDate == null ||
                weightEditText.text.isEmpty() ||
                priceEditText.text.isEmpty() ||
                taxEditText.text.isEmpty()
            ) {
                Toast.makeText(this, "يرجى إدخال جميع البيانات", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val weight = weightEditText.text.toString().toDoubleOrNull() ?: 0.0
            val pricePerKg = priceEditText.text.toString().toDoubleOrNull() ?: 0.0
            val taxRate = taxEditText.text.toString().toDoubleOrNull() ?: 0.0

            val diffDays = ((endDate!!.timeInMillis - startDate!!.timeInMillis) / (1000 * 60 * 60 * 24)).toInt()
            val paidDays = (diffDays - 5).coerceAtLeast(0)
            val weeks = Math.ceil(paidDays / 7.0).toInt()
            val effectiveWeight = if (weight > 100) 100 else weight.toInt()
            val baseCost = effectiveWeight * pricePerKg * weeks
            val tax = baseCost * (taxRate / 100)
            val total = baseCost + tax

            resultText.text = """
                ✅ الأيام المدفوعة: $paidDays
                ✅ عدد الأسابيع: $weeks
                💰 السعر قبل الضريبة: ${baseCost.toInt()} د.ع
                💸 الضريبة (${taxRate.toInt()}%): ${tax.toInt()} د.ع
                🧾 المجموع الكلي: ${total.toInt()} د.ع
            """.trimIndent()
        }
    }
}