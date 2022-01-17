package uz.umarxon.obhavo.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import uz.umarxon.obhavo.ItemFragment
import uz.umarxon.obhavo.models.WeatherModel


class MainViewPagerAdapter(private val context: Context, fm: FragmentManager,var list: List<WeatherModel>)
    : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return ItemFragment(list[position])
    }

    override fun getPageTitle(position: Int): CharSequence {
        return list[position].name
    }

    override fun getCount(): Int {
        return list.size
    }
}