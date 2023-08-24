package Model;



/**
 * InHouse class with getters and setters for machineId and a constructor.
 */
public class InHouse extends Part {
    private int machineId;

    /**
     * The information for In-house line.

     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId){
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     * Gets the machine ID.
     * @return Machine ID as a number
     */
    public int getMachineId() {
        return this.machineId;
    }
}

