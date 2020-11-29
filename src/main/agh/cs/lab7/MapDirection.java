package agh.cs.lab7;

public enum MapDirection {
    NORTH, SOUTH, WEST, EAST, NullPointerException;

    public String toString(){
        switch(this) {
            case NORTH: return "Północ";
            case SOUTH: return "Południe";
            case EAST: return "Wschód";
            case WEST: return "Zachód";
            default:
                System.out.println("Error: toString in MapDirection.java");
                return "";
        }
    }

    public MapDirection next(){
        switch(this) {
            case NORTH: return MapDirection.EAST;
            case EAST: return MapDirection.SOUTH;
            case SOUTH: return MapDirection.WEST;
            case WEST: return MapDirection.NORTH;
            default:
                System.out.println("Error: next in MapDirection.java");
                return NullPointerException;

        }
    }

    public MapDirection previous(){
        switch(this) {
            case NORTH: return MapDirection.WEST;
            case WEST: return MapDirection.SOUTH;
            case SOUTH: return MapDirection.EAST;
            case EAST: return MapDirection.NORTH;
            default:
                System.out.println("Error: previous in MapDirection.java");
                return NullPointerException;
        }
    }

    public Vector2d toUnitVector(){
        int x=0, y=0;

        if(this.equals(MapDirection.NORTH))
            y=1;
        else if(this.equals(MapDirection.WEST))
            x=-1;
        else if(this.equals(MapDirection.SOUTH))
            y=-1;
        else if(this.equals(MapDirection.EAST))
            x=1;
        else
            System.out.println("Error: toUnitVector in MapDirection.java");

        return new Vector2d(x, y);
    }
}
