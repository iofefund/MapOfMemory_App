package org.mapofmemory.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.mapofmemory.DayOfMemoryItemFragment;
import org.mapofmemory.entities.DayOfMemory;

import java.util.List;

/**
 * Created by The Tronuo on 04.02.2018.
 */

public class DOMAdapter extends FragmentPagerAdapter {
    private List<DayOfMemory> doms;
    public DOMAdapter(FragmentManager fragmentManager, List<DayOfMemory> doms) {
        super(fragmentManager);
        this.doms = doms;
    }

    @Override
    public int getCount() {
        return doms.size();
    }

    @Override
    public Fragment getItem(int position) {
        return DayOfMemoryItemFragment.newInstance(doms.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}
