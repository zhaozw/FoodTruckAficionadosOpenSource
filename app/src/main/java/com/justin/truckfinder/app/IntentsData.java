package com.justin.truckfinder.app;

import android.app.Activity;
import android.content.Intent;

import java.io.Serializable;

/**
 * Created by justinmac on 4/9/14.
 */
public class IntentsData extends Activity implements Serializable {

    Intent intentForGeo;

    public Intent getIntentForGeo() {
        return intentForGeo;
    }

    public void setIntentForGeo(Intent intentForGeo) {
        this.intentForGeo = intentForGeo;
    }
}
