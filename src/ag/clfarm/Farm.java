package ag.clfarm;

import java.util.*;

public class Farm {
    private GameStatus game;
    private List<Field> fields;
    private Map<Resource, Integer> storage;
    
    
    public Farm(GameStatus game)
    {
        this.game = game;
        
        this.fields = new ArrayList<Field>();
        this.storage = new HashMap<Resource, Integer>();
    }
    
    
    public final List<Field> getFields()
    {
        return fields;
    }

    public Field createNewField()
    {
        Field f = new Field();
        fields.add(f);
        
        return f;
    }
    
    public final Map<Resource, Integer> getStorage()
    {
        return storage;
    }

    public void addToStorage(Resource r)
    {
        addToStorage(r, 1);
    }
    
    public void addToStorage(Resource r, int quantity)
    {
        if (quantity < 0) {
            //TODO: logger.warning
            return;
        }
        
        Integer oldQuantity = storage.get(r);
        if (oldQuantity == null)
            oldQuantity = 0;
        
        storage.put(r, (oldQuantity + quantity));
    }
    

    /**
     * @return true if production starts succesfully, false otherwise
     */
    public boolean startProd(int iField, CropType cropType)
    {
        if (iField < 0 || iField >= fields.size())
            return false;
        
        if (cropType == CropType.NONE)
            return false;
            
        Field f = fields.get(iField);
        if (f.isProducing())
            return false;
        
        f.startProd(cropType);
        game.scheduleCrop(f.getProdEnd(), cropType);
        return true;
    }
    
    public static Farm makeSampleFarm(GameStatus game)
    {
        Farm sampleFarm = new Farm(game);
        
        for (int iField=0; iField < 3; iField++) {
            sampleFarm.createNewField();

            CropType cropType;
            
            switch (iField) {
            case 0:
                cropType = CropType.WHEAT;
                break;
            case 1:
                cropType = CropType.CORN;
                break;
            case 2:
                cropType = CropType.SOY;
                break;
                
            default:
                cropType = CropType.WHEAT;
            }
            
            sampleFarm.startProd(iField, cropType);
        }
        
        return sampleFarm;
    }
}
