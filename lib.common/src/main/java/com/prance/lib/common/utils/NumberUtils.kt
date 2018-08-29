package com.prance.lib.common.utils

/**
 * 不够位数的在前面补0，保留num的长度位数字
 * @param code
 * @return
 */
fun autoGenericCode(code: String, num: Int): String {
    var result = "";
    // 保留num的位数
    // 0 代表前面补充0
    // num 代表长度为4
    // d 代表参数为正数型
    result = String.format("%0" + num + "d", Integer.parseInt(code) + 1)
    return result
}