package autohealth.pgapps.com.autohealth.Models;



import java.util.List;

/**
 * Created by HomePC on 28-02-16.
 */
public class ParentInfoModel {


    public double Kms;

    public double getKms() {
        return Kms;
    }

    public void setKms(double kms) {
        Kms = kms;
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

    private List<Object> mChildrenList;

}
