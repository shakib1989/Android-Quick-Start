/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.shakib.quickstart.activities;

import java.lang.reflect.Field;
import java.net.InetAddress;

import com.shakib.quickstart.R;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.ViewConfiguration;


/**
 * 
 * @author Mohammed Shakib
 * 
 */
public abstract class AbstractBaseActivity extends Activity {

    AlertDialog alertExitDialog;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.getResourceLayout());
        this.getOverflowMenu();
        this.onBaseActivityAfterInitialize();
    }

    /**
     * 
     * @return the resource id of the layout
     */
    protected int getResourceLayout(){
        return R.layout.activity_base;
    }
    
    public abstract void onBaseActivityAfterInitialize();

    /**
     * Enables the Action Bar menu overflow. The Triple dots at the side of the
     * screen
     */
    private void getOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add Tab to Action Bar
     * 
     * @param title
     *            - The tab Title
     * @param listener
     *            - The tab listener
     */
    public void addTab(String title, ActionBar.TabListener listener) {
        ActionBar.Tab tab = this.getActionBar().newTab().setText(title);
        tab.setTabListener(listener);
        this.getActionBar().addTab(tab);

    }

    /**
     * Set the method content body for native fragments
     * 
     * @param fragment
     */
    public void setContent(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    void setActionBarTitle(String title) {
        this.getActionBar().setTitle(title);
    }

    public ActionBar.TabListener getDefaultTabListener(final String title, final Fragment fragment) {
        return new ActionBar.TabListener() {

            @Override
            public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
            }

            /**
             * Set the respective fragment as the content
             */
            @Override
            public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
                setContent(fragment);
            }

            @Override
            public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
            }

        };
    }

    /**
     * Notify a user that there is no Internet connection available via dialog
     */
    public void showNoConnectionDialog() {
        if (alertDialog == null) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Connection");
            alertDialogBuilder.setMessage("No Internet Connection.").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {
                }
            });
            alertDialog = alertDialogBuilder.create();
        }
        alertDialog.show();
    }

    /**
     * Notify a user of a message via dialog
     */
    public void showMessageDialog(String title, String message) {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Notify that the user is trying to exit the Application via dialog
     * and get user response
     * 
     * @param title
     * @param message
     */
    public void showExitDialog(String title, String message) {

        if (alertExitDialog != null && alertExitDialog.isShowing()) {
            alertExitDialog.dismiss();
        }
        final Activity activity = this;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                activity.finish();
            }
        });
        alertExitDialog = alertDialogBuilder.create();
        alertExitDialog.show();
    }

    /**
     * Check if Device is connected to a network
     * 
     * @return
     */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

    /**
     * 
     * @return
     */
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            if (!ipAddr.equals("")) {
                return true;
            }
        } catch (Exception e) {}
        return false;

    }
}
