package ag.clfarm;

public class Field {
    
    private CropType cropType;
    private long prodStart;
    private long prodEnd;
    
    
    public Field()
    {
        //fields begin uncultivated
        this.cropType = CropType.NONE;
    }
    
    
    public CropType getCropType()
    {
        return cropType;
    }

    
    public void startProd (CropType cropType)
    {
        this.cropType = cropType;
        this.prodStart = System.currentTimeMillis();
        this.prodEnd = prodStart + 30*1000; //TODO: differentiate crops by configuration
    }

    public void stopProd ()
    {
        this.cropType = CropType.NONE;
    }
    
    public boolean isProducing()
    {
        return (cropType != CropType.NONE);
    }
    
    public boolean isProdReady()
    {
        if (!isProducing())
            return false;

        long now = System.currentTimeMillis();
        return (prodEnd <= now);
    }
    
    public double getProdStage()
    {
        if (!isProducing())
            return 0.0;
        
        long now = System.currentTimeMillis();
        double ratio = ((double)(now - prodStart)) / (prodEnd - prodStart);
        
        if (ratio > 1.0)
            ratio = 1.0;
        else if (ratio < 0.0)
            ratio = 0.0;
        
        return ratio;
    }
    
    public String getProdStageLabel()
    {
        if (!isProducing())
            return "";
                    
        double prodStage = this.getProdStage();
        
        if (prodStage < 0.25)
            return "unripe";
        else if (prodStage < 0.75)
            return "ripening";
        else if (prodStage < 1.0)
            return "almost ripe";
        else
            return "ripe!";
    }

    public long getProdEnd()
    {
        if (!isProducing())
            return 0L;
        else
            return prodEnd;
    }
}
