package Model;

public class OutSourced extends Part {
    private String companyName;
    public OutSourced(int id, String name, double price, int stock, int min, int max, String companyName){
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }
    /**
     * This will set the company name for the product extension which inherit Product
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    /**
     * Returns the Company Name
     */
    public String getCompanyName() {
        return this.companyName;
    }
}
