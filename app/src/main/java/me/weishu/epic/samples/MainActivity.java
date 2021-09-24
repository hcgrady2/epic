package me.weishu.epic.samples;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import de.robv.android.xposed.XposedHelpers;
import me.weishu.epic.samples.tests.TestCase;
import me.weishu.epic.samples.tests.TestManager;
import me.weishu.epic.samples.tests.TestSuite;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends Activity implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = "MainActivity";

    ExpandableListView listView;

    List<TestSuite> allSuites;

    private  static Context mContext;

    private static final String[] LOCATION_AND_CONTACTS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE};

    private static final int RC_CAMERA_PERM = 123;
    private static final int RC_LOCATION_CONTACTS_PERM = 124;


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

        // Ask for both permissions
        EasyPermissions.requestPermissions(
                this,
                "给我定位权限，🙏",
                RC_LOCATION_CONTACTS_PERM,
                LOCATION_AND_CONTACTS);



// Target class, method with parameter types, followed by the hook callback (XC_MethodHook).
//        DexposedBridge.findAndHookMethod(Activity.class, "onCreate", Bundle.class, new XC_MethodHook() {
//
//            // To be invoked before Activity.onCreate().
//            @Override protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                // "thisObject" keeps the reference to the instance of target class.
//                Activity instance = (Activity) param.thisObject;
//
//                // The array args include all the parameters.
//                Bundle bundle = (Bundle) param.args[0];
//                Intent intent = new Intent();
//                // XposedHelpers provide useful utility methods.
//                XposedHelpers.setObjectField(param.thisObject, "mIntent", intent);
//
//                Log.i("hook", Thread.currentThread().getName());
//                Log.i("hook", Log.getStackTraceString(new Throwable()));
//
//
//            }
//
//            // To be invoked after Activity.onCreate()
//            @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                XposedHelpers.callMethod(param.thisObject, "sampleMethod", 2);
//            }
//        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Some permissions have been granted
        // ...
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Some permissions have been denied
        // ...
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
