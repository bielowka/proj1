package agh.ics.oop.basics;

public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;


    public MapDirection next(){
        switch (this)
        {
            case NORTH: return NORTHEAST;
            case NORTHEAST: return EAST;
            case EAST: return SOUTHEAST;
            case SOUTHEAST: return SOUTH;
            case SOUTH: return SOUTHWEST;
            case SOUTHWEST: return WEST;
            case WEST: return NORTHWEST;
            case NORTHWEST: return NORTH;
            default: return NORTH;
        }
    }

    public Vector2d toUnitVector(){
        switch (this)
        {
            case EAST:
                return new Vector2d(1,0);

            case WEST:
                return new Vector2d(-1,0);

            case NORTH:
                return new Vector2d(0,1);

            case SOUTH:
                return new Vector2d(0,-1);

            case NORTHWEST:
                return new Vector2d(-1,1);

            case NORTHEAST:
                return new Vector2d(1,1);

            case SOUTHWEST:
                return new Vector2d(-1,-1);

            case SOUTHEAST:
                return new Vector2d(1,-1);

            default:
                return new Vector2d(0,0);

        }

    }

    public static MapDirection intToMapDirection(int that){
        switch (that){
            case 0: return NORTH;
            case 1: return NORTHEAST;
            case 2: return EAST;
            case 3: return SOUTHEAST;
            case 4: return SOUTH;
            case 5: return SOUTHWEST;
            case 6: return WEST;
            case 7: return NORTHWEST;
            default:
                throw new IllegalStateException("Unexpected value: " + that);
        }
    }


}
