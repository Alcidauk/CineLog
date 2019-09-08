package com.ulicae.cinelog.utils.image;

import java.net.MalformedURLException;
import java.net.URL;

class ImageNetworkFinder implements ImageFinder<URL> {

    @Override
    public URL getImage(String imagePath) {
        try {
            return new URL(ImageCacheDownloader.BASE_URL + imagePath);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
