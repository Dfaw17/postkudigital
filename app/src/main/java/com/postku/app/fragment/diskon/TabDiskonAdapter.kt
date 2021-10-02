package com.postku.app.fragment.diskon

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.postku.app.fragment.diskon.fee.ServiceFeeFragment
import com.postku.app.fragment.diskon.promo.PromoFragment
import com.postku.app.fragment.diskon.tax.TaxFragment
import com.postku.app.fragment.product.kategori.KategoriFragment
import com.postku.app.fragment.product.menu.MenuFragment
import com.postku.app.fragment.product.stok.StockFragment

class TabDiskonAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    // sebuah list yang menampung objek Fragment
    private val pages = listOf(
        PromoFragment(),
        TaxFragment(),
        ServiceFeeFragment()
    )

    // menentukan fragment yang akan dibuka pada posisi tertentu
    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    // judul untuk tabs
    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Discount"
            1 -> "Pajak"
            else -> "Service"
        }
    }
}