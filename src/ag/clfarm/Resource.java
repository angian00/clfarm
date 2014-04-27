package ag.clfarm;

public enum Resource {
    //special values
    NONE,
    UNKNOWN,
    
    //crops
    WHEAT,
    CORN,
    SOY,;

    public static Resource fromCrop (CropType cropType)
    {
        return Resource.valueOf(cropType.toString());
        //TODO: protect from unknown values
    }
}
