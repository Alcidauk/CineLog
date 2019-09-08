package com.ulicae.cinelog.utils.image;

class CacheImageDownloaderTaskFactory {

    CacheImageDownloaderNetworkTask makeTask(String imagePath){
        return new CacheImageDownloaderNetworkTask(imagePath);
    }

}
