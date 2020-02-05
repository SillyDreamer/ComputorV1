package com.example.computorv1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.VISIBLE
import kotlinx.android.synthetic.main.activity_main.*
import android.view.View.GONE


class MainActivity : AppCompatActivity() {

    var checkPos = ""
    var dis = 0.0
    var degree : Int = 0
    var coeff_x1_0 : Double = 0.0
    var coeff_x1_1 : Double = 0.0
    var coeff_x1_2 : Double = 0.0
    var coeff_x2_0 : Double = 0.0
    var coeff_x2_1 : Double = 0.0
    var coeff_x2_2 : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        go.setOnClickListener {
            preferences()
            var equation = edit_text.text.toString()
            equation = equation.replace(" ", "")
            check_degree(equation)
            if (degree in 0..2) {
                parse_coeff_left(equation)
                parse_coeff_right(equation)
                reduced_form()
                solved()
            }
            else {

            }
        }
    }

    private fun preferences() {
        dis_tv.text = ""
        dis_tv.visibility = GONE
        degree_tv.text = ""
        reduced.text = ""
        result.text = ""
        degree = 0
        checkPos = ""
        dis = 0.0

        coeff_x1_2 = 0.0
        coeff_x1_1 = 0.0
        coeff_x1_0 = 0.0
        coeff_x2_1 = 0.0
        coeff_x2_2 = 0.0
        coeff_x2_0 = 0.0
    }


    private fun check_degree(equation : String) : Int {
        var len = 0
        var testlen : Int
        var testStr  = ""
        equation.toCharArray()
        for (i in equation) {
            if (i == 'x' || i == 'X') {
                testlen = len + 2
                while(testlen < equation.length && equation[testlen].isDigit()) {
                    testStr += equation[testlen]
                    testlen++
                }
                if (degree < testStr.toInt()) {
                    degree = testStr.toInt()
                    println("degree = $degree")
                }
                testStr = ""
            }
            len++
        }
        degree_tv.text = "степень уравнения = $degree"
        return degree
    }

    private fun parse_coeff_left(equation: String) {
        var len = 0
        var count = 0
        var coeffStr = ""

        while (len < equation.length) {
            if (equation[len] == '=')
                break
            while ((len + count) < equation.length && (equation[len+count].isDigit() || equation[len+count] == '.')) {
                if(count == 0 && (len + count -1 >= 0) && (equation[len + count - 1] == '-'))
                    coeffStr += '-'
                coeffStr += equation[len+count]
                count++
            }
            if ((len + count - 1 >= 0 ) && (equation[len + count -1] == 'x' || equation[len + count - 1] == 'X')) {
                len += count - 1
                if (equation[len+2] == '0') {
                    checkPos += '0'
                    if (coeffStr.isNotEmpty())
                        coeff_x1_0 = coeffStr.toDouble()
                    else
                        coeff_x1_0 = 1.0
                    coeffStr = ""
                    count = 0
                    len += 2
                }
                else if (equation[len+2] == '1') {
                    checkPos += '1'
                    if (coeffStr.isNotEmpty())
                        coeff_x1_1 = coeffStr.toDouble()
                    else
                        coeff_x1_1 = 1.0
                    coeffStr = ""
                    count = 0
                    len += 2
                }
                else if (equation[len+2] == '2') {
                    println("ya tyt i str = $coeffStr")
                    checkPos += '2'
                    if (coeffStr.isNotEmpty())
                        if (coeffStr == "-")
                            coeff_x1_2 = -1.0
                        else
                            coeff_x1_2 = coeffStr.toDouble()
                    else
                        coeff_x1_2 = 1.0
                    coeffStr = ""
                    count = 0
                    len += 2
                }
            }
            len++
        }
        println("coeff1_0 = $coeff_x1_0 coeff1_1 = $coeff_x1_1 coeff1_2 == $coeff_x1_2")
    }

    private fun parse_coeff_right(equation: String) {
        var len = 0
        var count = 0
        var coeffStr = ""

        while (len < equation.length && equation[len] != '=')
            len++
        while (len < equation.length) {
            while ((len + count) < equation.length && (equation[len+count].isDigit() || equation[len+count] == '.')) {
                if(count == 0 && (len + count -1 >= 0) && (equation[len + count - 1] == '-'))
                    coeffStr += '-'
                coeffStr += equation[len+count]
                count++
            }
            if ((len + count - 1 >= 0 ) && (equation[len + count -1] == 'x' || equation[len + count - 1] == 'X')) {
                len += count - 1
                if (equation[len+2] == '0') {
                    if (!checkPos.contains('0'))
                        checkPos += '0'
                    if (coeffStr.isNotEmpty())
                        coeff_x2_0 = coeffStr.toDouble()
                    else
                        coeff_x2_0 = 1.0
                    coeffStr = ""
                    count = 0
                    len += 2
                }
                else if (equation[len+2] == '1') {
                    if (!checkPos.contains('1'))
                        checkPos += '1'
                    if (coeffStr.isNotEmpty())
                        coeff_x2_1 = coeffStr.toDouble()
                    else
                        coeff_x2_1 = 1.0
                    coeffStr = ""
                    count = 0
                    len += 2
                }
                else if (equation[len+2] == '2') {
                    if (!checkPos.contains('2'))
                        checkPos += '2'
                    if (coeffStr.isNotEmpty())
                        coeff_x2_2 = coeffStr.toDouble()
                    else
                        coeff_x2_2 = 1.0
                    coeffStr = ""
                    count = 0
                    len += 2
                }
            }
            len++
        }
        println("coeff2_0 = $coeff_x2_0 coeff2_1 = $coeff_x2_1 coeff2_2 == $coeff_x2_2")

    }

    private fun reduced_form() {
        var count = 0
        var reduced_str = ""
        var intDigit = 0
        reduced.setText(reduced_str)
        for (i in checkPos) {
            count += 1
            if (i == '0') {
                coeff_x1_0 -= coeff_x2_0
                if (coeff_x1_0 == 0.0) {
                    if (count == 1)
                        reduced_str += "x^0 "
                    else
                        reduced_str += "x^0 "
                }
                else {
                    if ((coeff_x1_0 - coeff_x1_0.toInt()) == 0.0)
                        intDigit = coeff_x1_0.toInt()
                    if (intDigit != 0) {
                        if (count == 1 || intDigit < 0)
                            reduced_str += "${intDigit}x^0 "
                        else {
                            reduced_str += "+${intDigit}x^0 "
                        }
                    } else {
                        if (count == 1 || coeff_x1_0 < 0)
                            reduced_str += "${coeff_x1_0}x^0 "
                        else {
                            reduced_str += "+${coeff_x1_0}x^0 "
                        }
                    }
                }
                intDigit = 0
            }
            else if (i == '1') {
                coeff_x1_1 -= coeff_x2_1
                if (coeff_x1_1 == 0.0) {
                    if (count == 1)
                        reduced_str += "x^1 "
                    else
                        reduced_str += "x^1 "
                }
                else {
                    if ((coeff_x1_1 - coeff_x1_1.toInt()) == 0.0)
                        intDigit = coeff_x1_1.toInt()
                    if (intDigit != 0) {
                        if (count == 1 || intDigit < 0)
                            reduced_str += "${intDigit}x^1 "
                        else {
                            reduced_str += "+ ${intDigit}x^1 "
                        }
                    } else {
                        if (count == 1 || coeff_x1_1 < 0)
                            reduced_str += "${coeff_x1_1}x^1 "
                        else {
                            reduced_str += "+ ${coeff_x1_1}x^1 "
                        }
                    }
                }
                intDigit = 0
            }
            else {
                coeff_x1_2 -= coeff_x2_2
                if (coeff_x1_2 == 0.0) {
                    if (count == 1)
                        reduced_str += "x^2 "
                    else
                        reduced_str += "x^2 "
                }
                else {
                    if ((coeff_x1_2 - coeff_x1_2.toInt()) == 0.0)
                        intDigit = coeff_x1_2.toInt()
                    if (intDigit != 0) {
                        if (count == 1 || intDigit < 0)
                            reduced_str += "${intDigit}x^2 "
                        else {
                            reduced_str += "+ ${intDigit}x^2 "
                        }
                    } else {
                        if (count == 1 || coeff_x1_2 < 0)
                            reduced_str += "${coeff_x1_2}x^2 "
                        else {
                            reduced_str += "+ ${coeff_x1_2}x^2 "
                        }
                    }
                }
                intDigit = 0
            }
        }

        reduced_str += "= 0"
        reduced.text = reduced_str

    }

    private fun solved() {
        if (degree == 0) {
            if (coeff_x1_0 == 0.0)
                result.text = "все числа подходят"
            else
                result.text = "нет решений"
        }

        else if (degree == 1) {
            if (coeff_x1_0.rem(coeff_x1_1) == 0.0) {
                val res = (-coeff_x1_0 / coeff_x1_1)
                val formattedDouble = String.format("ответ = %.2f", res)
                result.text = formattedDouble
            }
            else {
                val formattedDouble = String.format(" ответ = %.2f \nиррац.дробь: %.2f / %.2f", (-coeff_x1_0 / coeff_x1_1), -coeff_x1_0, coeff_x1_1)
                result.text = formattedDouble
            }
        }

        else if (degree == 2) {
            dis_tv.visibility = VISIBLE
            dis = (coeff_x1_1 * coeff_x1_1) - (4 * coeff_x1_2 * coeff_x1_0)
            if (dis < 0) {
                dis_tv.text = "Дискриминант отрицательный и равен $dis"
                result.text = "дискриминант отрицательный, решений нет"
            }
            else if (dis > 0) {
                dis_tv.text = "Дискриминант положительный и равен $dis"
                println("dis = $dis")
                if ((-1 * coeff_x1_1 - findSqrt(dis)).rem(2 * coeff_x1_2) == 0.0 ||
                    (-1 * coeff_x1_1 + findSqrt(dis)).rem(2 * coeff_x1_2) == 0.0) {
                    var x1 = (-1 * coeff_x1_1 - findSqrt(dis)) / 2 * coeff_x1_2
                    var x2 = (-1 * coeff_x1_1 + findSqrt(dis)) / 2 * coeff_x1_2
                    val formattedDouble = String.format("ответ x1 = %.2f и x2 = %.2f", x1, x2)
                    result.text = formattedDouble
                }
                else {
                    val formattedDouble = String.format("ответ x1 = %.2f и x2 = %.2f \n" +
                            "иррац.дробь: х1 = %.2f / %.2f и x2 = %.2f / %.2f", (-1 * coeff_x1_1 - findSqrt(dis)) / 2 * coeff_x1_2, (-1 * coeff_x1_1 + findSqrt(dis)) / 2 * coeff_x1_2,
                        (-1 * coeff_x1_1 - findSqrt(dis)), 2 * coeff_x1_2, (-1 * coeff_x1_1 + findSqrt(dis)), 2 * coeff_x1_2)
                    result.text = formattedDouble
                }
            }
            else if(dis == 0.0) {
                dis_tv.text = "Дискриминант равен 0"
                if (-coeff_x1_1.rem(2*coeff_x1_2) == 0.0) {
                    val res = (coeff_x1_1 * (-1)) / (2 * coeff_x1_2)
                    val formattedDouble = String.format("ответ = %.2f", res)
                    result.text = formattedDouble
                }
                else {
                    val formattedDouble = String.format("ответ = %.2f \n" +
                            "иррац.дроь: %.2f / %2.f", (coeff_x1_1 * (-1)) / (2 * coeff_x1_2), -coeff_x1_1, 2 * coeff_x1_2)
                    result.text = formattedDouble
                }
            }
        }
    }




    fun abs(x : Double) : Double {
        val res = if (x < 0) x * (-1) else x
        return res
    }

    fun square(
        n: Double,
        i: Double, j: Double
    ): Double {
        val mid = (i + j) / 2
        val mul = mid * mid

        return if (mul == n || abs(mul - n) < 0.00001)
            mid
        else if (mul < n)
            square(n, mid, j)
        else
            square(n, i, mid)
    }

    fun findSqrt(n: Double) : Double {
        var i = 1.0
        var res = 0.0

        var found = false
        while (!found) {
            if (i * i == n) {
                res = i
                found = true
            } else if (i * i > n) {

                res = square(n, i - 1, i)
                found = true

            }
            i++
        }
        return res
    }
}
