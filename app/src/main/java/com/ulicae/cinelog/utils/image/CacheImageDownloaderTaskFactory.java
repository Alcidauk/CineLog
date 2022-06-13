package com.ulicae.cinelog.utils.image;

import java.io.File;

class CacheImageDownloaderTaskFactory {

    CacheImageDownloaderNetworkTask makeTask(File cacheDir, String imagePath){
        return new CacheImageDownloaderNetworkTask(cacheDir, imagePath);
    }

}
