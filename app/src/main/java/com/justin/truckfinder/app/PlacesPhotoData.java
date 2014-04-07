package com.justin.truckfinder.app;

import com.android.volley.toolbox.ImageLoader;

import java.io.Serializable;

/**
 * Created by justinmac on 4/7/14.
 */
public class PlacesPhotoData implements Serializable {
    private String photoPlacesURL;
    private ImageLoader imageLoader;


    public String getPhotoPlacesURL() {
        return photoPlacesURL;
    }

    public void setPhotoPlacesURL(String photoPlacesURL) {
        this.photoPlacesURL = photoPlacesURL;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }
}
