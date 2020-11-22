package agh.cs.lab5;

abstract public class AbstractWorldMap implements agh.cs.lab5.IMapElement {

    public abstract boolean canMoveTo(Vector2d position);

    public boolean place(Animal animal) {
        if(canMoveTo(animal.position)){
            if(!isOccupied(animal.position) || objectAt(animal.position) instanceof agh.cs.lab5.Grass)
            System.out.println("Animal added: " + animal.position);
            animals.add(animal);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isOccupied(Vector2d position) {
        // check animals
        for(Animal animal : animals){
            if(position.equals(animal.position)){
                return true;
            }
        }

        return false;
    }

    public Object objectAt(Vector2d position) {
        // return Animal object as first - display priority
        for(agh.cs.lab5.Animal animal : animals){
            if(animal.position.equals(position)){
                return animal;
            }
        }

        return null;
    }

    public abstract String toString(agh.cs.lab5.IWorldMap map);
}
