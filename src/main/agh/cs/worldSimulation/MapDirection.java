package agh.cs.worldSimulation;

public enum MapDirection {
    NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST, NullPointerException;

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
        int x = 0, y = 0;

        if(this.equals(MapDirection.NORTH))
            y = 1;
        else if(this.equals(MapDirection.NORTH_EAST)) {
            x = 1;
            y = 1;
        } else if(this.equals(MapDirection.WEST))
            x = -1;
        else if(this.equals(MapDirection.SOUTH_EAST)) {
            x = 1;
            y = -1;
        } else if(this.equals(MapDirection.SOUTH))
            y = -1;
        else if(this.equals(MapDirection.SOUTH_WEST)) {
            x = -1;
            y = -1;
        } else if(this.equals(MapDirection.EAST))
            x=1;
        else if(this.equals(MapDirection.NORTH_WEST)) {
            x = -1;
            y = 1;
        }
        else
            System.out.println("Error: toUnitVector in MapDirection.java");

        return new Vector2d(x, y);
    }
}
