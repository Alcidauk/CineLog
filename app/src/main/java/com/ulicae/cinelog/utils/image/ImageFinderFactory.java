package com.ulicae.cinelog.utils.image;

class ImageFinderFactory {

    ImageFinder makeNetworkImageFinder(){
        return new ImageNetworkFinder();
    }

    ImageFinder makeCacheImageFinder(){
        return new ImageCacheFinder();
    }
}
