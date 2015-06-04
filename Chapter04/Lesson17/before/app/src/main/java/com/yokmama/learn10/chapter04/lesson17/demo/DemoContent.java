package com.yokmama.learn10.chapter04.lesson17.demo;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

import com.yokmama.learn10.chapter04.lesson17.fragment.FrameLayoutFragment;
import com.yokmama.learn10.chapter04.lesson17.fragment.GridLayoutFragment;
import com.yokmama.learn10.chapter04.lesson17.fragment.LinearLayoutFragment;
import com.yokmama.learn10.chapter04.lesson17.fragment.RelativeLayoutFragment2;
import com.yokmama.learn10.chapter04.lesson17.fragment.RelativeLayoutFragment;
import com.yokmama.learn10.chapter04.lesson17.fragment.TableLayoutFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DemoContent {

    /**
     * An array of demo items.
     */
    public static List<DemoItem> ITEMS = new ArrayList<DemoItem>();

    static {
        addItem(new DemoItem(
                FrameLayout.class.getSimpleName(),
                "FrameLayoutの使用したサンプルを表示",
                FrameLayoutFragment.class.getCanonicalName()));
        addItem(new DemoItem(
                LinearLayout.class.getSimpleName(),
                "LinearLayoutの使用したサンプルを表示",
                LinearLayoutFragment.class.getCanonicalName()));
        addItem(new DemoItem(
                TableLayout.class.getSimpleName(),
                "TableLayoutの使用したサンプルを表示",
                TableLayoutFragment.class.getCanonicalName()));
        if(Build.VERSION.SDK_INT>=14) {
            addItem(new DemoItem(
                    GridLayout.class.getSimpleName(),
                    "GridLayoutの使用したサンプルを表示",
                    GridLayoutFragment.class.getCanonicalName()));
        }
        addItem(new DemoItem(
                RelativeLayout.class.getSimpleName()+"1",
                "RelativeLayoutの使用したサンプルを表示１",
                RelativeLayoutFragment.class.getCanonicalName()));
        addItem(new DemoItem(
                RelativeLayout.class.getSimpleName()+"2",
                "RelativeLayoutの使用したサンプルを表示２",
                RelativeLayoutFragment2.class.getCanonicalName()));
    }

    private static void addItem(DemoItem item) {
        ITEMS.add(item);
    }

    /**
     * A demo item representing a piece of content.
     */
    public static class DemoItem implements Parcelable {
        private String content;
        private String description;
        private String fragmentName;

        public DemoItem(String content, String description, String fragmentName) {
            this.content = content;
            this.description = description;
            this.fragmentName = fragmentName;
        }

        @Override
        public String toString() {
            return content;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(content);
            dest.writeString(description);
            dest.writeString(fragmentName);
        }

        public static final Parcelable.Creator<DemoItem> CREATOR
                = new Parcelable.Creator<DemoItem>() {
            public DemoItem createFromParcel(Parcel in) {
                return new DemoItem(in);
            }

            public DemoItem[] newArray(int size) {
                return new DemoItem[size];
            }
        };

        private DemoItem(Parcel in) {
            content = in.readString();
            description = in.readString();
            fragmentName = in.readString();
        }

        public String getFragmentName() {
            return fragmentName;
        }

        public String getContent(){
            return content;
        }

        public String getDescription(){
            return description;
        }
    }
}
