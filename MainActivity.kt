package com.example.healthappt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.SeekBar
import android.widget.Switch


class MainActivity : AppCompatActivity() {
    private lateinit var editTextText: EditText
    private lateinit var editTextText1: EditText
    private lateinit var switch: Switch
    private lateinit var editTextText3: EditText
    private lateinit var seekBar: SeekBar
    private lateinit var button: Button
    private lateinit var button1: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextText = findViewById(R.id.editTextText)
        editTextText1 = findViewById(R.id.editTextText1)
        switch = findViewById(R.id.switchGender)
        editTextText3 = findViewById(R.id.editTextText3)
        seekBar = findViewById(R.id.seekBarActivityLevel)

        button = findViewById(R.id.button)
        button1 = findViewById(R.id.button1)

        editTextText.hint = "кг"
        editTextText1.hint = "см"
        editTextText3.hint = ""

        switch.setOnCheckedChangeListener { _, isChecked ->
            val gender = if (isChecked) "1" else "0"
        }

        button.setOnClickListener {
            val weight = editTextText.text.toString()
            val height = editTextText1.text.toString()
            val sex = if (switch.isChecked) "1" else "0"
            val age = editTextText3.text.toString()
            val actLevel = seekBar.progress.toString()

            if (weight.isNotEmpty() && height.isNotEmpty() && age.isNotEmpty()) {
                // Проверка на наличие буквенных символов
                if (weight.toDoubleOrNull() != null && height.toDoubleOrNull() != null &&
                    age.toDoubleOrNull() != null
                ) {
                    val intent = Intent(this, SecondActivity::class.java)
                    intent.putExtra("weight", weight)
                    intent.putExtra("height", height)
                    intent.putExtra("sex", sex)
                    intent.putExtra("age", age)
                    intent.putExtra("actlevel", actLevel)
                    startActivity(intent)
                } else {
                    // Предупреждение о наличии букв в полях
                    Toast.makeText(this, "Поля должны содержать числовые значения", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Предупреждение о незаполненных полях
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }

        button1.setOnClickListener {
            editTextText.text.clear()
            editTextText1.text.clear()
            editTextText3.text.clear()
        }
        val seekBar = findViewById<SeekBar>(R.id.seekBarActivityLevel)
        val textView = findViewById<TextView>(R.id.textViewActivityLevel)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val level = progress + 1 // Уровень начинается с 1, а не с 0
                textView.text = "$level"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }
}

class SecondActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    private lateinit var button2: Button
    private lateinit var buttonCopy: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        textView = findViewById(R.id.textView)
        button2 = findViewById(R.id.button2)
        buttonCopy = findViewById(R.id.buttonCopy)

        val weight = intent.getStringExtra("weight")?.toDoubleOrNull() ?: 0.0
        val height = intent.getStringExtra("height")?.toDoubleOrNull() ?: 0.0
        val sex = intent.getStringExtra("sex")?.toDoubleOrNull() ?: 1.0 // значение по умолчанию - мужской пол
        val age = intent.getStringExtra("age")?.toDoubleOrNull() ?: 0.0
        val actlevel = intent.getStringExtra("actlevel")?.toDoubleOrNull() ?: 1.0 // уровень активности, значение по умолчанию 1.0

        val totalEnergyExpenditure: Double // расчет базового метаболизма
        val activityMultiplier = when (actlevel.toInt()) {
            0 -> 1.2
            1 -> 1.375
            2 -> 1.55
            3 -> 1.725
            4 -> 1.9
            else -> 1.9 // По умолчанию экстра уровень активности
        }

        if (sex == 1.0) { // Женщина
            totalEnergyExpenditure = ((10 * weight) + (6.25 * height) - (5 * age) - 161) * activityMultiplier
        } else { // Мужчина
            totalEnergyExpenditure = ((10 * weight) + (6.25 * height) - (5 * age) + 5) * activityMultiplier
        }

        val proteins: Double
        val fats = weight // Жиры одинаковы для обоих полов

        if (sex == 1.0) { // Женщина
            proteins = weight * 1.6
        } else { // Мужчина
            proteins = weight * 1.8
        }

        val carbohydrates = (totalEnergyExpenditure - (proteins * 4) - (fats * 9)) / 4

// Указанные значения витаминов и минералов приведены только для примера
        val vitaminA = 900.0 // рекомендуемый дневной прием витамина A в микрограммах
        val vitaminC = 90.0 // рекомендуемый дневной прием витамина C в миллиграммах
        val vitaminD = 600.0 // рекомендуемый дневной прием витамина D в МЕ (международных единицах)
        val vitaminE = 15.0 // рекомендуемый дневной прием витамина E в миллиграммах
        val vitaminK = 120.0 // рекомендуемый дневной прием витамина K в микрограммах

        val iron = 8.0 // рекомендуемый дневной прием железа в миллиграммах
        val calcium = 1000.0 // рекомендуемый дневной прием кальция в миллиграммах
        val magnesium = 400.0 // рекомендуемый дневной прием магния в миллиграммах
        val phosphorus = 700.0 // рекомендуемый дневной прием фосфора в миллиграммах
        val potassium = 4700.0 // рекомендуемый дневной прием калия в миллиграммах

        val result = """
        Рассчитанные данные:
        Дневное потребление: $totalEnergyExpenditure ккал
        Белки: $proteins г
        Жиры: $fats г
        Углеводы: $carbohydrates г
        
        Витамины:
        Витамин A: $vitaminA мкг
        Витамин C: $vitaminC мг
        Витамин D: $vitaminD МЕ
        Витамин E: $vitaminE мг
        Витамин K: $vitaminK мкг
        
        Минералы:
        Железо: $iron мг
        Кальций: $calcium мг
        Магний: $magnesium мг
        Фосфор: $phosphorus мг
        Калий: $potassium мг
    """.trimIndent()


        textView.text = result

        button2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        buttonCopy.setOnClickListener {
            // Копирование результата в буфер обмена
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("Result", result)
            clipboardManager.setPrimaryClip(clipData)

            Toast.makeText(this, "Результат скопирован в буфер обмена", Toast.LENGTH_SHORT).show()
        }
    }
}
