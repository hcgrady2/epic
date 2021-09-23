package me.weishu.epic.samples;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.robv.android.xposed.DexposedBridge;
import de.robv.android.xposed.XC_MethodHook;
import me.weishu.epic.samples.tests.TestCase;
import me.weishu.epic.samples.tests.TestManager;
import me.weishu.epic.samples.tests.TestSuite;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    ExpandableListView listView;

    List<TestSuite> allSuites;

    private  static Context mContext;

    public static Context getmContext(){
        return  mContext;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        Log.i(TAG, "savedInstance:" + savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Epic Test");
        listView = findViewById(R.id.list);
        allSuites = TestManager.getInstance().getAllSuites();
        ExpandableListAdapter adapter = new MyAdapter();
        listView.setAdapter(adapter);


        /**
         * demo
         */
        try {
            DexposedBridge.findAndHookMethod(
                    Class.forName("com.loc.d"),
                    "startLocation",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            Log.i("hook", Thread.currentThread().getName());
                            Log.i("hook", Log.getStackTraceString(new Throwable()));
                        }
                    }
            );
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    private class MyAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return allSuites.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return allSuites.get(groupPosition).getAllCases().size();
        }

        @Override
        public TestSuite getGroup(int groupPosition) {
            return allSuites.get(groupPosition);
        }

        @Override
        public TestCase getChild(int groupPosition, int childPosition) {
            return allSuites.get(groupPosition).getAllCases().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View parentView = View.inflate(parent.getContext(), R.layout.parent_layout, null);
            TextView t = parentView.findViewById(R.id.text);
            t.setText(getGroup(groupPosition).getName());

            ImageView indicator = parentView.findViewById(R.id.indicator);
            if (isExpanded) {
                indicator.setImageResource(R.drawable.arrow_down);
            } else {
                indicator.setImageResource(R.drawable.arrow_up);
            }
            return parentView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final TestCase child = getChild(groupPosition, childPosition);

            final View childView = View.inflate(parent.getContext(), R.layout.child_layout, null);
            final TextView title = childView.findViewById(R.id.label);
            title.setText(child.getName());

            Button test = childView.findViewById(R.id.test);
            test.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    child.test();
                }
            });

            Button validate = childView.findViewById(R.id.validate);
            validate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean validate = child.validate();
                    final int color = v.getContext().getResources().getColor(validate ?
                            android.R.color.holo_green_light : android.R.color.holo_red_light);
                    childView.setBackgroundColor(color);
                }
            });

            return childView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}
