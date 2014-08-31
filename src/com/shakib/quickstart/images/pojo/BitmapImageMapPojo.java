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

package com.shakib.quickstart.images.pojo;

import android.graphics.Bitmap;


/**
 * 
 * @author Mohammed Shakib
 *
 */
public class BitmapImageMapPojo {

    protected String loadStatus;
    protected Bitmap bitmapImage;
    
    
    public BitmapImageMapPojo() {}
    
    /**
     * @param loadStatus
     * @param bitmapImage
     */
    public BitmapImageMapPojo(String loadStatus, Bitmap bitmapImage) {
        super();
        this.loadStatus = loadStatus;
        this.bitmapImage = bitmapImage;
    }
    
    /**
     * @return the bitmap image load status
     */
    public String getLoadStatus() {
        return loadStatus;
    }

    
    /**
     * @param loadStatus - the bitmap image load status to
     */
    public void setLoadStatus(String loadStatus) {
        this.loadStatus = loadStatus;
    }

    
    /**
     * @return the bitmapImage
     */
    public Bitmap getBitmapImage() {
        return bitmapImage;
    }

    
    /**
     * @param bitmapImage - the bitmapImage to set
     */
    public void setBitmapImage(Bitmap bitmapImage) {
        this.bitmapImage = bitmapImage;
    }
    

}
