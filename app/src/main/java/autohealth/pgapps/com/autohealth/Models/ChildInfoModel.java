package autohealth.pgapps.com.autohealth.Models;

/**
 * Created by HomePC on 28-02-16.
 */
public class ChildInfoModel {


    public ChildInfoModel()
    {

    }
    public ChildInfoModel(int id, double previousKms, double kilometers,double fuelQty ,double fuelCost,double mileage, double totalCost, boolean isFullTank, String createdDate ) {
        this.Kilometers = kilometers;
        this.PreviousKms = previousKms;
        this.FuelCost = fuelCost;
        this.Fuelqty = fuelQty;
        this.TotalCost = totalCost;
        this.isFullTank = isFullTank;
        this.Mileage = mileage;
        this.ID = id;
        this.CreatedDate = createdDate;
    }
    public ChildInfoModel( double previousKms, double kilometers,double fuelQty ,double fuelCost,double mileage, double totalCost, boolean isFullTank,  String createdDate ) {
        this.Kilometers = kilometers;
        this.PreviousKms = previousKms;
        this.FuelCost = fuelCost;
        this.Fuelqty = fuelQty;
        this.TotalCost = totalCost;
        this.isFullTank = isFullTank;
        this.Mileage = mileage;
        this.CreatedDate = createdDate;

    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String CreatedDate;
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int ID;

    public double Kilometers;
    public double getKilometers() {
        return Kilometers;
    }
    public void setKilometers(double kilometers) {
        Kilometers = kilometers;
    }

    
    public double PreviousKms;
    public double getPreviousKms() {
        return PreviousKms;
    }
    public void setPreviousKms(double previousKms) {
        PreviousKms = previousKms;
    }

    public double Mileage;
    public double getMileage() {
        return Mileage;
    }
    public void setMileage(double mileage) {
        Mileage = mileage;
    }

    public double Fuelqty;
    public double getFuelqty() {
        return Fuelqty;
    }
     public void setFuelqty(double fuelqty) {
        Fuelqty = fuelqty;
    }

    public double FuelCost;
    public double getFuelCost() {
        return FuelCost;
    }
    public void setFuelCost(double fuelCost) {
        FuelCost = fuelCost;
    }


    public double TotalCost;
    public double getTotalCost() {
        return TotalCost;
    }
    public void setTotalCost(double totalCost) {
        TotalCost = totalCost;
    }


    public boolean isFullTank;
    public boolean isFullTank() {
        return isFullTank;
    }
    public void setIsFullTank(boolean isFullTank) {
        this.isFullTank = isFullTank;
    }


}
