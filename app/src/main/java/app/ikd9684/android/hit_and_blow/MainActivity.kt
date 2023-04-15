package app.ikd9684.android.hit_and_blow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.ikd9684.android.hit_and_blow.logic.HitAndBlow
import app.ikd9684.android.hitandblow.R
import app.ikd9684.android.hitandblow.databinding.ActivityMainBinding
import app.ikd9684.android.hitandblow.databinding.LayoutResultBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    data class ResultItem(
        val numbers: List<Int>,
        val result: HitAndBlow.Result,
    )

    class ResultAdapter : RecyclerView.Adapter<ViewHolder>() {

        class ResultViewHolder(private val binding: LayoutResultBinding) :
            ViewHolder(binding.root) {

            fun bind(item: ResultItem) {
                var numbers = ""
                item.numbers.forEach { numbers += "$it" }
                binding.tvNumbers.text = numbers
                binding.tvHitValue.text = "${item.result.hit}"
                binding.tvBlowValue.text = "${item.result.blow}"

                val hitValueColor = if (item.result.hit == numbers.length) {
                    R.color.text_hit_all
                } else {
                    R.color.text_main
                }
                val tvHitValueColor = binding.root.context.getColor(hitValueColor)
                binding.tvHitValue.setTextColor(tvHitValueColor)

            }
        }

        private val items = mutableListOf<ResultItem>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding =
                LayoutResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ResultViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            (holder as? ResultViewHolder)?.bind(items[position])
        }

        override fun getItemCount(): Int = items.size

        fun clearAllItems() {
            val toPosition = items.size
            items.clear()
            notifyItemRangeRemoved(0, toPosition)
        }

        fun prependItem(numbers: List<Int>, result: HitAndBlow.Result) {
            items.add(0, ResultItem(numbers, result))
            notifyItemInserted(0)
        }

        fun appendItem(numbers: List<Int>, result: HitAndBlow.Result) {
            items.add(ResultItem(numbers, result))
            notifyItemInserted(items.size - 1)
        }
    }

    private val digits = 3
    private val hitAndBlow = HitAndBlow(digits)
    private val resultAdapter = ResultAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvResult.adapter = resultAdapter
        binding.rvResult.layoutManager =
            LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)

        onClickRestartBtn()
    }

    private fun onClickDeduceBtn() {
        val numbers = mutableListOf<Int>()
        val inputText = binding.etNumber.text
        inputText.forEach {
            try {
                val n = "$it".toInt()
                numbers.add(n)
            } catch (e: Exception) {
                return
            }
        }

        if (numbers.size != digits) {
            return
        }
        val result = hitAndBlow.deduce(numbers)

        resultAdapter.prependItem(numbers, result)

        binding.etNumber.text.clear()

        if (result.hit == hitAndBlow.digits) {
            binding.etNumber.isEnabled = false
            binding.btnDeduce.text = getString(R.string.restart)
            binding.btnDeduce.setOnClickListener {
                onClickRestartBtn()
            }
        }
    }

    private fun onClickRestartBtn() {
        resultAdapter.clearAllItems()
        hitAndBlow.initRandom()

        binding.etNumber.isEnabled = true
        binding.etNumber.requestFocus()
        binding.btnDeduce.text = getString(R.string.deduce)
        binding.btnDeduce.setOnClickListener {
            onClickDeduceBtn()
        }
    }
}
