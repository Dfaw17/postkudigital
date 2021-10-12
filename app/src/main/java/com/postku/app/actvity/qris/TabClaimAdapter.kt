package com.postku.app.actvity.qris

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.postku.app.actvity.qris.claim.RiwayatDoneFragment
import com.postku.app.actvity.qris.claim.RiwayatProsesFragment
import com.postku.app.actvity.wallet.topup.TopupCancelFragment
import com.postku.app.actvity.wallet.topup.TopupProsesFragment
import com.postku.app.actvity.wallet.topup.TopupSuksesFragment
import com.postku.app.fragment.product.kategori.KategoriFragment
import com.postku.app.fragment.product.menu.MenuFragment
import com.postku.app.fragment.product.stok.StockFragment

class TabClaimAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    // sebuah list yang menampung objek Fragment
    private val pages = listOf(
        RiwayatProsesFragment(),
        RiwayatDoneFragment()
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
            0 -> "Dalam Proses"
            else -> "Selesai"
        }
    }
}