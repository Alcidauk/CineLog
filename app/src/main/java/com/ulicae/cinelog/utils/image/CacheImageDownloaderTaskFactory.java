package com.ulicae.cinelog.utils.image;

import java.io.File;

class CacheImageDownloaderTaskFactory {

    CacheImageDownloaderNetworkTask makeTask(File dataDir, String imagePath){
        return new CacheImageDownloaderNetworkTask(dataDir, imagePath);
    }

}
