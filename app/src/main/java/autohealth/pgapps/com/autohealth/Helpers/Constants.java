package autohealth.pgapps.com.autohealth.Helpers;

import java.util.List;

import autohealth.pgapps.com.autohealth.Models.ChildInfoModel;

/**
 * Created by HomePC on 03-04-16.
 */
public class Constants {


    public static List<ChildInfoModel> fullTankList;

    public static List<ChildInfoModel> getFullTankList() {
        return fullTankList;
    }

    public static void setFullTankList(List<ChildInfoModel> fullTankList) {
        Constants.fullTankList = fullTankList;
    }

    public List<ChildInfoModel> GetFullListTank()
    {
        return  getFullTankList();
    }
}
