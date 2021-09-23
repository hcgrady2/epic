package me.weishu.epic.samples.hook;

import android.content.Intent;
import android.util.Log;

import de.robv.android.xposed.DexposedBridge;
import de.robv.android.xposed.XC_MethodHook;
import me.weishu.epic.samples.MainActivity;
import me.weishu.epic.samples.MainApplication;
import me.weishu.epic.samples.tests.TestCase;
import me.weishu.epic.samples.tests.returntype.ReturnTypeTarget;

/**
 * @Author: wanghaichao
 * @Date: 2021/9/23
 * @Description:
 * @Copyright: all rights reserved.
 */
public class MyTest extends TestCase {

    private static final String TAG = "VoidType";

    boolean callBefore = false;
    boolean callAfter = false;

    public MyTest() {
        super("跳转界面");
    }

    @Override
    public void test() {
        MainActivity.getmContext().startActivity(new Intent(MainActivity.getmContext(),OtherDemoMain.class));
        Log.i(TAG, "test: ");
    }

    @Override
    public boolean predicate() {

        Log.i(TAG, "callBefore:" + callBefore + ", callAfter:" + callAfter);

        return  false;
    }


}
