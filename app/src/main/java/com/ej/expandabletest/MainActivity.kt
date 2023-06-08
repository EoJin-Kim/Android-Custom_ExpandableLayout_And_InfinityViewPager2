package com.ej.expandabletest

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginLeft
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.ej.expandabletest.databinding.ActivityMainBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlin.math.ceil
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var nowTab = 3
    var tapSize = 0f

    private var bannerPosition = 0

    lateinit var job : Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        setTabUi()
        setTabAction()
        //임시 데이터 생성
        infinityScrollSetUp()

    }

    private fun infinityScrollSetUp() {
        val list: ArrayList<DataPage> = ArrayList<DataPage>().let {
            it.apply {
                add(DataPage(android.R.color.holo_red_light, "1 Page"))
                add(DataPage(android.R.color.holo_orange_dark, "2 Page"))
                add(DataPage(android.R.color.holo_green_dark, "3 Page"))
                add(DataPage(android.R.color.holo_blue_light, "4 Page"))
                add(DataPage(android.R.color.holo_blue_bright, "5 Page"))
                add(DataPage(android.R.color.black, "6 Page"))
            }
        }
        binding.viewPager2.adapter = ViewPagerAdapter(list)
        binding.viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.txtCurrentBanner.text = getString(R.string.viewpager2_banner, 1, list.size)
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            //사용자가 스크롤 했을때 position 수정
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bannerPosition = position
                binding.txtCurrentBanner.text = getString(
                    R.string.viewpager2_banner,
                    (bannerPosition % list.size) + 1,
                    list.size
                )
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                when (state) {
                    ViewPager2.SCROLL_STATE_IDLE -> {
                        if (!job.isActive) scrollJobCreate()
                    }

                    ViewPager2.SCROLL_STATE_DRAGGING -> job.cancel()

                    ViewPager2.SCROLL_STATE_SETTLING -> {}
                }
            }
        })

        bannerPosition = Int.MAX_VALUE / 2 - ceil(list.size.toDouble() / 2).toInt()

        binding.viewPager2.setCurrentItem(bannerPosition, false)

        val viewSize = resources.getDimension(R.dimen.indicator_size).toInt()
        val layoutParams = LinearLayout.LayoutParams(
            viewSize,viewSize
        )
        val marginSize = resources.getDimension(R.dimen.indicator_size).toInt()


        val textView1 = TextView(baseContext)
        textView1.text = ""
        textView1.background = resources.getDrawable(R.drawable.oval_white)


        layoutParams.setMargins(marginSize, 0, marginSize, 0)
        textView1.layoutParams = layoutParams

        val textView2 = TextView(baseContext)
        textView2.text = ""
        textView2.background = resources.getDrawable(R.drawable.oval_white)


        layoutParams.setMargins(marginSize, 0, marginSize, 0)
        textView2.layoutParams = layoutParams

        binding.layoutIndicator.addView(textView1)
        binding.layoutIndicator.addView(textView2)

//        binding.circleIndicator3.setViewPager(binding.viewPager2)
    }

    override fun onResume() {
        super.onResume()
        scrollJobCreate()
    }

    override fun onPause() {
        super.onPause()
        job.cancel()
    }

    private fun setTabUi() {
        val tab1Fragment = Tab1Fragment.newInstance()
        val tab2Fragment = Tab2Fragment.newInstance()
        val tab3Fragment = Tab3Fragment.newInstance()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.tab1, tab1Fragment)
        transaction.replace(R.id.tab2, tab2Fragment)
        transaction.replace(R.id.tab3, tab3Fragment)
        transaction.commit()
    }

    private fun setTabAction() {
        tapSize = resources.getDimension(R.dimen.tab_size)

        binding.tab1.setOnClickListener {
            if (nowTab == 2) {
                closeTab2()
            } else if (nowTab == 3) {
                closeTab2()
                closeTab3()
            }
            nowTab = 1
        }

        binding.tab2.setOnClickListener {
            if (nowTab == 1) {
                openTab2()
            } else if (nowTab == 3) {
                closeTab3()
            }
            nowTab = 2
        }

        binding.tab3.setOnClickListener {
            if (nowTab == 1) {
                openTab2()
                openTab3()
            } else if (nowTab == 2) {
                openTab3()
            }
            nowTab = 3
        }
    }

    private fun closeTab3() {
        ObjectAnimator.ofFloat(binding.tab3, "translationY", binding.tab3.height - tapSize).apply {
            duration = 400
            start()
        }
    }

    private fun closeTab2() {
        ObjectAnimator.ofFloat(binding.tab2, "translationY", binding.tab2.height - tapSize * 2)
            .apply {
                duration = 400
                start()
            }
    }

    private fun openTab3() {
        ObjectAnimator.ofFloat(binding.tab3, "translationY", 0f).apply {
            duration = 400
            start()
        }
    }

    private fun openTab2() {
        ObjectAnimator.ofFloat(binding.tab2, "translationY", 0f).apply {
            duration = 400
            start()
        }
    }

    fun scrollJobCreate() {
        job = lifecycleScope.launchWhenResumed {
            delay(1500)
            binding.viewPager2.setCurrentItemWithDuration(++bannerPosition, 2500)
        }
    }


}