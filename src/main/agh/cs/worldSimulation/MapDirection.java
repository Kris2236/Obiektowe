package agh.cs.worldSimulation;

public enum MapDirection {
    NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST, NullPointerException;

//    public static MapDirection getRandomDirection(){
//
//    }

    public String toString(){
        switch(this) {
            case NORTH: return "N (Północ)";
            case NORTH_EAST: return "NE (Północny-wschód)";
            case EAST: return "E (Wschód)";
            case SOUTH_EAST: return "SE (Południowy-wschód)";
            case SOUTH: return "S (Południe)";
            case SOUTH_WEST: return "SW (Południowy-zachód)";
            case WEST: return "W (Zachód)";
            case NORTH_WEST: return "NW (Północny-zachód)";
            default:
                System.out.println("Error: toString in MapDirection.java");
                return "";
        }
    }

    public MapDirection next(){
        switch(this) {
            case NORTH: return NORTH_EAST;
            case NORTH_EAST: return EAST;
            case EAST: return SOUTH_EAST;
            case SOUTH_EAST: return SOUTH;
            case SOUTH: return SOUTH_WEST;
            case SOUTH_WEST: return WEST;
            case WEST: return NORTH_WEST;
            case NORTH_WEST: return NORTH;
            default:
                System.out.println("Error: next in MapDirection.java");
                return NullPointerException;
        }
    }

    public MapDirection previous(){
        switch(this) {
            case NORTH: return NORTH_WEST;
            case NORTH_EAST: return NORTH;
            case EAST: return NORTH_EAST;
            case SOUTH_EAST: return EAST;
            case SOUTH: return SOUTH_EAST;
            case SOUTH_WEST: return SOUTH;
            case WEST: return SOUTH_WEST;
            case NORTH_WEST: return WEST;
            default:
                System.out.println("Error: previous in MapDirection.java");
                return NullPointerException;
        }
    }

    public Vector2d toUnitVector(){

        switch(this) {
            case NORTH: return new Vector2d(0,1);
            case NORTH_EAST: return new Vector2d(1,1);
            case EAST: return new Vector2d(1,0);
            case SOUTH_EAST: return new Vector2d(1,-1);
            case SOUTH: return new Vector2d(0,-1);
            case SOUTH_WEST: return new Vector2d(-1,-1);
            case WEST: return new Vector2d(-1,0);
            case NORTH_WEST: return new Vector2d(-1,1);
            default:
                throw new IllegalArgumentException("Illegal direction in toUnitVector in MapDirection.java");
        }
    }
}
