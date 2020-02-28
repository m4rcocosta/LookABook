package uni.mobile.mobileapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {
    int totalTabs;
    public TabAdapter(FragmentManager fm, int totalTabs) {
        super(fm);
        this.totalTabs = totalTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                InfoProfileFragment infoProfileFragment = new InfoProfileFragment();
                return infoProfileFragment;
            case 1:
                AdvancedProfileFragment advancedProfileFragment = new AdvancedProfileFragment();
                return advancedProfileFragment;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}
