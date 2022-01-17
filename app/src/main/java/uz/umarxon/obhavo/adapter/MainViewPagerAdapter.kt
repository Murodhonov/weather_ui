package uz.umarxon.obhavo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import uz.umarxon.obhavo.ItemFragment
import uz.umarxon.obhavo.models.WeatherModel

class MainViewPagerAdapter(fm: FragmentManager, var list: List<WeatherModel>)
    : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return ItemFragment(list[position].coord.lat,list[position].coord.lon)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return list[position].name
    }

    override fun getCount(): Int {
        return list.size
    }
}