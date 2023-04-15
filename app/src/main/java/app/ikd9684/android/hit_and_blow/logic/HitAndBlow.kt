package app.ikd9684.android.hit_and_blow.logic

import android.util.Log

class HitAndBlow(val digits: Int) {

    data class Result(val hit: Int, val blow: Int)

    companion object {
        private const val TAG = "HitAndBlow"
    }

    private var listOfNumbers = List(digits) { 0 }

    fun initRandom() {
        val numbers = (0..9).shuffled().take(digits)
        initWithNumbers(numbers)
    }

    fun initWithNumbers(numbers: List<Int>) {
        listOfNumbers = List(digits) {
            numbers[it]
        }
        Log.d(TAG, "initWithNumbers: numbers=$numbers")
    }

    fun deduce(estimatedNumbers: List<Int>): Result {
        if (estimatedNumbers.size != listOfNumbers.size) {
            throw IllegalArgumentException("specify ${listOfNumbers.size} estimatedNumbers: $estimatedNumbers")
        }

        var hit = 0
        var blow = 0

        for ((i, n) in listOfNumbers.withIndex()) {
            if (estimatedNumbers[i] == n) {
                ++hit
            } else if (estimatedNumbers.contains(n)) {
                ++blow
            }
        }

        Log.d(TAG, "deduce: $listOfNumbers <- estimated=$estimatedNumbers =>(hit=$hit, low=$blow)")
        return Result(hit, blow)
    }
}
