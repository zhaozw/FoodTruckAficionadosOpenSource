package com.justin.truckfinder.app;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/*
 * Created by justindelta on 3/24/14.
 */
public class FoodTruckStorage {
    private static FoodTruckDataGetter foodTruckDataGetter;
    //
    // Singleton pattern here:
    //
    private static FoodTruckStorage FOOD_STORAGE_REFERENCE;

    private FoodTruckStorage() {

    }

    protected static FoodTruckStorage getInstance() {
        if (FOOD_STORAGE_REFERENCE == null) {
            FOOD_STORAGE_REFERENCE = new FoodTruckStorage();
        }
        return FOOD_STORAGE_REFERENCE;
    }

//    private static FoodTruckDataGetter foodTruckDataGetter;
    //
    // Serialization here
    //


    static final String DATA_FILE_ARRAY = "data_file_array";

    protected static boolean saveMyData(Context aContext, ArrayList<FoodTruckData> myData) {
        try {
            FileOutputStream fos = aContext.openFileOutput(DATA_FILE_ARRAY, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(myData);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

        protected static ArrayList<FoodTruckData> getMyData(Context aContext) {
        try {
            FileInputStream mFileInputStream = aContext.openFileInput(DATA_FILE_ARRAY);
            ObjectInputStream mObjectInputStream = new ObjectInputStream(mFileInputStream);
            Object readObject = mObjectInputStream.readObject();
            mObjectInputStream.close();

            if(readObject != null && readObject instanceof ArrayList) {
                return (ArrayList<FoodTruckData>) readObject;
            }
        } catch (IOException anIOException) {
            anIOException.printStackTrace();
        } catch (ClassNotFoundException aClassNotFoundException) {
            aClassNotFoundException.printStackTrace();
        }
        return null;
    }

}
