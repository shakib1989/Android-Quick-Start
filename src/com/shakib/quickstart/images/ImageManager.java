/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.shakib.quickstart.images;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.shakib.quickstart.images.pojo.BitmapImageMapPojo;

/**
 * 
 * @author Mohammed Shakib
 *
 */
public class ImageManager {

    private static HashMap<String, BitmapImageMapPojo> imageMap = new HashMap<String, BitmapImageMapPojo>();
    private static HashMap<String, ArrayList<ImageView>> imageViewQueue = new HashMap<String, ArrayList<ImageView>>();
    private static boolean syncMethodStatus = false;

    /**
     * Set All Images View Images that are waiting in queue
     */
    public static synchronized void setImages() {
        ImageManager.syncMethodStatus = true;
        for (String key : imageViewQueue.keySet()) {
            setImagesFormKey(key);
        }
        ImageManager.syncMethodStatus = false;
        System.gc();
    }

    /**
     * 
     * @param key - the Key to set all images from the image view
     */
    private static synchronized void setImagesFormKey(String key) {
        //Check if Key is Valid
        if (ImageManager.imageMap.containsKey(key) && "done".equalsIgnoreCase(ImageManager.imageMap.get(key).getLoadStatus())) {
            Bitmap image = ImageManager.imageMap.get(key).getBitmapImage();
            ArrayList<ImageView> imageViewList = ImageManager.imageViewQueue.get(key);
            if (imageViewList != null && !imageViewList.isEmpty()) {
                //Set All Image View Images
                while (imageViewList.size() > 0) {
                    imageViewList.get(0).setImageBitmap(image);
                    imageViewList.remove(0);
                }
                ImageManager.imageViewQueue.get(key).clear();
            }
        }
       
    }

    /**
     * Set All Images Views Images that are in queue and then remove the image
     */
    public static synchronized void removeAllLoadedImages() {
        for (String key : ImageManager.imageMap.keySet()) {
            ImageManager.removeUrlLoadedImages(key);
        }
        System.gc();
    }
    
    /**
     * Set All Images Views Images that are in queue and then remove the image
     */
    public static synchronized void removeUrlLoadedImages(String key) {
        if ("done".equalsIgnoreCase(ImageManager.imageMap.get(key).getLoadStatus())) {
            ImageManager.setImagesFormKey(key);
            ImageManager.imageMap.remove(key);
            ImageManager.imageViewQueue.remove(key);
        }
        System.gc();
    }

    /**
     * Free the memory space taken by the saved bit images when the memory usage is greater than 80% 
     */
    public static void doExcessMemoryClean() {
        long freeSize = 0L;
        long totalSize = 0L;
        long usedSize = -1L;
        try {
            // Calculate Memory Statistics 
            Runtime info = Runtime.getRuntime();
            freeSize = info.freeMemory();
            totalSize = info.totalMemory();
            usedSize = totalSize - freeSize;
            //Check if more than 80% of the memory is currently being used
            if(((usedSize*1.0)/totalSize) > 0.80){
               removeAllLoadedImages();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the Image View with the image after it is loaded
     * @param url - the image url
     * @param view  - the image view
     */
    public static void setImage(String url, ImageView view) {
        if (!ImageManager.imageViewQueue.containsKey(url)) {
            ImageManager.imageViewQueue.put(url, new ArrayList<ImageView>());
        }
        ImageManager.imageViewQueue.get(url).add(view);
        ImageManager imageManager = new ImageManager();
        imageManager.loadImage(url);
    }

    /**
     * Invokes LoadImageAsyncTask to load the image
     * @param url
     */
    public void loadImage(String url) {
        LoadImageAsyncTask loadImage = new LoadImageAsyncTask(url);
        loadImage.execute("");
    }

    /**
     * Load Image and saves it to Image manager
     * @author Mohammed Shakib
     *
     */
    protected static class LoadImageAsyncTask extends AsyncTask<String, String, Bitmap> {

        String url;
        Bitmap bitmap;

        public LoadImageAsyncTask(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Bitmap doInBackground(String... args) {
            BitmapImageMapPojo pojo = ImageManager.imageMap.get(url);
            if (pojo!=null && ("loading".equalsIgnoreCase(pojo.getLoadStatus()) || "done".equalsIgnoreCase(pojo.getLoadStatus()))) {
                return null;
            } else {
                pojo = new BitmapImageMapPojo("loading", null);
                ImageManager.imageMap.put(url, pojo);
                try {
                    bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {
            if (!ImageManager.imageMap.containsKey(url) && image != null) {
                BitmapImageMapPojo pojo = new BitmapImageMapPojo("done", image);
                ImageManager.imageMap.put(url, pojo);
            } else if (!"done".equalsIgnoreCase(ImageManager.imageMap.get(url).getLoadStatus()) && image != null) {
                ImageManager.imageMap.get(url).setBitmapImage(image);
                ImageManager.imageMap.get(url).setLoadStatus("done");
            
            }else if ("loading".equalsIgnoreCase(ImageManager.imageMap.get(url).getLoadStatus()) && image == null) {
                ImageManager.imageMap.get(url).setLoadStatus("failed");
            }
            System.gc();
            setImages();
        }
    }
}
